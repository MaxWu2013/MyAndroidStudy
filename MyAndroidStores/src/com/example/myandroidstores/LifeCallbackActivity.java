package com.example.myandroidstores;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import android.app.Activity;
import android.os.Bundle;

public class LifeCallbackActivity extends Activity implements IActivityLifeCallbackManagered{
	/*用于收集该activity下的所有fragmenmt*/
	private WeakHashMap<String, BaseFragment> mFragmentCollection ;
	/*life cycle state listener 监听器*/
	private List<OnActivityLifeCycleListener> mLiftCycleListeners ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initBaseInfo();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		onActivityStartCallBack();
	}
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		onActivityRestartCallBack();
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		onActivityStopCallBack();
	}
	
	/**
	 * 初始化基本变量信息
	 */
	private void initBaseInfo(){
		mFragmentCollection = new WeakHashMap<String, BaseFragment>();
		mLiftCycleListeners = new ArrayList<OnActivityLifeCycleListener>();
	}
	
	@Override
	public void addLifeCallback(OnActivityLifeCycleListener listener) {
		if(!mLiftCycleListeners.contains(listener)){
			mLiftCycleListeners.add(listener);
		}
	}
	
	@Override
	public void removeLifeCallback(OnActivityLifeCycleListener listener) {
		if(mLiftCycleListeners.contains(listener)){
			mLiftCycleListeners.remove(listener);
		}
	}
	
	/**
	 * activity onStart() 回调监听
	 */
	private void onActivityStartCallBack() {
		for(OnActivityLifeCycleListener listener: mLiftCycleListeners){
			listener.onActivityStart(this);
		}
	}
	
	/**
	 * activity onRestart() 回调监听
	 */
	private void onActivityRestartCallBack() {
		for(OnActivityLifeCycleListener listener: mLiftCycleListeners){
			listener.onActivityRestart(this);
		}
	}
	
	/**
	 * activity onStop() 回调监听
	 */
	private void onActivityStopCallBack(){
		for(OnActivityLifeCycleListener listener: mLiftCycleListeners){
			listener.onActivityStop(this);
		}
	}
}
