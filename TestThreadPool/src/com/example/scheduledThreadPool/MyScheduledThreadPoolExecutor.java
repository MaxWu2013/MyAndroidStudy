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
		
		MyScheduledTask<?>myTask =(MyScheduledTask<?>)task;   // �˴�֮������ǿ��ת����MyScheduledTask ,����Ϊ���Task �Ǵ� decorateTask�����������ص�
		// ��Ϊ super.scheduleAtFixedRate���� ���ʼ��һ������Ϊperiod��task ,���ҷ���MySheduledTask ���ٵ���MyScheduledTask��setPeriod �������� 
		myTask.setPeriod(TimeUnit.MILLISECONDS.convert(period, unit));
		
		return task;
		
	}
}
