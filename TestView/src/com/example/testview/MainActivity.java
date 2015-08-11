package com.example.testview;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;


public class MainActivity extends Activity {

	HorizontialListView listView ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
           //handler.sendEmptyMessage(0);
           String deviceInfo = getDeviceInfo(getApplicationContext());
           Log.e("Max", "deviceInfo = "+deviceInfo);
           
    }
 
    
    public static String getDeviceInfo(Context context) {
        try{
          org.json.JSONObject json = new org.json.JSONObject();
          android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
              .getSystemService(Context.TELEPHONY_SERVICE);
      
          String device_id = tm.getDeviceId();
          
          android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
              
          String mac = wifi.getConnectionInfo().getMacAddress();
          json.put("mac", mac);
          
          if( TextUtils.isEmpty(device_id) ){
            device_id = mac;
          }
          
          if( TextUtils.isEmpty(device_id) ){
            device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
          }
          
          json.put("device_id", device_id);
          
          return json.toString();
        }catch(Exception e){
          e.printStackTrace();
        }
      return null;
    }
    
}   
