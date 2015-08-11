package com.example.daemonthread;

import java.lang.Thread.UncaughtExceptionHandler;

import android.util.Log;

public class ExceptionHandler implements UncaughtExceptionHandler{

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Log.e("Max", "An exception has been captured");
		Log.e("Max","Thread id = "+t.getId());
		Log.e("Max", "Exception : "+e.getClass().getName());
		Log.e("Max", "Message="+e.getMessage());
		Log.e("Max"," Stack Trace="+e.getStackTrace() );
		
	}

}
