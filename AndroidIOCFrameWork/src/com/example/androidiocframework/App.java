package com.example.androidiocframework;

import java.util.WeakHashMap;

import com.example.androidiocframework.base.BaseFragmentActivity;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class App extends Application{

	private WeakHashMap<String, BaseFragmentActivity> mAcCollection ;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.e("Max", "App onCreate");
		mAcCollection = new WeakHashMap<String, BaseFragmentActivity>();
		try {
			ApplicationInfo info = getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA);
			int s_app_meta_info = info.metaData.getInt("test_app_meta_data");
			
			Log.e("Max", "App s_app_meta_info="+s_app_meta_info);
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void add(BaseFragmentActivity baseFragmentActivity){
		if(null == baseFragmentActivity){
			throw new NullPointerException("baseFragmentActivity is null");
		}
		boolean added = mAcCollection.containsKey(baseFragmentActivity.getClass().toString());
		if(!added){
			mAcCollection.put(baseFragmentActivity.getClass().toString(), baseFragmentActivity);
		}
	}
}
