package com.example.testthreadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import java.lang.Runnable ;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        
        
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
		        MyExecutor myExecutor = new MyExecutor(2 , 4 , 1000 ,
		        		TimeUnit.MILLISECONDS ,
		        		new LinkedBlockingDeque<Runnable>());
				
				
				List<Future<String>> results= new ArrayList<Future<String>>();
				for(int i=0 ; i<10 ; i++){
		        	SleepTwoSecondsTask task= new SleepTwoSecondsTask();
		        	Future<String> result = myExecutor.submit(task);
		        	results.add(result);
		        }
				
		        for(int i=0 ; i<5 ; i++){
		        	try{
		        		String result = results.get(i).get(1000*10 , TimeUnit.MILLISECONDS);
		        		Log.d("MainActivity", "Result fot Task="+i+
		        				" result="+result+
		        				" threadID="+Thread.currentThread().getId()+
		        				" threadName="+Thread.currentThread().getName());
		        	}catch(TimeoutException e){
		        		e.printStackTrace();
		        	}catch(InterruptedException e){
		        		e.printStackTrace();
		        	}catch(ExecutionException e){
		        		e.printStackTrace();
		        	}
		        }
		        
		        myExecutor.shutdown();
		        
		        for(int i=5 ; i<10; i++){
		        	try{
		        		String result = results.get(i).get();
		        		Log.d("MainActivity", "Result fot Task="+i+
		        				" result="+result+
		        				" threadID="+Thread.currentThread().getId()+
		        				" threadName="+Thread.currentThread().getName());
		        	}catch(InterruptedException e){
		        		e.printStackTrace();
		        	}catch(ExecutionException e){
		        		e.printStackTrace();
		        	}
		        }
		        
		        
		        try{
		        	myExecutor.awaitTermination(1, TimeUnit.DAYS);
		        }catch(InterruptedException e){
		        	e.printStackTrace();
		        }
		        
		        System.out.printf("Main:End of the program \n");
				
			}
		}).start();
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
