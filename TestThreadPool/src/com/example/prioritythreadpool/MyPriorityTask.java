package com.example.prioritythreadpool;

import java.util.concurrent.TimeUnit;

import android.util.Log;

public class MyPriorityTask implements Runnable , Comparable<MyPriorityTask>{

	private int priority ;
	
	private String name ;
	
	public MyPriorityTask(String name , int priority){
		this.name = name ;
		this.priority = priority ;
	}
	
	public int getPriority(){
		return priority;
	}
	
	@Override
	public void run() {
		Log.d("MyPriority Run()" , " name="+name
				+" priority="+priority);
		
		try{
			TimeUnit.SECONDS.sleep(8);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public int compareTo(MyPriorityTask o) {
		if(this.getPriority()<o.getPriority()){
			return 1;
		}
		if(this.getPriority()>o.getPriority()){
			return -1 ;
		}
		
		return 0;
	}
	
}
