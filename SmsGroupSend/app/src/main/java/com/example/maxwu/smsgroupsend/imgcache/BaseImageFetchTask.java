package com.example.maxwu.smsgroupsend.imgcache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class BaseImageFetchTask implements ImageFetchTask {
	public static boolean enableTransition = false;
	private String url;

	private String key;

	private ImageFetchListener listener;

	private TaskType mTaskType = TaskType.IMAGE;

	private Rect patch;
	private Rect padding;

	private int mWidth;
	private int mHeight;
	private int corners = 10;

	private Bitmap loadingBitmap;
	private Bitmap errorBitmap;

	private CachePolicy mCachePolicy = CachePolicy.MEM_AND_DISK;

	public BaseImageFetchTask(String url, int targetWidth, int targetHeight) {
		this.url = url;
		this.key = url;
		this.mWidth = targetWidth;
		this.mHeight = targetHeight;
	}

	public BaseImageFetchTask(String url, int targetWidth, int targetHeight,
			String key) {
		this(url, targetWidth, targetHeight);
		this.key = key;
	}

	public void setSize(int size) {
		this.mWidth = size;
		this.mHeight = size;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public BaseImageFetchTask setListener(ImageFetchListener l) {
		this.listener = l;
		return this;
	}

	public void setNinePatch(Rect patch, Rect padding) {
		this.patch = patch;
		this.padding = padding;
	}

	@Override
	public int getImageCount() {
		return 1;
	}

	@Override
	public String getImageUrl(int index) {
		return url;
	}

	@Override
	public String getStoreKey(int index) {
		return mWidth + "x" + mHeight + key;

	}

	public Bitmap getLoadingBitmap() {
		return loadingBitmap;
	}

	public void setLoadingBitmap(Bitmap loadingBitmap) {
		this.loadingBitmap = loadingBitmap;
	}

	public Bitmap getErrorBitmap() {
		return errorBitmap;
	}

	public void setErrorBitmap(Bitmap errorBitmap) {
		this.errorBitmap = errorBitmap;
	}

	@Override
	public Drawable loadFromMemCache(ImageCache cache, Resources res) {
		Bitmap bmp = cache.getBitmapFromMemCache(getStoreKey(0));
		if (bmp != null) {
			if (mCachePolicy == CachePolicy.NO_CACHE)
				cache.removeBitmapFromCache(getStoreKey(0));
			return new BitmapDrawable(res, bmp);
		}
		return null;
	}

	@Override
	public Drawable loadFromDiskCache(ImageCache cache, Resources res) {
		Bitmap bmp = cache.getBitmapFromDiskCache(getStoreKey(0));
		if (bmp != null) {
			cache.addBitmapToCache(getStoreKey(0), bmp);
			return new BitmapDrawable(res, bmp);
		}
		return null;
	}

	@Override
	public ImageFetchListener getListener() {
		return listener;
	}

	public void setTaskType(TaskType type) {
		this.mTaskType = type;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof BaseImageFetchTask) {
			BaseImageFetchTask task = (BaseImageFetchTask) o;
			return this.mTaskType == task.mTaskType
					&& getStoreKey(0).equals(task.getStoreKey(0));
		}
		return super.equals(o);
	}

	@Override
	public Drawable getDrawable(View view) {
		switch (mTaskType) {
		case TEXT_LEFT:
			if (view instanceof TextView) {
				TextView tv = (TextView) view;
				return tv.getCompoundDrawables()[0];
			}
			break;
		case TEXT_TOP:
			if (view instanceof TextView) {
				TextView tv = (TextView) view;
				return tv.getCompoundDrawables()[1];
			}
			break;
		case TEXT_RIGHT:
			if (view instanceof TextView) {
				TextView tv = (TextView) view;
				return tv.getCompoundDrawables()[2];
			}
			break;
		case TEXT_BOTTOM:
			if (view instanceof TextView) {
				TextView tv = (TextView) view;
				return tv.getCompoundDrawables()[3];
			}
			break;
		case BACKGROUND:
			return view.getBackground();
		case IMAGE:
			if (view instanceof ImageView)
				return ((ImageView) view).getDrawable();
			return view.getBackground();
		case IMAGECORNER:
			if (view instanceof ImageView) {
				Drawable drawable = ((ImageView) view).getDrawable();
				return drawable;
			} else {
				return view.getBackground();
			}
		default:
			break;
		}
		return null;
	}

	@Override
	public void setDrawable(View view, Drawable drawable) {
		if (drawable instanceof BitmapDrawable
				&& !(drawable instanceof ImageWorker.AsyncDrawable)) {
			if (patch != null && padding != null) {
				Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
				drawable = NinePatchBitmapFactory.createNinePathWithCapInsets(
						view.getResources(), bmp, patch.top, patch.right,
						bmp.getHeight() - patch.bottom, bmp.getWidth()
								- patch.right, padding, null);
			}
		}
		TransitionDrawable transDrawable = null;
		if (enableTransition && !(drawable instanceof ImageWorker.AsyncDrawable)
				&& getDrawable(view) instanceof ImageWorker.AsyncDrawable)
			transDrawable = new TransitionDrawable(new Drawable[] {
					new ColorDrawable(0), drawable });
		if (transDrawable != null)
			drawable = transDrawable;
		switch (mTaskType) {
		case TEXT_LEFT:
			if (view instanceof TextView) {
				TextView tv = (TextView) view;
				Drawable[] drawables = tv.getCompoundDrawables();
				tv.setCompoundDrawables(drawable, drawables[1], drawables[2],
						drawables[3]);
			}
			break;
		case TEXT_TOP:
			if (view instanceof TextView) {
				TextView tv = (TextView) view;
				Drawable[] drawables = tv.getCompoundDrawables();
				tv.setCompoundDrawables(drawables[0], drawable, drawables[2],
						drawables[3]);
			}
			break;
		case TEXT_RIGHT:
			if (view instanceof TextView) {
				TextView tv = (TextView) view;
				Drawable[] drawables = tv.getCompoundDrawables();
				tv.setCompoundDrawables(drawables[0], drawables[1], drawable,
						drawables[3]);
			}
			break;
		case TEXT_BOTTOM:
			if (view instanceof TextView) {
				TextView tv = (TextView) view;
				Drawable[] drawables = tv.getCompoundDrawables();
				tv.setCompoundDrawables(drawables[0], drawables[1],
						drawables[2], drawable);
			}
			break;
		case BACKGROUND:
			view.setBackgroundDrawable(drawable);
			break;
		case IMAGE:
			if (view instanceof ImageView)
				((ImageView) view).setImageDrawable(drawable);
			else
				view.setBackgroundDrawable(drawable);
			break;
		case IMAGECORNER:
			if (drawable != null) {
				if (drawable instanceof BitmapDrawable
						&& !(drawable instanceof ImageWorker.AsyncDrawable)) {
					Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
					bmp = toRoundCorner(bmp);
					drawable = new BitmapDrawable(bmp);
				}
			}
			if (view instanceof ImageView) {
				((ImageView) view).setImageDrawable(drawable);
			} else {
				view.setBackgroundDrawable(drawable);
			}
			break;
		default:
			break;

		}
		if (transDrawable != null)
			transDrawable.startTransition(500);
	}

	@Override
	public int getDesiredWidth() {
		return mWidth;
	}

	@Override
	public int getDesiredHeight() {
		return mHeight;
	}

	public CachePolicy getTaskCachePolicy() {
		return mCachePolicy;
	}

	public void setTaskCachePolicy(CachePolicy mCachePolicy) {
		this.mCachePolicy = mCachePolicy;
	}

	public void setCorners(int corners) {
		this.corners = corners;
	}

	@Override
	public int getCorners() {
		// TODO Auto-generated method stub
		return corners;
	}

	/**
	 * 画图
	 * 
	 * @param bitmap
	 * @return
	 */
	public Bitmap toRoundCorner(Bitmap bitmap) {
		if (bitmap == null) {
			return bitmap;
		}
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = corners;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
