package com.example.threadgroup;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class SearchTask implements Runnable{

	private Result result ;
	
	public SearchTask(Result result){
		this.result = result ;
	}
	
	@Override
	public void run() {
		
		String name = Thread.currentThread().getName();
		
		try{
			doTask();
			result.setName(name);
		}catch(InterruptedException e){
			Log.i("Max", "Thread = "+name+" Interrupted ");
			return ;
		}
		
		Log.i("Max", "Thread ="+name+"  success End ");
	}

	
	private void doTask() throws InterruptedException{
		Random random = new Random((new Date()).getTime());
		
		int value = (int)(random.nextDouble()*100);
		
		Log.i("Max", "Thread ="+Thread.currentThread().getName()+"  value="+value);
		
		TimeUnit.SECONDS.sleep(value);
	}
}
