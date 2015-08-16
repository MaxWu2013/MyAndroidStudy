/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.maxwu.smsgroupsend.imgcache;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class wraps up completing some arbitrary long running work when loading
 * a bitmap to an ImageView. It handles things like using a memory and disk
 * cache, running the work in a background thread and setting a placeholder
 * image.
 */
public abstract class ImageWorker {
	private static final String TAG = "ImageWorker";
//	private static final int FADE_IN_TIME = 200;
	
	public static int sWorkerThreadPriority = Thread.NORM_PRIORITY;

	protected ImageCache mImageCache;
	private ImageCache.ImageCacheParams mImageCacheParams;
	private Bitmap mLoadingBitmap;
//	private boolean mFadeInBitmap = true;
	private boolean mExitTasksEarly = false;
	protected boolean mPauseWork = false;
	private final Object mPauseWorkLock = new Object();

	protected Resources mResources;
	protected Context mAppContext;

	private static final int MESSAGE_CLEAR = 0;
	private static final int MESSAGE_INIT_DISK_CACHE = 1;
	private static final int MESSAGE_FLUSH = 2;
	private static final int MESSAGE_CLOSE = 3;

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new PriorityBlockingQueue<Runnable>();

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "ImageLoading #" + mCount.getAndIncrement());
		}
	};

	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
			1, 5, 60, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory,
			new ThreadPoolExecutor.DiscardOldestPolicy());

	HashMap<ImageFetchTask, BitmapWorkerTask> mRunningTasks = new HashMap<ImageFetchTask, BitmapWorkerTask>();

	protected ImageWorker(Context context) {
		mResources = context.getResources();
		mAppContext = context.getApplicationContext();
	}

	private ExecutorService mExecutor;

	public void setExecutor(ExecutorService executor) {
		this.mExecutor = executor;
	}

	public void stopFetcher() {
		synchronized(mRunningTasks){
			mRunningTasks.clear();
		}
		setExitTasksEarly(true);
		setPauseWork(false);
		mExecutor.shutdownNow();
	}
	
	public void clearTasks(){
		for(Entry<ImageFetchTask, BitmapWorkerTask> entry : mRunningTasks.entrySet()){
			entry.getValue().cancel(false);
		}
		synchronized(mRunningTasks){
			mRunningTasks.clear();
		}
	}

	public void loadImage(ImageFetchTask data, View image) {
		loadImage(mExecutor == null ? THREAD_POOL_EXECUTOR : mExecutor, data,
				image);
	}

	/**
	 * Load an image specified by the data parameter into an ImageView (override
	 * {@link ImageWorker# processBitmap(Object)} to define the processing
	 * logic). A memory and disk cache will be used if an {@link ImageCache} has
	 * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
	 * image is found in the memory cache, it is set immediately, otherwise an
	 * {@link AsyncTask} will be created to asynchronously load the bitmap.
	 * 
	 * @param data
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void loadImage(Executor executor, ImageFetchTask data,
			View imageView) {
		if (data == null) {
			return;
		}

		Drawable bitmap = null;

		if (mImageCache != null) {
			bitmap =  data.loadFromMemCache(mImageCache, mResources);//mImageCache.getBitmapFromMemCache(data.getStoreKey());
		}

		if (bitmap != null) {
			// Bitmap found in memory cache
			if (imageView != null)
				data.setDrawable(imageView, bitmap);
			if (data.getListener() != null)
				data.getListener().OnComplete(0);
		} else if (cancelPotentialWork(data, imageView)) {
			synchronized (mRunningTasks) {
				BitmapWorkerTask task = mRunningTasks.get(data);
				if (task == null) {
					task = new BitmapWorkerTask(imageView);
					// NOTE: This uses a custom version of AsyncTask that has
					// been pulled from the
					// framework and slightly modified. Refer to the docs at the
					// top of the class
					// for more info on what was changed.
					task.executeOnExecutor(executor, data);
					mRunningTasks.put(data, task);
				} else {
					if (imageView != null) {
						task.setImageView(imageView);
						task.renewTaskPriority();
					}
				}
				if (imageView != null) {
					final AsyncDrawable asyncDrawable = new AsyncDrawable(
							mResources,
							data.getLoadingBitmap() == null ? mLoadingBitmap
									: data.getLoadingBitmap(), task);
					data.setDrawable(imageView, asyncDrawable);
				}
			}
		}
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param bitmap
	 */
	public void setLoadingImage(Bitmap bitmap) {
		mLoadingBitmap = bitmap;
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param resId
	 */
	public void setLoadingImage(int resId) {
		mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId);
	}

	/**
	 * Adds an {@link ImageCache} to this worker in the background (to prevent
	 * disk access on UI thread).
	 * 
	 * @param fragmentManager
	 * @param cacheParams
	 */
	public void addImageCache(FragmentManager fragmentManager,
			ImageCache.ImageCacheParams cacheParams) {
		mImageCacheParams = cacheParams;
		setImageCache(ImageCache.findOrCreateCache(fragmentManager,
				mImageCacheParams));
		initCache();
	}

	public void initCache() {
		new CacheAsyncTask().execute(MESSAGE_INIT_DISK_CACHE);
	}

	/**
	 * Sets the {@link ImageCache} object to use with this ImageWorker. Usually
	 * you will not need to call this directly, instead use
	 * {@link ImageWorker#addImageCache} which will create and add the
	 * {@link ImageCache} object in a background thread (to ensure no disk
	 * access on the main/UI thread).
	 * 
	 * @param imageCache
	 */
	public void setImageCache(ImageCache imageCache) {
		mImageCache = imageCache;
	}

	public ImageCache getImageCache() {
		return mImageCache;
	}

	/**
	 * If set to true, the image will fade-in once it has been loaded by the
	 * background thread.
	 */
//	public void setImageFadeIn(boolean fadeIn) {
//		mFadeInBitmap = fadeIn;
//	}

	public void setExitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
	}

	/**
	 * Subclasses should override this to define any processing or work that
	 * must happen to produce the final bitmap. This will be executed in a
	 * background thread and be long running. For example, you could resize a
	 * large bitmap here, or pull down an image from the network.
	 * 
	 * @param data
	 *            The data to identify which image to process, as provided by
	 *            {@link ImageWorker#loadImage(Object, ImageView)}
	 * @return The processed bitmap
	 */
	protected abstract Bitmap processBitmap(ImageFetchTask data, int index);

	// /**
	// * Cancels any pending work attached to the provided ImageView.
	// *
	// * @param imageView
	// */
	// public static void cancelWork(ImageView imageView)
	// {
	// final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
	// if (bitmapWorkerTask != null)
	// {
	// bitmapWorkerTask.cancel(true);
	// if (Utils.DEBUG)
	// {
	// final Object bitmapData = bitmapWorkerTask.data;
	// Log.d(TAG, "cancelWork - cancelled work for " + bitmapData);
	// }
	// }
	// }

	/**
	 * Returns true if the current work has been canceled or if there was no
	 * work in progress on this image view. Returns false if the work in
	 * progress deals with the same data. The work is not stopped in that case.
	 */
	public static boolean cancelPotentialWork(ImageFetchTask data,
			View imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(data,imageView);

		if (bitmapWorkerTask != null) {
			final ImageFetchTask bitmapData = bitmapWorkerTask.data;
			if (bitmapData == null || !bitmapData.equals(data)) {
				bitmapWorkerTask.setImageView(null);
				if (Utils.DEBUG) {
					Log.d(TAG, "cancelPotentialWork - cancelled work for "
							+ data);
				}
			} else {
				// The same work is already in progress.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active work task (if any) associated with
	 *         this imageView. null if there is no such task.
	 */
	private static BitmapWorkerTask getBitmapWorkerTask(ImageFetchTask task, View imageView) {
		if (imageView != null) {
			final Drawable drawable = task.getDrawable(imageView);//imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * The actual AsyncTask that will asynchronously process the image.
	 */
	private class BitmapWorkerTask extends
			AsyncTask<ImageFetchTask, Void, Drawable> {
		private ImageFetchTask data;
		private WeakReference<View> imageViewReference;

		public BitmapWorkerTask(View imageView) {
			setImageView(imageView);
		}

		public void setImageView(View imageView) {
			imageViewReference = new WeakReference<View>(imageView);
		}

		/**
		 * Background processing.
		 */
		@Override
		protected Drawable doInBackground(ImageFetchTask... params) {
			Thread.currentThread().setPriority(sWorkerThreadPriority);
			data = params[0];
			if (Utils.DEBUG) {
				Log.d(TAG, "doInBackground - starting work "+data.getImageUrl(0));
			}

			// final String dataString = String.valueOf(data);
			Drawable bitmap = null;

			// Wait here if work is paused and the task is not cancelled
			synchronized (mPauseWorkLock) {
				while (mPauseWork && !isCancelled()) {
					try {
						mPauseWorkLock.wait();
					} catch (InterruptedException e) {
					}
				}
			}

			// If the image cache is available and this task has not been
			// cancelled by another
			// thread and the ImageView that was originally bound to this task
			// is still bound back
			// to this task and our "exit early" flag is not set then try and
			// fetch the bitmap from
			// the cache
			if (mImageCache != null && !isCancelled() && !mExitTasksEarly && imageViewReference.get() != null) {
				bitmap = data.loadFromDiskCache(mImageCache, mResources);//mImageCache.getBitmapFromDiskCache(data.getStoreKey());
			}

			// If the bitmap was not found in the cache and this task has not
			// been cancelled by
			// another thread and the ImageView that was originally bound to
			// this task is still
			// bound back to this task and our "exit early" flag is not set,
			// then call the main
			// process method (as implemented by a subclass)
			if (bitmap == null && !isCancelled() && !mExitTasksEarly && imageViewReference.get() != null) {
				long start = System.currentTimeMillis();
				for(int i=0;i<params[0].getImageCount();i++){
					if(data.getImageUrl(i) == null || data.getImageUrl(i).length() == 0)
						continue;
					Bitmap bmp = processBitmap(data, i);
					if(bmp != null && mImageCache != null)
						mImageCache.addBitmapToCache(data.getStoreKey(i), bmp);
				}
				if (Utils.DEBUG)
					Log.d(TAG,
							"bitmap download time: "
									+ (System.currentTimeMillis() - start) +" "+data.getImageUrl(0));
				bitmap = data.loadFromMemCache(mImageCache, mResources);
			}

			
			if (Utils.DEBUG) {
				Log.d(TAG,
						"doInBackground - finished work ");
			}

			return bitmap;
		}

		/**
		 * Once the image is processed, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Drawable bitmap) {
			// if cancel was called on this task or the "exit early" flag is set
			// then we're done
			if (isCancelled() || mExitTasksEarly) {
				bitmap = null;
			}

			final View imageView = getAttachedImageView(data);
			if (bitmap != null && imageView != null) {
				if (Utils.DEBUG) {
					Log.d(TAG, "onPostExecute - setting bitmap");
				}
				data.setDrawable(imageView, bitmap);

				if (data.getListener() != null) {
					data.getListener().OnComplete(0);
				}
			} else if(imageView != null) {
				if(data.getErrorBitmap() != null)
					data.setDrawable(imageView, new BitmapDrawable(mResources, data.getErrorBitmap()));
				if (data.getListener() != null) {
					data.getListener().OnComplete(-1);
				}
			}
			synchronized (mRunningTasks) {
				mRunningTasks.remove(data);
			}
		}

		@Override
		protected void onCancelled(Drawable bitmap) {
			super.onCancelled(bitmap);
			synchronized (mPauseWorkLock) {
				mPauseWorkLock.notifyAll();
			}
		}

		/**
		 * Returns the ImageView associated with this task as long as the
		 * ImageView's task still points to this task as well. Returns null
		 * otherwise.
		 */
		private View getAttachedImageView(ImageFetchTask task) {
			final View imageView = imageViewReference.get();
			final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(task,imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

	/**
	 * A custom Drawable that will be attached to the imageView while the work
	 * is in progress. Contains a reference to the actual worker task, so that
	 * it can be stopped if a new binding is required, and makes sure that only
	 * the last started worker process can bind its result, independently of the
	 * finish order.
	 */
	public static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

//	/**
//	 * Called when the processing is complete and the final bitmap should be set
//	 * on the ImageView.
//	 *
//	 * @param imageView
//	 * @param bitmap
//	 */
//	private void setImageBitmap(View imageView, Drawable bitmap) {
//		if (mFadeInBitmap) {
//			// Transition drawable with a transparent drwabale and the final
//			// bitmap
//			final TransitionDrawable td = new TransitionDrawable(
//					new Drawable[] {
//							new ColorDrawable(android.R.color.transparent),
//							bitmap });
//			// Set background to loading bitmap
//			imageView.setBackgroundDrawable(new BitmapDrawable(mResources,
//					mLoadingBitmap));
//
//			imageView.setImageDrawable(td);
//			td.startTransition(FADE_IN_TIME);
//		} else {
//			imageView.setImageDrawable(bitmap);
//		}
//	}

	public void setPauseWork(boolean pauseWork) {
		synchronized (mPauseWorkLock) {
			mPauseWork = pauseWork;
			if (!mPauseWork) {
				mPauseWorkLock.notifyAll();
			}
		}
	}

	protected class CacheAsyncTask extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... params) {
			switch ((Integer) params[0]) {
			case MESSAGE_CLEAR:
				clearCacheInternal();
				break;
			case MESSAGE_INIT_DISK_CACHE:
				initDiskCacheInternal();
				break;
			case MESSAGE_FLUSH:
				flushCacheInternal();
				break;
			case MESSAGE_CLOSE:
				closeCacheInternal();
				break;
			}
			return null;
		}
	}

	protected void initDiskCacheInternal() {
		if (mImageCache != null) {
			mImageCache.initDiskCache();
		}
	}

	protected void clearCacheInternal() {
		if (mImageCache != null) {
			mImageCache.clearCache();
		}
	}

	protected void flushCacheInternal() {
		if (mImageCache != null) {
			mImageCache.flush();
		}
	}

	protected void closeCacheInternal() {
		if (mImageCache != null) {
			mImageCache.close();
			mImageCache = null;
		}
	}

	public void clearCache() {
		new CacheAsyncTask().execute(MESSAGE_CLEAR);
	}

	public void flushCache() {
		new CacheAsyncTask().execute(MESSAGE_FLUSH);
	}

	public void closeCache() {
		new CacheAsyncTask().execute(MESSAGE_CLOSE);
	}
}
