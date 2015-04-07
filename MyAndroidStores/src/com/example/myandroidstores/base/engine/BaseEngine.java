package com.example.myandroidstores.base.engine;

/**
 * 基本引擎类:用于状态观察器
 */
public interface BaseEngine {

	/**
	 * 添加状态改变监听 ， 添加之后要注意移除IStateObserver
	 * @param observer
	 * @return
	 */
	public boolean attachIStateObserver(IStateObserver observer);

	/**
	 * 移除状态改变监听
	 * @param observer
	 */
	public boolean detachIStateObserver(IStateObserver observer);

}
