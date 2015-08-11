package com.example.daemonthread;

import java.util.ArrayDeque;
import java.util.Deque;

import android.app.Activity;
import android.os.Bundle;

public class DaemonThreadActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Deque<Event> deque = new ArrayDeque<Event>();
		
		WriterTask writer = new WriterTask(deque);
		
		
		for(int i=0;i<3 ;i++){
			Thread thread = new Thread(writer);
			thread.start();
		}
		
		CleanerTask cleaner = new CleanerTask(deque);
		cleaner.start();
	}
}
