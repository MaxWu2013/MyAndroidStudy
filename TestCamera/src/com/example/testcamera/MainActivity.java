package com.example.testcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener{

	private final static int CAN_TAKE = 0 ;
	private final static int CLIP_PICTURE =1 ;
	private final static int CANNOT_TAKE=2 ;
	
	Button take ;
	Button ok ;
	Button cancel ;
	Button clip ;
	
	MySurfaceView mySurfaceView;
	
	Handler handler ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        take =(Button)findViewById(R.id.take);
        ok =(Button)findViewById(R.id.ok);
        cancel =(Button)findViewById(R.id.cancel);
        clip=(Button)findViewById(R.id.clip);
        
        take.setText("<font size=\"+5\">T</font><font size=\"+2\">T</font>");
        
        mySurfaceView =(MySurfaceView)findViewById(R.id.camera);
        
        take.setOnClickListener(this);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        clip.setOnClickListener(this);
        
        ok.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        clip.setVisibility(View.INVISIBLE);
        
        handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		switch(msg.what){
        		case CAN_TAKE:
        			take.setVisibility(View.VISIBLE);
        			break;
        		case CLIP_PICTURE:
        			clip.setVisibility(View.VISIBLE);
        			break;
        		case CANNOT_TAKE:
        			ok.setVisibility(View.VISIBLE);
        			cancel.setVisibility(View.VISIBLE);
        			break;
        		}
        	}
        };
        
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
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.take:
			mySurfaceView.tackPicture();
			take.setVisibility(View.INVISIBLE);
			handler.sendEmptyMessageDelayed(CANNOT_TAKE , 1200);
			break;
		case R.id.ok:
			Intent intent = new Intent(MainActivity.this , ClipActivity.class);
			byte[] bytes = mySurfaceView.getData();
			intent.putExtra("PICTURE_DATE", bytes);
			startActivity(intent);
			break;
		case R.id.cancel:
			break;
		case R.id.clip:
			break;
		}
		
	}
}
