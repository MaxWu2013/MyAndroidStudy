package com.example.testthreadpool;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class SleepTwoSecondsTask implements Callable<String>{

	@Override
	public String call() throws Exception {
		Log.d("Max", "at call() ThreadId="+Thread.currentThread().getId()
				+" ThreadName="+Thread.currentThread().getName());
		TimeUnit.SECONDS.sleep(2);
		return new Date().toString();
	}
	
}
