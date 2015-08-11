package com.example.testcustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public abstract class AbstractBaseView extends View{
	public static String tag="AbstractBaseView" ;

	
	public AbstractBaseView(Context context) {
		super(context);
		
	}
	
	public AbstractBaseView(Context context , AttributeSet attrs){
		super(context , attrs);
	}
	
	public AbstractBaseView(Context context , AttributeSet attrs , int defStyle){
		super(context , attrs , defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec , int heightMeasureSpec){
		logSpec(MeasureSpec.getMode(widthMeasureSpec));
		Log.d(tag, "size:"+MeasureSpec.getSize(widthMeasureSpec));
		//setMeasuredDimension 必须在 onMeasure里面调用 设置View的尺寸
		setMeasuredDimension(getImprovedDefaultWidth(widthMeasureSpec)
				, getImprovedDefaultHeight(heightMeasureSpec));
	}
	
	@Override
	protected void onSizeChanged(int w , int h , int oldw , int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onLayout(boolean changed , int left , int top , int right , int bottom){
		super.onLayout(changed, left, top, right, bottom);
		Log.d(tag , "onLayout");
		
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Log.d(tag, "onDraw called");
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable p){
		Log.d(tag ,"onRestoreInstanceState");
		super.onRestoreInstanceState(p);
	}
	
	@Override
	protected Parcelable onSaveInstanceState(){
		Log.d(tag , "onSaveinstanceState");
		Parcelable p = super.onSaveInstanceState();
		return p ;
	}
	
	private int getImprovedDefaultHeight(int measureSpec){
		//int result = size ;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		
		switch(specMode){
		case MeasureSpec.UNSPECIFIED:
			return hGetMaximumHeight();
		case MeasureSpec.EXACTLY:
			return specSize;
		case MeasureSpec.AT_MOST:
			return hGetMinimumHeight();
		}
		//you shouldn't come here
		Log.e(tag, "unknown specmode");
		return specSize ;
	}
	
	private int getImprovedDefaultWidth(int measureSpec){
		//int result = size ;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		switch(specMode){
		case MeasureSpec.UNSPECIFIED:
			return hGetMaximumWidth();
		case MeasureSpec.EXACTLY:
			return specSize;
		case MeasureSpec.AT_MOST:
			return hGetMinimumWidth();
		}
		//you shouldn't come here
		Log.e(tag, "unknown specmode");
		return specSize ;
	}
	
	private void logSpec(int specMode){
		if(specMode==MeasureSpec.UNSPECIFIED){
			Log.d(tag, "mode:unspecified");
			return ;
		}
		if(specMode==MeasureSpec.EXACTLY){
			Log.d(tag , "mode:exact");
			return;
		}
		if(specMode==MeasureSpec.AT_MOST){
			Log.d(tag , "mode:at_most");
			return ;
		}
	}
	
	//Override these methods to provide a maximum size
	//"h" stands for hook pattern
	abstract protected int hGetMaximumHeight();
	abstract protected int hGetMaximumWidth();
	// For minimum height use the View's methods
	protected int hGetMinimumHeight(){
		return this.getSuggestedMinimumHeight();
	}
	protected int hGetMinimumWidth(){
		return this.getSuggestedMinimumWidth();
	}
}
