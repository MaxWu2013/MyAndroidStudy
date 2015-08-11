package com.example.testview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PercentCircleWidget extends RelativeLayout{
	
	private PercentCircle mPercentCircle ;
	private LinearLayout mLinearLayout ;
	private TextView mTvPercent ;
	private TextView mTvOther ;
	
	private int mRadius =50;
	private int mRingColor =0xff00ff00;
	private int mRingWidth = 4;
	private int mTextColor =0xffff0000;
	private int mTextSize =20;
	private int mPercent =50;
	
	
	public PercentCircleWidget(Context context, AttributeSet attrs) {
		this(context, attrs ,0);
	}
	
	public PercentCircleWidget(Context context) {
		this(context, null);
	}
	public PercentCircleWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		if(null != attrs){
			TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.PercentCircleWidget, defStyle, 0);
			
			int n = t.getIndexCount();
			
			for(int i=0 ; i<n ; i++){
				int attr = t.getIndex(i);
				switch(attr){
				case R.styleable.PercentCircleWidget_bg_radius:
					mRadius = t.getDimensionPixelSize(attr, mRadius);
					break;
				case R.styleable.PercentCircleWidget_bg_ring_color:
					mRingColor=t.getColor(attr, mRingColor);
					break;
				case R.styleable.PercentCircleWidget_bg_ring_width:
					mRingWidth=t.getDimensionPixelSize(attr, mRingWidth);
					break;
				case R.styleable.PercentCircleWidget_text_color:
					mTextColor=t.getColor(attr, mTextColor);
					break;
				case R.styleable.PercentCircleWidget_text_percent:
					mPercent = t.getInteger(attr, mPercent);
					break;
				case R.styleable.PercentCircleWidget_text_size:
					mTextSize=t.getDimensionPixelSize(attr, mTextSize);
					break;
				}
			}
			t.recycle();
		}
		
		initPercentCircleWidget(context);
	}
	
	private void initPercentCircleWidget(Context context){
		mPercentCircle = new PercentCircle(context);
		mLinearLayout = new LinearLayout(context);
		mTvPercent = new TextView(context);
		mTvOther = new TextView(context);
		
		mPercentCircle.setRadius(mRadius);
		mPercentCircle.setRingColor(mRingColor);
		mPercentCircle.setRingWidth(mRingWidth);
		RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(CENTER_IN_PARENT, 1);
		addView(mPercentCircle, lp);
		
		mTvPercent.setTextSize(mTextSize);
		mTvPercent.setTextColor(mTextColor);
		mTvPercent.setText(mPercent+"");
		mTvOther.setTextSize(mTextSize/2);
		mTvOther.setText("%");
		mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		mLinearLayout.addView(mTvPercent , new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mLinearLayout.addView(mTvOther, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(CENTER_IN_PARENT, 1);
		addView(mLinearLayout , lp);
		
	}

	public void setRadius(int radius){
		mRadius = radius;
		mPercentCircle.setRadius(mRadius);
		requestLayout();
		invalidate();
	}
	
	public void setRingColor(int ringColor){
		mRingColor = ringColor ;
		mPercentCircle.setRingColor(ringColor);
		invalidate();
	}
	
	public void setRingWidth(int ringWidth){
		mRingWidth = ringWidth ;
		mPercentCircle.setRingWidth(mRingWidth);
		requestLayout();
		invalidate();
	}
	
	public void setTextColor(int textColor){
		mTextColor = textColor; 
		mTvPercent.setTextColor(textColor);
	}
	
	public boolean setPercent(int percent){
		if(percent<0 || percent>100){
			return false;
		}
		mPercent = percent ;
		mTvPercent.setText(percent+"");
		return true;
	}
	
	public void setTextSize(int textSize){
		mTextSize = textSize ;
		mTvPercent.setTextSize(textSize);
	}

	public int getRadius() {
		return mRadius;
	}
	public int getRingColor() {
		return mRingColor;
	}
	public int getRingWidth() {
		return mRingWidth;
	}
	public int getTextColor() {
		return mTextColor;
	}

	public int getTextSize() {
		return mTextSize;
	}

	public int getPercent() {
		return mPercent;
	}
	
	
}
