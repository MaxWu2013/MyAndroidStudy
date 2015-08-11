package com.example.executeservicewiththreadfactory;

import java.util.ArrayList;
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


public class FourActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				MyThreadFactory threadFactory = new MyThreadFactory("MyThreadFactory");
		        
				ExecutorService executor = Executors.newCachedThreadPool(threadFactory);
				
				MyTask task = new MyTask();
				
				executor.submit(task);
				
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
