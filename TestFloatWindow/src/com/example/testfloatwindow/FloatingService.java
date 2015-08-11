package com.example.testfloatwindow;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
/** 
 * 悬浮窗Service 该服务会在后台一直运行一个悬浮的透明的窗体。 
 *  
 * @author 
 *  
 */  
public class FloatingService extends Service{

	private int statusBarHeight ; //状态栏高度
	private View floatingView ; // 透明悬浮框
	private boolean viewAdded = false;
	private WindowManager windowManager ;
	private WindowManager.LayoutParams layoutParams;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		floatingView = LayoutInflater.from(this).inflate(R.layout.floating ,  null);
		floatingView.setSaveEnabled(true);
		windowManager =(WindowManager)this.getSystemService(WINDOW_SERVICE);
        /* 
         * LayoutParams.TYPE_SYSTEM_ERROR：保证该悬浮窗所有View的最上层 
         * LayoutParams.FLAG_NOT_FOCUSABLE:该浮动窗不会获得焦点，但可以获得拖动 
         * PixelFormat.TRANSPARENT：悬浮窗透明 
         */  
		layoutParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSPARENT);
		// layoutParams.gravity = Gravity.RIGHT|Gravity.BOTTOM; //悬浮窗开始在右下角显示  
		layoutParams.gravity=Gravity.LEFT | Gravity.TOP;
		
		floatingView.setOnTouchListener(new View.OnTouchListener() {
			float[] temp = new float[] {0f , 0f};
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				layoutParams.gravity=Gravity.LEFT|Gravity.TOP;
				int eventaction = event.getAction();
				switch(eventaction){
				case MotionEvent.ACTION_DOWN:// 按下事件，记录按下时手指在悬浮窗的XY坐标值  
					temp[0]=event.getX();
					temp[1]=event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					refreshView((int)(event.getRawX()-temp[0]),(int)(event.getRawY()-temp[1]));
					break;
				}
				
				return true;
			}
		});
	}
    /** 
     * 刷新悬浮窗 
     *  
     * @param x 
     *            拖动后的X轴坐标 
     * @param y 
     *            拖动后的Y轴坐标 
     */  
	public void refreshView(int x , int y){
		//状态栏高度不能立即取，不然得到的值是0  
		if(statusBarHeight==0){
			View rootView = floatingView.getRootView();
			Rect r=new Rect();
			rootView.getWindowVisibleDisplayFrame(r);
			statusBarHeight=r.top;
		}
		
		layoutParams.x = x ;
		// y轴减去状态栏的高度，因为状态栏不是用户可以绘制的区域，不然拖动的时候会有跳动 
		layoutParams.y=y-statusBarHeight;
		
		refresh();
	}
    /** 
     * 添加悬浮窗或者更新悬浮窗 如果悬浮窗还没添加则添加 如果已经添加则更新其位置 
     */  
	public void refresh(){
		if(viewAdded){
			windowManager.updateViewLayout(floatingView, layoutParams);
		}else{
			windowManager.addView(floatingView, layoutParams);
			viewAdded=true;
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		refresh();
	}
    /** 
     * 关闭悬浮窗 
     */ 
	public void removeView(){
		if(viewAdded){
			windowManager.removeView(floatingView);
			viewAdded= false;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		removeView();
	}
	
	class StatusBarReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
