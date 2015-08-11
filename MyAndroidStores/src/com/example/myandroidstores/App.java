package com.example.myandroidstores;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class App extends Application implements OnActivityLifeCycleListener{

	private WeakHashMap<Integer, IActivityLifeCallbackManagered> mAcCollection ;
	private WeakReference<IActivityLifeCallbackManagered> mCurrentActivity ;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.e("Max", "App onCreate");
		mAcCollection = new WeakHashMap<Integer, IActivityLifeCallbackManagered>();
		try {
			ApplicationInfo info = getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA);
			int s_app_meta_info = info.metaData.getInt("test_app_meta_data");
			
			Log.e("Max", "App s_app_meta_info="+s_app_meta_info);
			Object object = new Object();
			object.hashCode();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	/**
	 * 添加Activity记录
	 * @param baseFragmentActivity
	 */
	private void add(IActivityLifeCallbackManagered activity) throws IllegalAccessException{
		if(null == activity){
			throw new NullPointerException("activity is null");
		}
		
		if(! (activity instanceof Activity)){
			throw new IllegalAccessException("activity is not an Instance Of Activity");
		}
		
		//通过哈希值，标识唯一对象
		boolean added = mAcCollection.containsKey(activity.hashCode());
		if(!added){
			//通过哈希值，标识唯一对象
			mAcCollection.put(activity.hashCode(), activity);
			activity.addLifeCallback(this);
		}
	}


	@Override
	public void onActivityStart(IActivityLifeCallbackManagered activity) {
		mCurrentActivity = new WeakReference<IActivityLifeCallbackManagered>(activity);
		
	}


	@Override
	public void onActivityRestart(IActivityLifeCallbackManagered activity) {
	}


	@Override
	public void onActivityStop(IActivityLifeCallbackManagered activity) {
		// TODO Auto-generated method stub
		
	}
	
}
