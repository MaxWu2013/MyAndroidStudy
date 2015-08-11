package com.example.myandroidstores;

public interface OnActivityLifeCycleListener {
	
	public void onActivityStart(IActivityLifeCallbackManagered activity);
	
	public void onActivityRestart(IActivityLifeCallbackManagered activity);
	
	public void onActivityStop(IActivityLifeCallbackManagered activity);
}
