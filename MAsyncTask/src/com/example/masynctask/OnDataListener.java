package com.example.masynctask;


/**
 * 
 * @author sfit0215
 * @date 2014-08-01
 * @version V1.0
 * @description 远程数据连接监听
 */
public interface OnDataListener {
	
	/**
	 * 进行数据连接
	 * @param obj
	 * @return result
	 */
	public Result doFetchData(Object obj) throws Exception;
	
	/**
	 * 处理得到的数据
	 */
	public void doProcessData(Object obj) throws Exception;
	
	/**
	 * 处理网络连接错误以及其他异常情况
	 */
	public void doErrorData(Object obj) throws Exception;

}
