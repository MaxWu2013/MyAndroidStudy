package com.example.testcustomview;

import android.view.View;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;

public class CircleView extends AbstractBaseView implements View.OnClickListener{
	
	public static String tag="CircleView";
	private int defRadius = 20 ;
	private int strokeColor=0xFFFF8C00;
	private int strokeWidth= 10 ;
	
	public CircleView(Context context){
		super(context);
		initCircleView();
	}
	public CircleView(Context context , int inStrokeWidth , int inStrokeColor){
		super(context);
		strokeColor = inStrokeColor;
		strokeWidth = inStrokeWidth;
		initCircleView();
	}
	public CircleView(Context context , AttributeSet attrs){
		this(context ,attrs ,0);
	}
	
	//meant for derived classes to call
	public CircleView(Context context , AttributeSet attrs , int defStyle){
		super(context ,attrs ,defStyle);
		TypedArray t = context.obtainStyledAttributes(attrs , R.styleable.CircleView , defStyle ,0);
		strokeColor = t.getColor(R.styleable.CircleView_strokeColor ,strokeColor);
		t.recycle();
		initCircleView();
	}
	
	public void initCircleView(){
		this.setMinimumHeight(defRadius*2);
		this.setMinimumWidth(defRadius*2);
		this.setOnClickListener(this);
		this.setClickable(true);
		this.setSaveEnabled(true);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Log.d(tag, "onDraw called");
		int w = this.getWidth();
		int h = this.getHeight();
		int t = this.getTop();
		int l = this.getLeft();
		
		int ox= w/2;
		int oy = h/2;
		int rad = Math.min(ox, oy)/2 ;
		
		canvas.drawCircle(ox, oy, rad, getBrush());
	}
	
	private Paint getBrush(){
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setStrokeWidth(strokeWidth);
		p.setColor(strokeColor);
		p.setStyle(Paint.Style.STROKE);
		return p ;
	}
	
	@Override
	protected int hGetMaximumHeight(){
		return defRadius*2;
	}
	@Override
	protected int hGetMaximumWidth(){
		return defRadius*2;
	}
	
	@Override
	public void onClick(View v){
		//increase the redius
		defRadius *=1.2;
		adjustMinimumHeight();
		requestLayout();
		invalidate();
	}
	
	private void adjustMinimumHeight(){
		this.setMinimumHeight(defRadius*2);
		this.setMinimumWidth(defRadius*2);
	}
	
	/**
	 * save and restore work
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable p){
		this.onRestoreInstanceStateStandard(p);
		this.initCircleView();
	}
	@Override
	protected Parcelable onSaveInstanceState(){
		return this.onSaveInstanceStateStandard();
	}
	
	private void onRestoreInstanceStateStandard(Parcelable state){
		//if it is not yours doesn't mean it is BaseSavedState
		//You may have a parent in your hierarchy that has their own
		//state derived from BaseSaveState
		// it is like peeling an onion or a Russian doll
		if(!(state instanceof SavedState)){
			super.onRestoreInstanceState(state);
			return ;
		}
		
		//it is our state
		SavedState ss = (SavedState)state ;
		// Peel it and give the child to the super class 
		super.onRestoreInstanceState(ss.getSuperState());
		defRadius = ss.defRadius ;
	}
	
	private Parcelable onSaveInstanceStateStandard(){
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.defRadius = this.defRadius;
		return ss ;
	}
	/**
	 * Save State inner static class
	 */
	public static class SavedState extends BaseSavedState{
		int defRadius ;

		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override 
		public void writeToParcel(Parcel out , int flags){
			super.writeToParcel(out, flags);
			out.writeInt(defRadius);
		}
		//Read back the values
		private SavedState(Parcel in){
			super(in);
			defRadius = in.readInt();
		}
		@Override
		public String toString(){
			return "CircleView deRadius:"+defRadius;
		}
		@SuppressWarnings("hiding")
		public static final Parcelable.Creator<SavedState>CREATER=
					new Parcelable.Creator<SavedState>() {

						@Override
						public SavedState createFromParcel(Parcel in) {
							// TODO Auto-generated method stub
							return new SavedState(in);
						}

						@Override
						public SavedState[] newArray(int size) {
							// TODO Auto-generated method stub
							return new SavedState[size];
						}
							
					};
	}
}
