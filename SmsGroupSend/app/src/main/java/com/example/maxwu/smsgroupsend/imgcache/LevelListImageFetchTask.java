package com.example.maxwu.smsgroupsend.imgcache;

import java.util.ArrayList;
import java.util.List;

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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class LevelListImageFetchTask implements ImageFetchTask {
	List<int[]> levels = new ArrayList<int[]>();
	List<String> urls = new ArrayList<String>();

	private int mWidth;
	private int mHeight;
	private int corners = 10;

	private Bitmap loadingBitmap;
	private Bitmap errorBitmap;

	private ImageFetchListener listener;

	private TaskType mTaskType = TaskType.IMAGE;

	private CachePolicy mCachePolicy = CachePolicy.MEM_AND_DISK;

	public LevelListImageFetchTask(int targetWidth, int targetHeight) {
		this.mWidth = targetWidth;
		this.mHeight = targetHeight;
	}

	public void addLevelUrl(int[] level, String url) {
		if (level != null && level.length > 1 && url != null
				&& url.length() > 0) {
			levels.add(level);
			urls.add(url);
		}
	}

	@Override
	public int getImageCount() {
		return urls.size();
	}

	@Override
	public String getImageUrl(int index) {
		if (index >= urls.size())
			return null;
		return urls.get(index);
	}

	@Override
	public String getStoreKey(int index) {
		return mWidth + "x" + mHeight + urls.get(index);
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
		LevelListDrawable sld = new LevelListDrawable();
		for (int i = 0; i < urls.size(); i++) {
			Bitmap bmp = cache.getBitmapFromMemCache(getStoreKey(i));
			if (mCachePolicy == CachePolicy.NO_CACHE)
				cache.removeBitmapFromCache(getStoreKey(i));
			if (bmp == null)
				return null;
			sld.addLevel(levels.get(i)[0], levels.get(i)[1],
					new BitmapDrawable(res, bmp));
		}
		return sld;
	}

	@Override
	public Drawable loadFromDiskCache(ImageCache cache, Resources res) {
		LevelListDrawable sld = new LevelListDrawable();
		for (int i = 0; i < urls.size(); i++) {
			Bitmap bmp = cache.getBitmapFromDiskCache(getStoreKey(i));
			if (bmp == null)
				return null;
			cache.addBitmapToCache(getStoreKey(i), bmp);
			sld.addLevel(levels.get(i)[0], levels.get(i)[1],
					new BitmapDrawable(res, bmp));
		}
		return sld;
	}

	@Override
	public ImageFetchListener getListener() {
		return listener;
	}

	public void setListener(ImageFetchListener l) {
		this.listener = l;
	}

	public void setTaskType(TaskType type) {
		this.mTaskType = type;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof LevelListImageFetchTask) {
			LevelListImageFetchTask task = (LevelListImageFetchTask) o;

			if (mTaskType == task.mTaskType && urls.size() == task.urls.size()) {
				for (int i = 0; i < urls.size(); i++) {
					if (!getStoreKey(i).equals(task.getStoreKey(i)))
						return false;
				}
				return true;
			}

			return false;

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
