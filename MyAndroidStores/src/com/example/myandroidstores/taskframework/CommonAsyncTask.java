package com.example.myandroidstores.taskframework;

import android.os.AsyncTask;
import android.util.Log;


public class CommonAsyncTask {

	private final static String TAG = "CommonAsyncTask";
	/** 数据加载任务 **/
	private DataAsyncTask mTask;
	/** 加载监听 **/
	private OnDataListener mDataListener;

	/** 连接id **/
	private int mConnectionType;
	
	public CommonAsyncTask(){}
	
	public CommonAsyncTask(OnDataListener listener){
		mDataListener = listener;
	}

	/**
	 * 开启一个异步任务
	 * @param connectionType 连接id
	 */
	public void doConnection(int connectionType) {
		mTask = new DataAsyncTask();
		mConnectionType = connectionType;
		mTask.execute();
	}

	/**
	 * 开启一个异步任务
	 * @param connectionType 连接id
	 * @param params 参数
	 */
	public void doConnection(int connectionType, Object... params) {
		if (mTask == null && connectionType >= 0) {
			mConnectionType = connectionType;
			mTask = new DataAsyncTask();
			mTask.execute(params);
		} else {
			Log.e("Max", "doConnection 开启异步任务异常！");
		}
	}


	/**
	 * 关闭当前连接
	 */
	public void disConnection() {
		if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) {
			mConnectionType = 0;
			mTask.cancel(true);
			Log.e("Max", "销毁异步任务对象");
		}
		mTask = null;
	}

	/**
	 * 设置监听
	 * @param mDataListener
	 */
	public void setDataListener(OnDataListener dataListener) {
		this.mDataListener = dataListener;
	}

	class DataAsyncTask extends AsyncTask<Object, String, Result> {

		@Override
		protected Result doInBackground(Object... params) {
			Result result = new Result();
			result.setType(mConnectionType);
			result.setParams(params);

			Log.d(TAG, "Max" + mDataListener);

			try {
				result = mDataListener.doFetchData(result);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(Result result) {

			try {
				if (null != mDataListener) {
					if(result == null){
						mDataListener.doErrorData(null);
						return;
					}
					result.setType(mConnectionType);
					if(result.isSucc()){
						mDataListener.doProcessData(result);
					}else{
						mDataListener.doErrorData(result);
					}
				} else {
					Log.e("Max", "IDataListener为空,请设置.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				disConnection();
			}
		}
	}

}
