package com.example.daemonthread;

import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class WriterTask implements Runnable{

	private Deque<Event> deque ;
	
	WriterTask(Deque<Event> deque){
		this.deque = deque ;
	}
	
	@Override
	public void run() {
		for(int i=1 ; i<20 ; i++){
			Event event = new Event();
			event.setDate(new Date());
			event.setEvent(String.format("The thread %s has generated an event", Thread.currentThread().getId()));
			
			deque.addFirst(event);
			Log.e("Max ", "generate date="+event.getEvent().toString());
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
		}
		
	}

}
