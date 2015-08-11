package com.example.testcomputeoffset;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;


public class CustomView extends ImageView{
	
	private int int_reverse = 0;
	
	public CustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}


	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public CustomView(Context context) {
		super(context);
		initView(context);
	}
	
	private Scroller mScroller ;
	
	private int length = 0 ;
	
	private void initView(Context context){
		mScroller = new Scroller(context , new DecelerateInterpolator());
	}
	
	public void setScrollLength(int length){
		this.length = length ;
	}
	
	public void startAnimation(){
		Log.i("Max", "startAnimation()");
		if(!mScroller.computeScrollOffset()){
			Log.i("Max", "startAnimation()  start");
			Log.i("Max", "startAnimation()  length="+length);
			mScroller.startScroll(0, 0, length, 0 , 3000);
			postInvalidate();
			int_reverse = 1;
		}
	}
	
	public void reverseAnimation(){
		Log.i("Max", "reverseAnimation()");
		if(!mScroller.computeScrollOffset()){
			Log.i("Max", "reverseAnimation()  start");
			Log.i("Max", "reverseAnimation() -length="+(-length));
			mScroller.startScroll(length, 0, -length, 0 , 3000);
			postInvalidate();
			int_reverse = -1;
		}
	}
	
	
	@Override
	public void computeScroll() {
		Log.i("Max", "computeScroll()");
		if(mScroller.computeScrollOffset()){
			int x = mScroller.getCurrX() ;
			int y = 0 ;
			scrollTo(x, y);
			Log.i("Max","scroll To  x="+x+" y="+y);
			postInvalidate();
		}else{
			Log.i("Max","mScroller.computeScrollOffset=== "+mScroller.computeScrollOffset());
			Log.i("Max"," reverse = "+int_reverse);
			if(int_reverse==1){
				reverseAnimation();
			}
		}
		super.computeScroll();
	}

}
