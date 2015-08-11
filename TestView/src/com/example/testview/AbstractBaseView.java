package com.example.testview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public abstract class AbstractBaseView extends View {

	public AbstractBaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getImprovedDefaultWidth(widthMeasureSpec),
				getImprovedDefaultHeight(heightMeasureSpec));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable p) {
		super.onRestoreInstanceState(p);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable p = super.onSaveInstanceState();
		return p;
	}

	private int getImprovedDefaultHeight(int measureSpec) {
		// int result = size ;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		switch (specMode) {
		case MeasureSpec.UNSPECIFIED:
			return hGetMaximumHeight();
		case MeasureSpec.EXACTLY:
			return specSize;
		case MeasureSpec.AT_MOST:
			return hGetMinimumHeight();
		}
		return specSize;
	}

	private int getImprovedDefaultWidth(int measureSpec) {
		// int result = size ;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		switch (specMode) {
		case MeasureSpec.UNSPECIFIED:
			return hGetMaximumWidth();
		case MeasureSpec.EXACTLY:
			return specSize;
		case MeasureSpec.AT_MOST:
			return hGetMinimumWidth();
		}
		return specSize;
	}

	abstract protected int hGetMaximumHeight();

	abstract protected int hGetMaximumWidth();

	protected int hGetMinimumHeight() {
		return this.getSuggestedMinimumHeight();
	}

	protected int hGetMinimumWidth() {
		return this.getSuggestedMinimumWidth();
	}
}
