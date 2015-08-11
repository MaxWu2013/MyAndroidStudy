package com.example.safetask.localvariable;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;

public class TestSafeTaskActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		testUnsafeThread();
		testSafeThread();
	}
	
	private void testUnsafeThread(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				
				testUnsafeTask();
				
			}
			
		}).start();
	}
	
	
	private void testUnsafeTask(){
		
		UnsafeTask task = new UnsafeTask();
		//����10�����̣�ÿ�����̿���ʱ���ֲ���ӡ����ʱ�䣬Ȼ������2�룬Ȼ���ӡʱ�俴��ʱ���Ƿ�ı䡣 ----�����֤��ʱ�䱻���������޸���
		for(int i=0 ; i<10 ; i++){
			Thread thread = new Thread(task);
			thread.start();
			
			try{
				TimeUnit.SECONDS.sleep(2);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	
	private void testSafeThread(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				
				testSafeTask();
				
			}
			
		}).start();
	}
	
	private void testSafeTask(){
		SafeTask task = new SafeTask();
		//����10�����̣�ÿ�����̿���ʱ���ֲ���ӡ����ʱ�䣬Ȼ������2�룬Ȼ���ӡʱ�俴��ʱ���Ƿ�ı䡣 ----�����֤��ʱ��û�����������޸ĵ�
		for(int i=0; i<10 ; i++){
			Thread thread = new Thread(task);
			thread.start();
			
			try{
				TimeUnit.SECONDS.sleep(2);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
