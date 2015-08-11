package com.example.threadsleep;

import android.app.Activity;
import android.os.Bundle;

public class SleepActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FileClock clock = new FileClock();
		Thread thread = new Thread(clock);
		thread.start();
	}
}
