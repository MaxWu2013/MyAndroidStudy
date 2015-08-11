package com.example.testnetworktoll;


public interface NetStateObserver extends IStateObserver{
	/**
	 * 网络状态改变
	 * @param oldType   
	 * @param newType
	 */
	public  void onNetStateChanged(boolean isConnected , int oldType , int newType);
}
