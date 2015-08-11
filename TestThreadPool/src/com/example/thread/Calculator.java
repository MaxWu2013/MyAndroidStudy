package com.example.thread;

import android.util.Log;

public class Calculator implements Runnable{
	private static final String TAG = "Max";
	private int number;
	public Calculator(int number){
		this.number = number;
	}
	
	@Override
	public void run() {
		for(int i=1 ; i<=10 ; i++){
			Log.e(TAG, "threadName="+Thread.currentThread().getName());
			Log.e(TAG, "number="+number);
			Log.e(TAG, "i="+i);
			Log.e(TAG, "i*number="+i*number);
		}
		
	}

}
