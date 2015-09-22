package com.maxwu.pulltofresh.app.ui.widget.refreshlistview.refresh_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.maxwu.pulltofresh.app.R;
import com.maxwu.pulltofresh.app.ui.Utils;
import com.maxwu.pulltofresh.app.ui.widget.refreshlistview.PullToRefreshView;


/**
 * Created by Oleksii Shliama on 22/12/2014.
 * https://dribbble.com/shots/1650317-Pull-to-Refresh-Rentals
 */
public class SunRefreshView extends BaseRefreshView implements Animatable {

    private static final float SCALE_START_PERCENT = 0.5f;
    private static final int ANIMATION_DURATION = 1000;

    private final static float SKY_RATIO = 0.65f;
    private static final float SKY_INITIAL_SCALE = 1.05f;

    private final static float TOWN_RATIO = 0.22f;
    private static final float TOWN_INITIAL_SCALE = 1.20f;
    private static final float TOWN_FINAL_SCALE = 1.30f;

    private static final float SUN_FINAL_SCALE = 0.75f;
    private static final float SUN_INITIAL_ROTATE_GROWTH = 1.2f;
    private static final float SUN_FINAL_ROTATE_GROWTH = 1.5f;

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    private PullToRefreshView mParent;
    private Matrix mMatrix;
    private Animation mAnimation;

    private int mTop;
    private int mScreenWidth;

    private int mSkyHeight;
    private float mSkyTopOffset;
    private float mSkyMoveOffset;

    private int mTownHeight;
    private float mTownInitialTopOffset;
    private float mTownFinalTopOffset;
    private float mTownMoveOffset;

    private int mSunSize = 100;
    private float mSunLeftOffset;
    private float mSunTopOffset;

    private float mPercent = 0.0f;
    private float mRotate = 0.0f;

    private Bitmap mSky;
    private Bitmap mSun;
    private Bitmap mTown;

    private boolean isRefreshing = false;

    public SunRefreshView(Context context, final PullToRefreshView parent) {
        super(context, parent);
        mParent = parent;
        mMatrix = new Matrix();

        setupAnimations(); 
        parent.post(new Runnable() {
            @Override
            public void run() {
                initiateDimens(parent.getWidth());
            }
        });
    }

    public void initiateDimens(int viewWidth) {
		Log.v("MaxWu","SRV initiateDimens viewWidth="+viewWidth);
        if (viewWidth <= 0 || viewWidth == mScreenWidth) return;

        mScreenWidth = viewWidth;
        mSkyHeight = (int) (SKY_RATIO * mScreenWidth);
        mSkyTopOffset = (mSkyHeight * 0.38f);
        mSkyMoveOffset = Utils.convertDpToPixel(getContext(), 15);

        mTownHeight = (int) (TOWN_RATIO * mScreenWidth);
        mTownInitialTopOffset = (mParent.getTotalDragDistance() - mTownHeight * TOWN_INITIAL_SCALE);
        mTownFinalTopOffset = (mParent.getTotalDragDistance() - mTownHeight * TOWN_FINAL_SCALE);
        mTownMoveOffset = Utils.convertDpToPixel(getContext(), 10);

        mSunLeftOffset = 0.3f * (float) mScreenWidth;
        mSunTopOffset = (mParent.getTotalDragDistance() * 0.1f);

        mTop = -mParent.getTotalDragDistance();

		
		Log.v("MaxWu","SRV  mSkyHeight="+mSkyHeight);
		Log.v("MaxWu","SRV  mSkyTopOffset="+mSkyTopOffset);
		Log.v("MaxWu","SRV  mSkyMoveOffset="+mSkyMoveOffset);
		Log.v("MaxWu","SRV  mTownHeight="+mTownHeight);
		Log.v("MaxWu","SRV  mTownInitialTopOffset="+mTownInitialTopOffset);
		Log.v("MaxWu","SRV  mTownMoveOffset="+mTownMoveOffset);
		Log.v("MaxWu","SRV  mSunLeftOffset="+mSunLeftOffset);
		Log.v("MaxWu","SRV  mSunTopOffset="+mSunTopOffset);
		Log.v("MaxWu","SRV  mTop="+mTop);
		
        createBitmaps();
    }

    private void createBitmaps() {
        mSky = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sky);
        mSky = Bitmap.createScaledBitmap(mSky, mScreenWidth, mSkyHeight, true);
        mTown = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.buildings);
        mTown = Bitmap.createScaledBitmap(mTown, mScreenWidth, (int) (mScreenWidth * TOWN_RATIO), true);
        mSun = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sun);
        mSun = Bitmap.createScaledBitmap(mSun, mSunSize, mSunSize, true);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
		Log.v("MaxWu","SRV setPercent percent="+percent+" invalidate="+invalidate);
        setPercent(percent);
        if (invalidate) setRotate(percent);
    }

    @Override
    public void offsetTopAndBottom(int offset) {
		Log.v("MaxWu","SRV offsetTopAndBottom offset="+offset);
        mTop += offset;
		
		Log.v("MaxWu","SRV mTop="+mTop);
		
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
		Log.v("MaxWu","SRV draw");
        if (mScreenWidth <= 0) return;

        final int saveCount = canvas.save();

        canvas.translate(0, mTop);
        canvas.clipRect(0, -mTop, mScreenWidth, mParent.getTotalDragDistance());

		Log.v("MaxWu","SRV draw mTop="+mTop);
		Log.v("MaxWu","SRV draw totalDragDistance="+mParent.getTotalDragDistance());
		
		
        drawSky(canvas);
        drawSun(canvas);
        drawTown(canvas);

        canvas.restoreToCount(saveCount);
    }

    private void drawSky(Canvas canvas) {
		Log.v("MaxWu","SRV drawSky");
        Matrix matrix = mMatrix;
        matrix.reset();
        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float skyScale;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            /** Change skyScale between {@link #SKY_INITIAL_SCALE} and 1.0f depending on {@link #mPercent} */
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            skyScale = SKY_INITIAL_SCALE - (SKY_INITIAL_SCALE - 1.0f) * scalePercent;
        } else {
            skyScale = SKY_INITIAL_SCALE;
        }

        float offsetX = -(mScreenWidth * skyScale - mScreenWidth) / 2.0f;
        float offsetY = (1.0f - dragPercent) * mParent.getTotalDragDistance() - mSkyTopOffset // Offset canvas moving
                - mSkyHeight * (skyScale - 1.0f) / 2 // Offset sky scaling
                + mSkyMoveOffset * dragPercent; // Give it a little move top -> bottom

		Log.v("MaxWu","SRV drawSky scalePercentDelta="+scalePercentDelta);		
		Log.v("MaxWu","SRV drawSky skyScale="+skyScale);		
		Log.v("MaxWu","SRV drawSky offsetX="+offsetX);		
		Log.v("MaxWu","SRV drawSky offsetY="+offsetY);
				
        matrix.postScale(skyScale, skyScale);
        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mSky, matrix, null);
    }

    private void drawTown(Canvas canvas) {
		Log.v("MaxWu","SRV drawTown");
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float townScale;
        float townTopOffset;
        float townMoveOffset;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            /**
             * Change townScale between {@link #TOWN_INITIAL_SCALE} and {@link #TOWN_FINAL_SCALE} depending on {@link #mPercent}
             * Change townTopOffset between {@link #mTownInitialTopOffset} and {@link #mTownFinalTopOffset} depending on {@link #mPercent}
             */
			Log.v("MaxWu","SRV drawTown  scalePercentDelta > 0");
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            townScale = TOWN_INITIAL_SCALE + (TOWN_FINAL_SCALE - TOWN_INITIAL_SCALE) * scalePercent;
            townTopOffset = mTownInitialTopOffset - (mTownFinalTopOffset - mTownInitialTopOffset) * scalePercent;
            townMoveOffset = mTownMoveOffset * (1.0f - scalePercent);
        } else {
            float scalePercent = dragPercent / SCALE_START_PERCENT;
            townScale = TOWN_INITIAL_SCALE;
            townTopOffset = mTownInitialTopOffset;
            townMoveOffset = mTownMoveOffset * scalePercent;
        }

        float offsetX = -(mScreenWidth * townScale - mScreenWidth) / 2.0f;
        float offsetY = (1.0f - dragPercent) * mParent.getTotalDragDistance() // Offset canvas moving
                + townTopOffset
                - mTownHeight * (townScale - 1.0f) / 2 // Offset town scaling
                + townMoveOffset; // Give it a little move

		Log.v("MaxWu","SRV drawTown scalePercentDelta="+scalePercentDelta);		
		Log.v("MaxWu","SRV drawTown townScale="+townScale);
		Log.v("MaxWu","SRV drawTown townTopOffset="+townTopOffset);
		Log.v("MaxWu","SRV drawTown townMoveOffset="+townMoveOffset);
		Log.v("MaxWu","SRV drawTown offsetX="+offsetX);
		Log.v("MaxWu","SRV drawTown offsetY="+offsetY);
				
        matrix.postScale(townScale, townScale);
        matrix.postTranslate(offsetX, offsetY);

        canvas.drawBitmap(mTown, matrix, null);
    }

    private void drawSun(Canvas canvas) {
		Log.v("MaxWu","SRV drawSun");
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = mPercent;
        if (dragPercent > 1.0f) { // Slow down if pulling over set height
            dragPercent = (dragPercent + 9.0f) / 10;
        }

        float sunRadius = (float) mSunSize / 2.0f;
        float sunRotateGrowth = SUN_INITIAL_ROTATE_GROWTH;

        float offsetX = mSunLeftOffset;
        float offsetY = mSunTopOffset
                + (mParent.getTotalDragDistance() / 2) * (1.0f - dragPercent) // Move the sun up
                - mTop; // Depending on Canvas position

        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
		
        if (scalePercentDelta > 0) {
			Log.e("MaxWu","SRV drawSun scalePercentDelta>0");
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            float sunScale = 1.0f - (1.0f - SUN_FINAL_SCALE) * scalePercent;
            sunRotateGrowth += (SUN_FINAL_ROTATE_GROWTH - SUN_INITIAL_ROTATE_GROWTH) * scalePercent;

            matrix.preTranslate(offsetX + (sunRadius - sunRadius * sunScale), offsetY * (2.0f - sunScale));
            matrix.preScale(sunScale, sunScale);

            offsetX += sunRadius;
            offsetY = offsetY * (2.0f - sunScale) + sunRadius * sunScale;
        } else {
			Log.e("MaxWu","SRV drawSun scalePercentDelta<0");
            matrix.postTranslate(offsetX, offsetY);
            offsetX += sunRadius;
            offsetY += sunRadius;
        }
		if(isRefreshing){
		Log.e("MaxWu","SRV drawSun sunRotateGrowth="+sunRotateGrowth);
		Log.e("MaxWu","SRV drawSun scalePercentDelta="+scalePercentDelta);
		Log.e("MaxWu","SRV drawSun offsetX="+offsetX);
		Log.e("MaxWu","SRV drawSun offsetY="+offsetY);
		Log.e("MaxWu","SRV drawSun mPercent="+mPercent);
		Log.e("MaxWu","SRV drawSun dragPercent="+dragPercent);
		}

		
        matrix.postRotate(
                (isRefreshing ? -360 : 360) * mRotate * (isRefreshing ? 1 : sunRotateGrowth),
                offsetX,
                offsetY);

        canvas.drawBitmap(mSun, matrix, null);
    }

    public void setPercent(float percent) {
		Log.v("MaxWu","SRV setPercent percent="+percent);
        mPercent = percent;
    }

    public void setRotate(float rotate) {
		Log.v("MaxWu","SRV setRotate = "+rotate);
        mRotate = rotate;
        invalidateSelf();
    }

    public void resetOriginals() {
		Log.v("MaxWu","SRV resetOriginals");
        setPercent(0);
        setRotate(0);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
		Log.v("MaxWu","SRV onBoundsChange bounds="+bounds);
        super.onBoundsChange(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
		Log.v("MaxWu","SRV setBounds left="+left+" right="+right+" top="+top+" bottom="+bottom);
        super.setBounds(left, top, right, mSkyHeight + top);
    }

    @Override
    public void setAlpha(int alpha) {
		Log.v("MaxWu","SRV setAlpha alpha="+alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
		Log.v("MaxWu","SRV setColorFilter ");
    }

    @Override
    public int getOpacity() {
		Log.v("MaxWu","SRV getOpacity ");
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isRunning() {
		Log.v("MaxWu","SRV isRunning ");
        return false;
    }

    @Override
    public void start() {
		Log.v("MaxWu","SRV start");
        mAnimation.reset();
        isRefreshing = true;
        mParent.startAnimation(mAnimation);
    }

    @Override
    public void stop() {
		Log.v("MaxWu","SRV stop");
        mParent.clearAnimation();
        isRefreshing = false;
        resetOriginals();
    }

    private void setupAnimations() {
		Log.v("MaxWu","SRV setupAnimations");
        mAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setRotate(interpolatedTime);
            }
        };
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(LINEAR_INTERPOLATOR);
        mAnimation.setDuration(ANIMATION_DURATION);
    }

}
