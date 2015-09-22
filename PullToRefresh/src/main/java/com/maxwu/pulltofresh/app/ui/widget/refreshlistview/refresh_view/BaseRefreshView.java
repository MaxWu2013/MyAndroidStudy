package com.maxwu.pulltofresh.app.ui.widget.refreshlistview.refresh_view;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import android.util.Log;

import com.maxwu.pulltofresh.app.ui.widget.refreshlistview.PullToRefreshView;


public abstract class BaseRefreshView extends Drawable implements Drawable.Callback, Animatable {

    private PullToRefreshView mRefreshLayout;
    private boolean mEndOfRefreshing;

    public BaseRefreshView(Context context, PullToRefreshView layout) {
        mRefreshLayout = layout;
    }

    public Context getContext() {
        return mRefreshLayout != null ? mRefreshLayout.getContext() : null;
    }

    public PullToRefreshView getRefreshLayout() {
        return mRefreshLayout;
    }

    public abstract void setPercent(float percent, boolean invalidate);

    public abstract void offsetTopAndBottom(int offset);

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
		Log.v("MaxWu","BRV invalidateDrawable");
        final Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
		Log.v("MaxWu","BRV scheduleDrawable");
        final Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
		Log.v("MaxWu","BRV unscheduleDrawablef");
        final Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    @Override
    public int getOpacity() {
		Log.v("MaxWu","BRV getOpacity");
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
		Log.v("MaxWu","BRV setAlpha");
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
		Log.v("MaxWu","BRV setColorFilter");
    }

    /**
     * Our animation depend on type of current work of refreshing.
     * We should to do different things when it's end of refreshing
     *
     * @param endOfRefreshing - we will check current state of refresh with this
     */
    public void setEndOfRefreshing(boolean endOfRefreshing) {
		Log.v("MaxWu","BRV setEndOfRefreshing");
        mEndOfRefreshing = endOfRefreshing;
    }
}
