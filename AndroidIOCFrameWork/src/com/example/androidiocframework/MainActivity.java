package com.example.androidiocframework;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidiocframework.annotation.ViewInjectUtils;
import com.example.androidiocframework.annotation.interf.ContentView;
import com.example.androidiocframework.annotation.interf.OnClick;
import com.example.androidiocframework.annotation.interf.ViewInject;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends Activity  {

	@ViewInject(R.id.id_btn)
	private Button mBtn1;
	@ViewInject(R.id.id_btn02)
	private Button mBtn2 ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Max", "onCreate");
        
        try {
			ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
			int s_activity_meta_data = info.metaData.getInt("test_activity_meta_data");
			Log.e("Max", "onCreate s_activity_meta_data="+s_activity_meta_data);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ViewInjectUtils.inject(this);
        
    }

	@OnClick({ R.id.id_btn,R.id.id_btn02})
	public void clickBtnInvoked(View v) {
		switch(v.getId()){
		case R.id.id_btn:
			Toast.makeText(getApplicationContext(), "id_btn onClick", Toast.LENGTH_LONG).show();
			break;
		case R.id.id_btn02:
			Toast.makeText(getApplicationContext(), "id_btn02 onClick", Toast.LENGTH_LONG).show();
			break;
		}
		
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.e("Max", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}
 
}
