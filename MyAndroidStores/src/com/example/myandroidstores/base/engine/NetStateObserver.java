package com.example.myandroidstores.base.engine;

/**
 * 网络状态观察者
 * @author MaxWu
 * @date 2014-12-8
 * @time 上午10:36:07
 * @description ：
 */
public interface NetStateObserver extends IStateObserver{
	/**
	 * 网络状态改变
	 * @param oldType   
	 * @param newType
	 */
	public  void onNetStateChanged(boolean isConnected , int oldType , int newType);
}
