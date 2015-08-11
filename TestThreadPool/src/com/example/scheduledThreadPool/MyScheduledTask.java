package com.example.scheduledThreadPool;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class MyScheduledTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {
	
	private RunnableScheduledFuture<V> task;
	
	private ScheduledThreadPoolExecutor executor ;
	
	private long period ;
	
	private long startDate ;
	
	public MyScheduledTask(Runnable runnable, V result ,
			RunnableScheduledFuture<V> task , 
			ScheduledThreadPoolExecutor executor) {
		super(runnable, result);
		this.task = task;
		this.executor=executor;
	}
	//获取当前需要延迟的时间 , unit 参数传的目的是告诉我们 需要返回的时间的单位
	@Override
	public long getDelay(TimeUnit unit){
		if(!isPeriodic()){
			return task.getDelay(unit);
		}else{
			if(startDate==0){
				return task.getDelay(unit);
			}else{
				Date now = new Date();
				long delay = startDate-now.getTime();
				return unit.convert(delay, TimeUnit.MILLISECONDS);
			}
		}
	}
	
	
	@Override
	public int compareTo(Delayed o){
		return task.compareTo(o);
	}
	
	@Override
	public boolean isPeriodic(){
		return task.isPeriodic();
	}
	
	@Override
	public void run(){
		if(isPeriodic() && (!executor.isShutdown())){
			//You have also to take into account if the executor has been shutdown. 
			//In that case, you don't have to store again the periodic tasks into the 
			//queue of the executor
			Date now = new Date();
			startDate =now.getTime()+period;
			executor.getQueue().add(this);
		}
		
		Log.d("Run () ","pre-   cur="+new Date()
			+"Is Periodic="+isPeriodic());
		
		super.runAndReset();
		
		Log.d("Run() ", "post-  cur="+new Date()); 
	}
	public void setPeriod(long period){
		this.period = period;
	}
}
