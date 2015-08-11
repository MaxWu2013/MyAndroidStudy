package com.example.safetask.localvariable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class UnsafeTask implements Runnable{

	private Date startDate ;
	
	@Override
	public void run() {
		startDate = new Date();
		Log.i("Max", "starting thread ="+Thread.currentThread().getId()+"  date="+startDate);
		
		try{
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random()*10));
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		Log.i("Max", "thread finished ="+Thread.currentThread().getId()+" date="+startDate);
	}

}
