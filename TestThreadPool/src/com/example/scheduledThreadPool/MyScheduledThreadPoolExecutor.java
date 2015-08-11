package com.example.scheduledThreadPool;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor{

	public MyScheduledThreadPoolExecutor(int corePoolSize) {
		super(corePoolSize);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected <V> RunnableScheduledFuture<V> decorateTask(
			Runnable runnable ,
			RunnableScheduledFuture<V> task){
		MyScheduledTask<V> myTask = new MyScheduledTask<V>(runnable , null, task ,this);
		return myTask;
	}
	
	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command ,
			long initialDelay , long period , TimeUnit unit){
		
		ScheduledFuture<?> task = super.scheduleAtFixedRate(command, initialDelay, period, unit);
		
		MyScheduledTask<?>myTask =(MyScheduledTask<?>)task;   // 此处之所以能强制转换成MyScheduledTask ,是因为这个Task 是从 decorateTask（）方法返回的
		// 因为 super.scheduleAtFixedRate（） 会初始化一个周期为period的task ,并且放入MySheduledTask ，再调用MyScheduledTask的setPeriod 设置周期 
		myTask.setPeriod(TimeUnit.MILLISECONDS.convert(period, unit));
		
		return task;
		
	}
}
