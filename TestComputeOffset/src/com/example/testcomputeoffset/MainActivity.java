package com.example.testcomputeoffset;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Button;


public class MainActivity extends Activity {

	CustomView animationView ;
	Button btn ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Transformation tf = new Transformation();
        
        animationView = (CustomView)findViewById(R.id.customview);
        
        btn = (Button) findViewById(R.id.btn);
        
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/*animationView.setScrollLength(animationView.getWidth());
				animationView.startAnimation();*/
				
				Log.i("Max", "info="+getDeviceInfo(MainActivity.this));
			}
		});

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
