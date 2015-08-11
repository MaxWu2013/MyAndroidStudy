package com.example.masynctask;

import android.app.Activity;
import android.widget.Toast;

public abstract class BaseActivity extends Activity implements OnDataListener {

	private CommonAsyncTask task;

	@Override
	public abstract Result doFetchData(Object obj) throws Exception ;
	
	
	@Override
	public abstract void doProcessData(Object obj) throws Exception ;
	
	/**
	 * 返回错误后提示
	 */
	@Override
	public void doErrorData(Object obj) throws Exception {
		Result result = (Result)obj;
		if (null != result) {
			Toast.makeText(this, result.getMsg(), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this ,"server error" , Toast.LENGTH_SHORT).show();
		}
	}
	

	/*
	 * 新建线程后台处理
	 */
	protected void doConnection(int type, Object... params) {
		task = new CommonAsyncTask(this);
		task.doConnection(type, params);
	}
	protected void doConnection(int type) {
		task = new CommonAsyncTask(this);
		task.doConnection(type);
	}
}
