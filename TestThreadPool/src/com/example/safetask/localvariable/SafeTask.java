package com.example.safetask.localvariable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class SafeTask implements Runnable{

	private static ThreadLocal<Date> startDate =new ThreadLocal<Date>(){
		@Override
		protected Date initialValue() {
			// TODO Auto-generated method stub
			return new Date();
		}
		
	};
	
	@Override
	public void run() {
		Log.i("Max", "starting thread ="+Thread.currentThread().getId()+"  date="+startDate.get());
		
		try{
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random()*10));
		}catch(InterruptedException e){
			e.printStackTrace();
		}

		Log.i("Max", "thread finished ="+Thread.currentThread().getId()+" date="+startDate.get());
	}

}
