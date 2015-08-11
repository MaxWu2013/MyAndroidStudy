package com.example.myandroidstores.base.engine;



import com.example.myandroidstores.R;
import com.example.myandroidstores.web.NetworkUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;
/**
 * 网络状态广播接收器
 * @author MaxWu
 * @date 2014-12-8
 * @time 上午10:36:38
 * @description ：
 */
public class NetworkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		NetStateEngine mNetStateEngine = NetStateEngine.getInstance();
		
		int type = NetworkUtils.getConnectedType(context);
		
		mNetStateEngine.notifyNetWorkStateChanged(
				NetworkUtils.isNetworkConnected(context),
				type );
		
		if(ConnectivityManager.TYPE_MOBILE == type){
			Toast.makeText(context, context.getString(R.string.net_work_connected_mobile), Toast.LENGTH_SHORT).show();
		}else if(ConnectivityManager.TYPE_WIFI == type){
			Toast.makeText(context, context.getString(R.string.net_work_connected_wifi), Toast.LENGTH_SHORT).show();
		}else if(-1 == type){
			Toast.makeText(context, context.getString(R.string.net_work_connected_fail), Toast.LENGTH_SHORT).show();
		}

	}

}
