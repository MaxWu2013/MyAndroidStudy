package com.example.maxwu.smsgroupsend.imgcache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface ImageFetchTask {
	public enum TaskType{
		IMAGE,BACKGROUND,IMAGECORNER,TEXT_LEFT,TEXT_TOP,TEXT_RIGHT,TEXT_BOTTOM,GIF_ANIM
	}
	
	public enum CachePolicy{
		MEM_AND_DISK, MEM_ONLY, NO_CACHE
	}
	
	public CachePolicy getTaskCachePolicy();

	public int getImageCount();
	public String getImageUrl(int index);
	public String getStoreKey(int index);
	
	public Bitmap getLoadingBitmap();
	
	public Bitmap getErrorBitmap();
	
	public Drawable loadFromMemCache(ImageCache cache, Resources res);
	
	public Drawable loadFromDiskCache(ImageCache cache, Resources res);
	
	public ImageFetchListener getListener();
	
	public Drawable getDrawable(View view);
	
	public void setDrawable(View view, Drawable drawable);
	
	public int getDesiredWidth();
	public int getDesiredHeight();
	public int getCorners();
}
