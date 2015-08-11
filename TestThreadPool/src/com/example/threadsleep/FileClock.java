package com.example.threadsleep;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class FileClock implements Runnable{

	@Override
	public void run() {
		Log.e("Max", "time = "+new Date().toString());
		for(int i=0  ; i<10 ; i++){
			Log.e("Max", "time = "+new Date().toString());
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				Log.e("Max", "The FileClock has been interrupted");
			}
			
		}
		
	}

}
