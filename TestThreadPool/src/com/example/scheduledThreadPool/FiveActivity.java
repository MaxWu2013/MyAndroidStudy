package com.example.scheduledThreadPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import java.lang.Runnable ;

import com.example.myThreadFactory.MyTask;
import com.example.myThreadFactory.MyThreadFactory;
import com.example.testthreadpool.MyExecutor;
import com.example.testthreadpool.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * SceheduledThreadPool is a thread pool that manage the thread width period runnable
 * 调度线程池 用来 管理 具有周期运行性质的 线程 ， 
 * @author MaxWu
 * @date 2014-10-16
 * @time 上午8:53:46
 * @description ：
 */
public class FiveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				MyScheduledThreadPoolExecutor executor = new MyScheduledThreadPoolExecutor(2);
				
				Task task = new Task();
				
				Log.d("Max", "cur ="+new Date());
				 
				executor.schedule(task, 1, TimeUnit.SECONDS);
				
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Task task1 = new Task();
				Log.d("Max" , "cur ="+new Date());
				
				executor.scheduleAtFixedRate(task1, 1, 3, TimeUnit.SECONDS);
				
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				executor.shutdown();
				
				try {
					executor.awaitTermination(1, TimeUnit.DAYS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		        Log.d("Max", "End of the program.");
			}
		}).start();
        
        
    }
}
