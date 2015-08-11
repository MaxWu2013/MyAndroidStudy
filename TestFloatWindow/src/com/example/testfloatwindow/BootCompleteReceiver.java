package com.example.testfloatwindow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("XXXXXXXXXXXXXXX", "OnReceive .......................");
		Log.e("XXXXXXXXXXXXXXX", "OnReceive .......................");
		Log.e("XXXXXXXXXXXXXXX", "OnReceive .......................");
		Log.e("XXXXXXXXXXXXXXX", "OnReceive .......................");
		Log.e("XXXXXXXXXXXXXXX", "OnReceive .......................");
		Log.e("XXXXXXXXXXXXXXX", "OnReceive .......................");
		Intent fSIntent = new Intent();
		fSIntent.setAction("com.example.testfloatwindow.floatingservice");
		context.startService(fSIntent);
	}

}
