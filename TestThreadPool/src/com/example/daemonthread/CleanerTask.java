package com.example.daemonthread;

import java.util.Date;
import java.util.Deque;

import android.util.Log;

public class CleanerTask extends Thread{
	
	private Deque<Event> deque ;
	
	public CleanerTask(Deque<Event> deque){
		this.deque = deque;
		setDaemon(true);
		
		setUncaughtExceptionHandler(new ExceptionHandler());
	}
	
	@Override
	public void run() {
		while(true){
			Date date = new Date();
			clean(date);
		}
	}
	
	private void clean(Date date){
		long difference ;
		boolean delete;
		
		if(deque.size()==0){
			return ;
		}
		delete = false;
		do{
			Event e = deque.getLast();
			difference = date.getTime()-e.getDate().getTime();
			if(difference>10000){
				Log.e("Max", "Cleaner :"+e.getEvent());
				deque.removeLast();
				delete = true;
			}
		}while(difference >10000);
		
		if(delete){
			Log.e("Max", "Cleaner:Size of the queue:"+deque.size());
		}
	}
}
