package com.example.myandroidstores;

import android.app.Activity;

/**
 * 管理Activty生命周期回调管理器
 * @author ASRock
 *
 */
public interface IActivityLifeCallbackManagered {
	/**
	 *  添加Actiivty生命周期监听器
	 * @param listener
	 */
	public void addLifeCallback(OnActivityLifeCycleListener listener);
	/**
	 * 移除Actiivty生命周期监听器
	 * @param listener
	 */
	public void removeLifeCallback(OnActivityLifeCycleListener listener);
	
	
}
