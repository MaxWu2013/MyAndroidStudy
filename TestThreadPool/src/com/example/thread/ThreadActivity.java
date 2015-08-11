package com.example.thread;

import android.app.Activity;
import android.os.Bundle;

public class ThreadActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		for(int i=1 ; i<=10 ; i++){
			Calculator calculator = new Calculator(i);
			Thread thread = new Thread(calculator);
			thread.start();
		}
	}
}
