package com.example.testview;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

public class HorizontialListView extends AdapterView<ListAdapter> {

	public boolean mAlwaysOverrideTouch = true;
	protected ListAdapter mAdapter;
	private int mLeftViewIndex = -1;
	private int mRightViewIndex = 0;
	protected int mCurrentX;
	protected int mNextX;
	private int mMaxX = Integer.MAX_VALUE;
	private int mMinX = 0;
	private int mDisplayOffset = 0;
	private int itemNum = 0;
	protected Scroller mScroller;
	private GestureDetector mGesture;
	private Queue<View> mRemovedViewQueue = new LinkedList<View>();
	private OnItemSelectedListener mOnItemSelected;
	private OnItemClickListener mOnItemClicked;
	private boolean mDataChanged = false;
	private boolean circledisplay = false;
	
	private boolean BIAO_ZHI_WEI = false;
	
	public HorizontialListView(Context context) {
		super(context);
		initView();
	}
	
	public HorizontialListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public HorizontialListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	
	public void setCircleDisplay(boolean circle){
		circledisplay = circle;
		if(circledisplay){
			mMinX = Integer.MIN_VALUE;
		}
	}
	public void setDisplayItemNum(int num){
		itemNum = num;
		requestLayout();
	}
	
	public int getDisplayItemNum(){
		return itemNum;
	}
	private synchronized void initView() {
		mLeftViewIndex = -1;
		mRightViewIndex = 0;
		mDisplayOffset = 0;
		mCurrentX = 0;
		mNextX = 0;
		mMaxX = Integer.MAX_VALUE;
		mScroller = new Scroller(getContext());
		mGesture = new GestureDetector(getContext(), mOnGesture);
	}
	private DataSetObserver mDataSetObserver = new DataSetObserver() {

		@Override
		public void onChanged() {
			synchronized(HorizontialListView.this){
				mDataChanged = true;
				invalidate();
				requestLayout();
			}
		}

		@Override
		public void onInvalidated() {
			reset();
			invalidate();
			requestLayout();
		}
		
	};
	@Override
	public ListAdapter getAdapter() {
		return mAdapter;
	}

	@Override
	public View getSelectedView() {
		return null;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if(mAdapter !=null){
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
		}
		mAdapter = adapter;
		mAdapter.registerDataSetObserver(mDataSetObserver);
		reset();
	}
	public synchronized void reset(){
		initView();
		removeAllViewsInLayout();
		requestLayout();
	}
	
	public boolean IsAnimation(){
		return !mScroller.isFinished();
	}
	@Override
	public void setSelection(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClicked = listener ;
	}

	@Override
	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		mOnItemSelected = listener ;
	}
	
	
	private void addAndMeasureChild(final View child, int viewPos) {
		LayoutParams params = child.getLayoutParams();
		if(params == null) {
			params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		}
		int itemWidth = getWidth()/itemNum ;
		
		addViewInLayout(child, viewPos, params, true);
		child.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
	}
	
	@Override
	protected synchronized void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if(mAdapter == null){
			return;
		}
		
		if(mDataChanged){
			int oldCurrentX = mCurrentX;
			initView();
			removeAllViewsInLayout();
			mNextX = oldCurrentX;
			mDataChanged = false;
		}

		if(mScroller.computeScrollOffset()){
			int scrollX = mScroller.getCurrX();
			mNextX = scrollX;
		}
		
		if(mNextX < mMinX){
			mNextX = mMinX;
			mScroller.forceFinished(true);
		}
		if(mNextX > mMaxX) {
			mNextX = mMaxX;
			mScroller.forceFinished(true);
		}
		
		int dx = mCurrentX - mNextX;
		
		removeNonVisibleItems(dx);
		fillList(dx);
		positionItems(dx);
		
		mCurrentX = mNextX;
		
		if(!mScroller.isFinished()){
			post(new Runnable(){
				@Override
				public void run() {
					requestLayout();
				}
			});
			
		}else{
			/*
			 * *******************************************************************
			 */
			//perform onItemSelectedListerner 
			int averageLine = getWidth()/2;
			int displayoffset_left = mDisplayOffset;
			int position = mLeftViewIndex;
			int childnum = getAdapter().getCount();
			if(circledisplay){
				while(position < 0){
					position += childnum;
				}
			}
			for(int i=0;i<getChildCount();i++){
				View child = getChildAt(i);
				int childWidth = child.getMeasuredWidth();
				int childXcenter = displayoffset_left + childWidth/2;
				if(Math.abs(childXcenter -averageLine) ==0){
					if(mOnItemSelected != null){
						mOnItemSelected.onItemSelected(HorizontialListView.this, child, (position + 1 + i)%childnum, mAdapter.getItemId( (position + 1 + i)%childnum ));
					}
				}
	
				displayoffset_left += childWidth;
			}
			/*
			 * ************************************************************************
			 */
		}
	}
	
	private void fillList(final int dx) {
		int edge = 0;
		View child = getChildAt(getChildCount()-1);
		if(child != null) {
			edge = child.getRight();
		}
		fillListRight(edge, dx);
		
		edge = 0;
		child = getChildAt(0);
		if(child != null) {
			edge = child.getLeft();
		}
		fillListLeft(edge, dx);
		
		
	}
	
	private void fillListRight(int rightEdge, final int dx) {
		if(circledisplay){
			
			while(rightEdge+dx < getWidth()){
				int childnum = mAdapter.getCount();
				int position = mRightViewIndex;
				while(position < 0){
					position += childnum ;
				}				
			
			View child = mAdapter.getView(position%childnum, mRemovedViewQueue.poll(), this);
			addAndMeasureChild(child, -1);
			rightEdge += child.getMeasuredWidth();
			mRightViewIndex++;
			}
			
		}else{
		
			while(rightEdge + dx < getWidth() && mRightViewIndex < mAdapter.getCount()){
				View child = mAdapter.getView(mRightViewIndex, mRemovedViewQueue.poll(), this);
				addAndMeasureChild(child, -1);
				rightEdge += child.getMeasuredWidth();	
			if(mRightViewIndex == mAdapter.getCount()-1){
				mMaxX = mCurrentX + rightEdge - getWidth();
			}
			mRightViewIndex++;
		}
	}
		
		
	}
	
	private void fillListLeft(int leftEdge, final int dx) {
		if(circledisplay){
			while(leftEdge + dx > 0 ){
				int childnum = mAdapter.getCount();
				int position = mLeftViewIndex;
				while(position < 0){
					position += childnum;
				}
				
				View child = mAdapter.getView(position%childnum, mRemovedViewQueue.poll(), this);
				addAndMeasureChild(child, 0);
				leftEdge -= child.getMeasuredWidth();
				
				mLeftViewIndex--;
				mDisplayOffset -= child.getMeasuredWidth();
			}
		}else{
			while(leftEdge + dx > 0 && mLeftViewIndex >= 0) {
			View child = mAdapter.getView(mLeftViewIndex, mRemovedViewQueue.poll(), this);
			addAndMeasureChild(child, 0);
			leftEdge -= child.getMeasuredWidth();
			
			
			mLeftViewIndex--;
			mDisplayOffset -= child.getMeasuredWidth();
			}
		}
		
	}
	
	private void removeNonVisibleItems(final int dx) {
		View child = getChildAt(0);
		while(child != null && child.getRight() + dx <= 0) {
			mDisplayOffset += child.getMeasuredWidth();
			mRemovedViewQueue.offer(child);
			removeViewInLayout(child);
			mLeftViewIndex++;
			child = getChildAt(0);
			
		}
		
		child = getChildAt(getChildCount()-1);
		while(child != null && child.getLeft() + dx >= getWidth()) {
			mRemovedViewQueue.offer(child);
			removeViewInLayout(child);
			mRightViewIndex--;
			child = getChildAt(getChildCount()-1);
		}
	}
	
	private void positionItems(final int dx) {
		if(getChildCount() > 0){
			int averageLine = getWidth()/2 ;
			mDisplayOffset += dx;
			int left = mDisplayOffset;
			for(int i=0;i<getChildCount();i++){
				View child = getChildAt(i);
				int childWidth = child.getMeasuredWidth();
				int childXcenter = left + childWidth/2;
				int padding_width = Math.abs(childXcenter - averageLine);
				float scale = 0.0f;
			//	if(padding_width < childWidth/2){
			//		scale = padding_height == 0 ? 0.5f : 0.4f ;
			//	}else{
			//		scale = 0.2f;
			//	}
				
			//	if(child instanceof TwoImageView){
					//((TwoImageView) child).setBitmapsAlpha(255-Math.round(255*padding_height/childHeight));
					//((TwoImageView) child).setBitmapScale(scale);
			//	}
				
			//	if(child instanceof FourImageView){
			//		((FourImageView) child).setBitmapScale(scale);
			//	}				
				
				child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
				left += childWidth;
			}
		}
	}
	
	public boolean scrollToNext(){
		if(IsAnimation()) return false;
		int itemWidth = getWidth()/itemNum ;
		scrollTo(itemWidth);
		return true;
	}
	public boolean scrollToNext(int index){
		if(IsAnimation()) return false;
		int itemWidth = getWidth()/itemNum;
		int distance = Math.abs(index)*itemWidth;
		scrollTo(distance);
		return true;
	}
	public boolean scrollToPrevious(){
		if(IsAnimation()) return false;
		int itemWidth = getWidth()/itemNum;
		scrollTo(-itemWidth);
		return true;
	}
	public boolean scrollToPrevious(int index){
		if(IsAnimation()) return false;
		int itemWidth = getWidth()/itemNum;
		int distance = Math.abs(index)*itemWidth;
		scrollTo(-distance);
		return true;
	}
	/*
	 * param distance: positive value will scroll content up
	 */
	public synchronized void scrollTo(int distance) {
		//Log.i("HorizontialListLayout this = ", "this " + this);
		//Log.i("scrollTo","mCurrentX  ="+ mCurrentX + "distance = " + distance);
		mScroller.startScroll(mCurrentX, 0, distance , 0);
		requestLayout();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean handled = mGesture.onTouchEvent(ev);
		if(ev.getAction() == MotionEvent.ACTION_UP ){
			if(BIAO_ZHI_WEI){
				BIAO_ZHI_WEI = false;
			}else{
				//start animation to scroll to the center
				//mDisplayOffset is aways negative
				int itemWidth = getWidth()/itemNum;
				int distance = Math.abs(mDisplayOffset) - itemWidth/2 ;
				if(distance > 0){
					distance = itemWidth + mDisplayOffset ;
				}else{
					distance = mDisplayOffset ;
				}
				scrollTo(distance);
			}
		}
		return handled;
	}
	
	protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
		synchronized(HorizontialListView.this){
			/*
			 * <mScroller.fling() > the scroll distance is caculate in its method, we are hard to control it
			 *  So, i use <mScroller.scroll()> instead, we caculate the distance ourselves with the velocity value.
			 */
			///drop it
			//mScroller.fling(mNextX, 0, (int)-velocityX, 0, 0, mMaxX, 0, 0);


			/*
			 *  the distance must be special , so the center item can be exactlty at the center of layout
			 *  first : caculate the current offset and scroll direction
			 *  second : caculate the distancet to scroll 
			 *  
			 */
			int itemWidth = getWidth()/itemNum;
			int distance = 0;
			/*velocityX > 0 : direction is right
			 * velocityX < 0 : direction is left
			 */
			 
			 if(velocityX >0){
			 	distance = mDisplayOffset ;
			 }else{
			 	distance = itemWidth + mDisplayOffset ;
			}
			// velocityX 's direction is opposite to the distance used in  <scrollTo(distance)>
			//distance += (-1)*Math.round(velocityX/100)*itemWidth;
			scrollTo(distance);
		
		}
		return true;
	}
	
	protected boolean onDown(MotionEvent e) {
		mScroller.forceFinished(true);
		return true;
	}
	
	private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			return HorizontialListView.this.onDown(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			BIAO_ZHI_WEI = true ;
			return HorizontialListView.this.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			
			synchronized(HorizontialListView.this){
				mNextX += (int)distanceX;
			}
			requestLayout();
			
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
		
		BIAO_ZHI_WEI = true;
		
		boolean handled = false;
			/*MaxWU
			 * first , find the item you tap
			 * second , let the selected item scroll to the center
			 */
			Rect viewRect = new Rect();
			//param tapItemXCenter : the  child's X value which is relative to its parent 
			int tapItemXCenter = -1;
			for(int i=0;i<getChildCount();i++){
				View child = getChildAt(i);
				int left = child.getLeft();
				int right = child.getRight();
				int top = child.getTop();
				int bottom = child.getBottom();
				viewRect.set(left, top, right, bottom);
				if(viewRect.contains((int)e.getX(), (int)e.getY())){
					tapItemXCenter = left+(right - left)/2;
					if(mOnItemClicked != null){
						mOnItemClicked.onItemClick(HorizontialListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId( mLeftViewIndex + 1 + i ));
					}
					/*
					if(mOnItemSelected != null){
						mOnItemSelected.onItemSelected(HorizontialListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId( mLeftViewIndex + 1 + i ));
					}
					*/
					break;
				}
				
			}
			if( tapItemXCenter < 0){
				handled = false;
			}
			int averageLine = getWidth()/2 ;
			//start animation to scroll the tap item to the center
			// distance is positive  will scroll the content up
			int distance = tapItemXCenter - averageLine;
			scrollTo(distance);
			handled = true;
			if(handled){
			return true;
			}
			return super.onSingleTapUp(e);
		}
		
	};
}
