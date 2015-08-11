package com.example.testview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.util.Log;

public class PercentCircle extends AbstractBaseView{
	
	private int mRadius = 20 ;//
	private int mRingWidth = 2 ;//
	private int mRingColor = 0xffff0000 ;
	private int mPercent = 50;
	
	private Paint mPaint ;
	private Paint mPaintUp ;

	private PorterDuffColorFilter mFilter ;
	
	private RectF mRectF ;

	public PercentCircle(Context context) {
		this(context , null);
	}
	
	public PercentCircle(Context context, AttributeSet attrs) {
		this(context, attrs , 0);
	}

	public PercentCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if( null != attrs){
			TypedArray t = context.obtainStyledAttributes(attrs , R.styleable.PercentCircle , defStyle ,0);
			int n =t.getIndexCount();
			for(int i=0 ; i< n ; i++){
				int attr = t.getIndex(i);
				switch(attr){
				case R.styleable.PercentCircle_percent:
					mPercent = t.getInteger(attr, mPercent);
					break;
				case R.styleable.PercentCircle_radius:
					mRadius = t.getDimensionPixelSize(attr, 100);
					break;
				case R.styleable.PercentCircle_ring_color:
					mRingColor = t.getColor(attr, mRingColor);
					break;
				case R.styleable.PercentCircle_ring_width:
					mRingWidth = t.getDimensionPixelSize(attr, mRingWidth);
					break;
				}
			}
			t.recycle();
		}
		initPercentCircle();
	}
	
	private void initPercentCircle(){
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0x000000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(mRingWidth);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.BUTT);
		
		mFilter = new PorterDuffColorFilter(0xf8f8ff, PorterDuff.Mode.LIGHTEN);
		mPaint.setColorFilter(mFilter);
		mPaint.setAlpha(100);
		
		mPaintUp = new Paint();
		mPaintUp.setAntiAlias(true);
		mPaintUp.setColor(mRingColor);
		mPaintUp.setStyle(Paint.Style.STROKE);
		mPaintUp.setStrokeWidth(mRingWidth);
		mPaintUp.setStrokeJoin(Paint.Join.ROUND);
		mPaintUp.setStrokeCap(Paint.Cap.BUTT);
		mPaintUp.setAlpha(255);
		

	}
	
	private void checkPercent(){
		mPercent = Math.min(100, mPercent);
		mPercent = Math.max(0, mPercent);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(mRadius+getPaddingLeft()+ getPaddingRight()< w){
			w = mRadius+getPaddingLeft()+ getPaddingRight() ;
		}
		if(mRadius+getPaddingTop()+getPaddingBottom()< h){
			h= mRadius+getPaddingTop()+getPaddingBottom();
		}
		mRectF = new RectF(getPaddingLeft()+(int)Math.ceil(mRingWidth/2f), getPaddingTop()+(int)Math.ceil(mRingWidth/2f), w-getPaddingRight()-(int)Math.ceil(mRingWidth/2f), h-getPaddingBottom()-(int)Math.ceil(mRingWidth/2f));
		
	}
	
	public void setRingColor(int color){
		mRingColor = color ;
		mPaintUp.setColor(mRingColor);
		invalidate();
	}
	
	public void setRingWidth(int width){
		mRingWidth = width ;
		mPaint.setStrokeWidth(mRingWidth);
		mPaintUp.setStrokeWidth(mRingWidth);
		invalidate();
	}
	
	public boolean  setRadius(int radius){
		if(radius <0 ){
			return false;
		}
		mRadius = radius ;
		return true;
	}
	
	@Override
	protected int hGetMinimumHeight() {
		int h =  super.hGetMinimumHeight();
		return Math.max(h, mRadius);
	}
	
	@Override
	protected int hGetMinimumWidth() {
		int w = super.hGetMinimumWidth();
		return Math.max(w, mRadius);
	}
	
	@Override
	protected int hGetMaximumHeight() {
		// TODO Auto-generated method stub
		return mRadius+getPaddingLeft()+getPaddingRight()+2*(int)Math.ceil(mRingWidth/2f);
	}

	@Override
	protected int hGetMaximumWidth() {
		// TODO Auto-generated method stub
		return mRadius+getPaddingTop()+getPaddingBottom()+2*(int)Math.ceil(mRingWidth/2f);
	}
	
	private int getAngle(){
		return (int) (mPercent*0.01*360);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		checkPercent();
		canvas.drawArc(mRectF, 270+getAngle(), 360, false, mPaint);
		canvas.drawArc(mRectF, 270, getAngle(), false, mPaintUp);
		Log.e("Max", "mRadius="+mRadius);
		Log.e("Max", "mPercent="+mPercent);
		Log.e("Max", "mRingColor="+mRingColor);
		Log.e("Max", "mRingWidth="+mRingWidth);
	}

}
