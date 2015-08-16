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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.View;

import com.example.maxwu.smsgroupsend.Logger;
import com.example.maxwu.smsgroupsend.http.AsyncHttpClient;


/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images
 * fetched from a URL.
 */
public class ImageFetcher extends ImageResizer
{
    private static final String TAG            = "ImageFetcher";
    //    private static final int    HTTP_CACHE_SIZE        = 10 * 1024 * 1024; // 10MB
    //    private static final String HTTP_CACHE_DIR         = "http";
    private static final int    IO_BUFFER_SIZE = 4 * 1024;

    //    private DiskLruCache        mHttpDiskCache;
    private File                tempDir;

    //    private boolean             mHttpDiskCacheStarting = true;
    //    private final Object        mHttpDiskCacheLock     = new Object();
    //    private static final int    DISK_CACHE_INDEX       = 0;

    AsyncHttpClient mClient = new AsyncHttpClient();
    /**
     * Initialize providing a target image width and height for the processing
     * images.
     * 
     * @param context
     * @param imageWidth
     * @param imageHeight
     */
    public ImageFetcher(Context context, int imageWidth, int imageHeight)
    {
        super(context, imageWidth, imageHeight);
        init(context);
    }

    /**
     * Initialize providing a single target image size (used for both width and
     * height);
     * 
     * @param context
     * @param imageSize
     */
    public ImageFetcher(Context context, int imageSize)
    {
        super(context, imageSize);
        init(context);
    }

    private void init(Context context)
    {
        checkConnection(context);
        tempDir = ImageCache.getDiskCacheDir(context, "temp");
    }

    @Override
    protected void initDiskCacheInternal()
    {
        super.initDiskCacheInternal();
        initHttpDiskCache();
    }

    private void initHttpDiskCache()
    {
                if (!tempDir.exists())
                {
                    tempDir.mkdirs();
                }
        //        synchronized (mHttpDiskCacheLock)
        //        {
        //            if (ImageCache.getUsableSpace(mHttpCacheDir) > HTTP_CACHE_SIZE)
        //            {
        //                try
        //                {
        //                    mHttpDiskCache = DiskLruCache.open(mHttpCacheDir, 1, 1, HTTP_CACHE_SIZE);
        //                    if (Utils.DEBUG)
        //                    {
        //                        Log.d(TAG, "HTTP cache initialized");
        //                    }
        //                } catch (IOException e)
        //                {
        //                    mHttpDiskCache = null;
        //                }
        //            }
        //            mHttpDiskCacheStarting = false;
        //            mHttpDiskCacheLock.notifyAll();
        //        }
    }

    //    @Override
    //    protected void clearCacheInternal()
    //    {
    //        super.clearCacheInternal();
    //        synchronized (mHttpDiskCacheLock)
    //        {
    //            if (mHttpDiskCache != null && !mHttpDiskCache.isClosed())
    //            {
    //                try
    //                {
    //                    mHttpDiskCache.delete();
    //                    if (Utils.DEBUG)
    //                    {
    //                        Log.d(TAG, "HTTP cache cleared");
    //                    }
    //                } catch (IOException e)
    //                {
    //                    Log.e(TAG, "clearCacheInternal - " + e);
    //                }
    //                mHttpDiskCache = null;
    //                mHttpDiskCacheStarting = true;
    //                initHttpDiskCache();
    //            }
    //        }
    //    }

    //    @Override
    //    protected void flushCacheInternal()
    //    {
    //        super.flushCacheInternal();
    //        synchronized (mHttpDiskCacheLock)
    //        {
    //            if (mHttpDiskCache != null)
    //            {
    //                try
    //                {
    //                    mHttpDiskCache.flush();
    //                    if (Utils.DEBUG)
    //                    {
    //                        Log.d(TAG, "HTTP cache flushed");
    //                    }
    //                } catch (IOException e)
    //                {
    //                    Log.e(TAG, "flush - " + e);
    //                }
    //            }
    //        }
    //    }
    //
    //    @Override
    //    protected void closeCacheInternal()
    //    {
    //        super.closeCacheInternal();
    //        synchronized (mHttpDiskCacheLock)
    //        {
    //            if (mHttpDiskCache != null)
    //            {
    //                try
    //                {
    //                    if (!mHttpDiskCache.isClosed())
    //                    {
    //                        mHttpDiskCache.close();
    //                        mHttpDiskCache = null;
    //                        if (Utils.DEBUG)
    //                        {
    //                            Log.d(TAG, "HTTP cache closed");
    //                        }
    //                    }
    //                } catch (IOException e)
    //                {
    //                    Log.e(TAG, "closeCacheInternal - " + e);
    //                }
    //            }
    //        }
    //    }

    /**
     * Simple network connection check.
     * 
     * @param context
     */
    private void checkConnection(Context context)
    {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting())
        {
            //Toast.makeText(context, "No connection!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "checkConnection - no connection found");
        }
    }
    
    public void loadImage(String data, View image){
    	loadImage(new BaseImageFetchTask(data, mImageWidth,mImageHeight), image);
    }
    
    public StateListImageFetchTask getTask(){
    	return new StateListImageFetchTask(mImageWidth, mImageHeight);
    }

	public LevelListImageFetchTask getLevelListTask() {
		return new LevelListImageFetchTask(mImageWidth, mImageHeight);
	}
    
    public BaseImageFetchTask getBaseTask(String url){
    	return new BaseImageFetchTask(url, mImageWidth, mImageHeight);
    }

    public void loadImage(String data, String key, View image){
    	loadImage(new BaseImageFetchTask(data, mImageWidth,mImageHeight,key), image);
    }
    
    /**
     * The main process method, which will be called by the ImageWorker in the
     * AsyncTask background thread.
     * 
     * @param data
     *            The data to load the bitmap, in this case, a regular http URL
     * @return The downloaded and resized bitmap
     */
    @Override
    protected Bitmap processBitmap(ImageFetchTask data, int index)
    {
        if (Utils.DEBUG)
        {
            Log.d(TAG, "processBitmap - " + data.getImageUrl(index));
        }
        File temp = null;
        Bitmap bmp = null;
        boolean diskCache = true;
        try
        {
        	String url = data.getImageUrl(index);
        	Logger.d("image url: " + url);
        	Uri uri = Uri.parse(url);
        	if("file".equalsIgnoreCase(uri.getScheme())){
        		Logger.d("encoded path: "+uri.getEncodedPath());
        		diskCache = false;
        		bmp = decodeSampledBitmapFromFile(uri.getEncodedPath(), data.getDesiredWidth(), data.getDesiredHeight());
        	} else if ("asset".equalsIgnoreCase(uri.getScheme())){
        		diskCache = false;
        		String path = uri.getEncodedPath();
        		if(path.startsWith("/"))
        			path = path.substring(1);
        		Logger.d("asset path: "+ path);
				bmp = decodeSampledBitmapFromAsset(mResources.getAssets(), path,
						data.getDesiredWidth(), data.getDesiredHeight());
        	} else if ("content".equalsIgnoreCase(uri.getScheme())){
        		diskCache = false;
        		Logger.d("content path: "+url);
        		bmp = decodeSampledBitmapFromInputStream(mAppContext.getContentResolver().openInputStream(uri), data.getDesiredWidth(), data.getDesiredHeight());
        	} else if("video".equals(uri.getScheme())){
        		Logger.d("encoded path: "+uri.getEncodedPath());
        		bmp = ThumbnailUtils.createVideoThumbnail(uri.getEncodedPath(), Thumbnails.MINI_KIND);
        	} else {
	        	Logger.d(tempDir.getAbsolutePath());
	            temp = File.createTempFile("img_"+Math.random(), ".jpg", tempDir);
	            if (downloadUrlToStream(data.getImageUrl(index), new FileOutputStream(temp)))
	            {
	                bmp = decodeSampledBitmapFromFile(temp.getAbsolutePath(), data.getDesiredWidth(), data.getDesiredHeight());
	            }
        	}
        } catch (Exception e)
        {
            Log.e(TAG, "processBitmap - " + e);
        } catch (OutOfMemoryError oom){
        	Logger.printMemory("OOM error "+data.getImageUrl(index));
        } finally
        {
			if (bmp != null && mImageCache != null && diskCache
					&& data.getTaskCachePolicy() == ImageFetchTask.CachePolicy.MEM_AND_DISK)
				mImageCache.saveBitmapToDisk(data.getStoreKey(index), bmp);
            
            if(temp != null)
                temp.delete();
        }
        return bmp;
    }

    /**
     * Download a bitmap from a URL and write the content to an output stream.
     * 
     * @param urlString
     *            The URL to fetch
     * @return true if successful, false otherwise
     */
    public boolean downloadUrlToStream(String urlString, OutputStream outputStream)
    {
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try
        {
            HttpResponse response = mClient.syncGetRaw(urlString, null);
            in = new BufferedInputStream(response.getEntity().getContent(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            byte[] buffer = new byte[IO_BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, len);
            }
            return true;
        } catch (final IOException e)
        {
            Log.e(TAG, "Error in downloadBitmap - " + urlString, e);
        } finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            } catch (final IOException e)
            {
            }
        }
        return false;
    }

//    /**
//     * Workaround for bug pre-Froyo, see here for more info:
//     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
//     */
//    public static void disableConnectionReuseIfNecessary()
//    {
//        // HTTP connection reuse which was buggy pre-froyo
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
//        {
//            System.setProperty("http.keepAlive", "false");
//        }
//    }
}
