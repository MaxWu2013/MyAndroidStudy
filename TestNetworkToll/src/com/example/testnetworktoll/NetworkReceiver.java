package com.example.testnetworktoll;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class NetworkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		NetStateEngine mNetStateEngine = NetStateEngine.getInstance();
		mNetStateEngine.notifyNetWorkStateChanged(
				NetworkUtils.isNetworkConnected(context),
				NetworkUtils.getConnectedType(context));

	}

}
