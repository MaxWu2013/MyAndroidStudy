package com.example.threadgroup;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestThreadGroupActivity extends Activity{
	
	private ThreadGroup threadGroup = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		threadGroup = new ThreadGroup("srearcher");
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				TestThreadGroup();
			}
			
		}).start();
	}
	
	private void TestThreadGroup(){
		Result result =new Result();
		
		SearchTask searchTask = new SearchTask(result);
		
		for(int i=0 ; i< 5 ;i++){
			Thread thread = new Thread(threadGroup , searchTask);
			thread.start();
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		Log.i("Max", "Number of Threads active count="+threadGroup.activeCount());
		
		threadGroup.list();
		
		Thread[] threads = new Thread[threadGroup.activeCount()];
		
		//MaxWu : enumerate��Thread[] list �� �ǰ�  threadgroup ���� active ״̬���̷߳Ž�    list�б�����
		threadGroup.enumerate(threads);
		for(int i=0 ; i<threadGroup.activeCount(); i++){
			Log.i("Max", "Thread name="+threads[i].getName()+" status="+threads[i].getState());
		}
		
		waitFinish(threadGroup);
		
		//һ���Բ����� �߳���ֱ���ж������߳�
		threadGroup.interrupt();
	}
	
	private void waitFinish(ThreadGroup threadGroup){
		while(threadGroup.activeCount()>9){
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
