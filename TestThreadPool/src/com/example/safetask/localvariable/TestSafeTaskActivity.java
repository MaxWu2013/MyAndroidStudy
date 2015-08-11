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
		//开启10个进程，每个进程开启时保持并打印开启时间，然后休眠2秒，然后打印时间看看时间是否改变。 ----》结果证明时间被其他进程修改了
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
		//开启10个进程，每个进程开启时保持并打印开启时间，然后休眠2秒，然后打印时间看看时间是否改变。 ----》结果证明时间没被其他进程修改到
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
