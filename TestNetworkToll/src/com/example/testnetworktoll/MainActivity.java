package com.example.testnetworktoll;

import android.util.Log;
import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends Activity {

	TextView mNetwork ;
	TextView mWifi ;
	TextView mMobileNetwork; 
	NetworkReceiver receiver ;
	
	NetStateEngine mNetStateEngine = NetStateEngine.getInstance();
	
	NetStateObserver observer  = new NetStateObserver(){
		@Override
		public void onNetStateChanged(boolean isConnected, int oldType,
				int newType) {
			display();
		}
		
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNetwork = (TextView)findViewById(R.id.network_available);
        mWifi = (TextView)findViewById(R.id.wifi_available);
        mMobileNetwork = (TextView)findViewById(R.id.mobilenetwork_available);
        
        display();
        
        receiver = new NetworkReceiver();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        MainActivity.this.registerReceiver(receiver, filter);
        
        mNetStateEngine.attachIStateObserver(observer);
        
    }

    
    private void display(){
    	boolean isNetwork = NetworkUtils.isNetworkConnected(this);
        boolean isWifi = NetworkUtils.isWifiConnected(this);
        boolean isMobileNetwork = NetworkUtils.isMobileConnected(this);
        
        int type = NetworkUtils.getConnectedType(this);
        
        Log.i("Max", "net type = " + type);
        
        if(isNetwork){
        	mNetwork.setText(mNetwork.getText()+" true ");
        }else{
        	mNetwork.setText(mNetwork.getText()+" false ");
        }
        
        if(isWifi){
        	mWifi.setText(mWifi.getText()+" true ");
        }else{
        	mWifi.setText(mWifi.getText()+" false ");
        }
        
        if(isMobileNetwork){
        	mMobileNetwork.setText(mMobileNetwork.getText()+" true ");
        }else{
        	mMobileNetwork.setText(mMobileNetwork.getText()+" false ");
        }
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	MainActivity.this.unregisterReceiver(receiver);
    	mNetStateEngine.detachIStateObserver(observer);
    	
    }
}
