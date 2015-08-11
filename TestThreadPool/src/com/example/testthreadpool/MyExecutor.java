package com.example.testthreadpool;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class MyExecutor extends ThreadPoolExecutor{
	
	private ConcurrentHashMap<String , Date> startTimes ;
	
	public MyExecutor(int corePoolSize ,
			int maximumPoolSize , 
			long keepAliveTime ,
			TimeUnit unit ,
			BlockingQueue<Runnable> workQueue){
		super(corePoolSize , maximumPoolSize ,keepAliveTime , unit , workQueue);
		startTimes = new ConcurrentHashMap<String, Date>();
	}
	
	@Override
	public void shutdown(){
		Log.d("Max", "ShutDown Executed tasks="+getCompletedTaskCount()+
				" Running tasks="+getActiveCount()+
				" Pending tasks="+getQueue().size()+
				" Executed tasks="+getCompletedTaskCount()+
				" ThreadId="+Thread.currentThread().getId()+
				" ThreadName="+Thread.currentThread().getName());
		super.shutdown();
	}
	
	@Override
	public List<Runnable> shutdownNow(){
		Log.d("Max", "ShutDownNow Executed tasks="+getCompletedTaskCount()+
				" Running tasks="+getActiveCount()+
				" Pending tasks="+getQueue().size()+
				" Executed tasks="+getCompletedTaskCount()+
				" ThreadId="+Thread.currentThread().getId()+
				" ThreadName="+Thread.currentThread().getName());
		return super.shutdownNow();
	}
	@Override
	protected void beforeExecute(Thread t , Runnable r){
		Log.d("beforeExecute", " A task is beginning :"+t.getName()
				+" id="+t.getId()
				+" currentThreadId ="+Thread.currentThread().getId()
				+" currentThreadName="+Thread.currentThread().getName()
				+" hashCode="+r.hashCode());
		startTimes.put(String.valueOf(r.hashCode()), new Date());
	}
	
	@Override
	protected void afterExecute(Runnable r , Throwable t){
		Future<?> result =(Future<?>)r ;
		try{
			Date startDate=startTimes.remove(String.valueOf(r.hashCode()));
			Date finishDate=new Date();
			long diff=finishDate.getTime()-startDate.getTime();
			
			Log.d("afterExecute", "A task is finishing result="+result.get()+
					" Duration="+diff+
					" currentThreadId="+Thread.currentThread().getId()+
					" currentThreadname="+Thread.currentThread().getName());
			
			
		}catch(InterruptedException e){
			e.printStackTrace();
		}catch(ExecutionException e){
			e.printStackTrace();
		}
	}
}
