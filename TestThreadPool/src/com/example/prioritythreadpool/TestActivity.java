package com.example.prioritythreadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import java.lang.Runnable ;

import com.example.testthreadpool.MyExecutor;
import com.example.testthreadpool.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        
        
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
		        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,2,1,
		        		TimeUnit.SECONDS , new PriorityBlockingQueue<Runnable>());
		        
		        
		        for(int i=0 ; i<4 ; i++){
		        	MyPriorityTask task = new MyPriorityTask("Task "+i, i);
		        	executor.execute(task);
		        }
		        
		        try{
		        	TimeUnit.SECONDS.sleep(1);
		        }catch(InterruptedException e){
		        	
		        }
		        
		        for(int i=4 ; i<8;i++){
		        	MyPriorityTask task = new MyPriorityTask("Task "+i, i);
		        	executor.execute(task);
		        }
		        
		        executor.shutdown();
		        
		        try{
		        	executor.awaitTermination(1, TimeUnit.DAYS);
		        }catch(InterruptedException e){
		        	e.printStackTrace();
		        }
				
		        
		        Log.d("Max", "End of the program.");
			}
		}).start();
        
        
    }
}
