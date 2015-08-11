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
 * ������Service �÷�����ں�̨һֱ����һ��������͸���Ĵ��塣 
 *  
 * @author 
 *  
 */  
public class FloatingService extends Service{

	private int statusBarHeight ; //״̬���߶�
	private View floatingView ; // ͸��������
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
         * LayoutParams.TYPE_SYSTEM_ERROR����֤������������View�����ϲ� 
         * LayoutParams.FLAG_NOT_FOCUSABLE:�ø����������ý��㣬�����Ի���϶� 
         * PixelFormat.TRANSPARENT��������͸�� 
         */  
		layoutParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSPARENT);
		// layoutParams.gravity = Gravity.RIGHT|Gravity.BOTTOM; //��������ʼ�����½���ʾ  
		layoutParams.gravity=Gravity.LEFT | Gravity.TOP;
		
		floatingView.setOnTouchListener(new View.OnTouchListener() {
			float[] temp = new float[] {0f , 0f};
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				layoutParams.gravity=Gravity.LEFT|Gravity.TOP;
				int eventaction = event.getAction();
				switch(eventaction){
				case MotionEvent.ACTION_DOWN:// �����¼�����¼����ʱ��ָ����������XY����ֵ  
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
     * ˢ�������� 
     *  
     * @param x 
     *            �϶����X������ 
     * @param y 
     *            �϶����Y������ 
     */  
	public void refreshView(int x , int y){
		//״̬���߶Ȳ�������ȡ����Ȼ�õ���ֵ��0  
		if(statusBarHeight==0){
			View rootView = floatingView.getRootView();
			Rect r=new Rect();
			rootView.getWindowVisibleDisplayFrame(r);
			statusBarHeight=r.top;
		}
		
		layoutParams.x = x ;
		// y���ȥ״̬���ĸ߶ȣ���Ϊ״̬�������û����Ի��Ƶ����򣬲�Ȼ�϶���ʱ��������� 
		layoutParams.y=y-statusBarHeight;
		
		refresh();
	}
    /** 
     * ������������߸��������� �����������û�������� ����Ѿ�����������λ�� 
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
     * �ر������� 
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
