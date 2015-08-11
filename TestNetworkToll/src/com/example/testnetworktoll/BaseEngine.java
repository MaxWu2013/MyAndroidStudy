package com.example.testnetworktoll;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * 基本引擎类
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
