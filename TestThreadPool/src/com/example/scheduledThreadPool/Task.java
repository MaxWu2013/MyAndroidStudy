package com.example.scheduledThreadPool;

import java.util.concurrent.TimeUnit;

import android.util.Log;

public class Task implements Runnable{

	@Override
	public void run() {
		Log.d("Task", " run begin");
		
		try{
			TimeUnit.SECONDS.sleep(2);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		Log.d("Task "," run end");
		
	}

}
