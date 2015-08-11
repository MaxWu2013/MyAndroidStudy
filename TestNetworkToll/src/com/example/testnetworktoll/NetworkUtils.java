package com.example.testnetworktoll;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	/**
	 * 判断是否有网络连�?
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context){
		if( null != context){
			ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if( null != mNetworkInfo){
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}
	/**
	 * 判断wifi网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context){
		
		if(null !=context){
			ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if( null != mWifiNetworkInfo){
				return mWifiNetworkInfo.isAvailable();
			}
		}
		
		return false;
	}
	/**
	 * 判断mobile 网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context){
		if( null != context){
			ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo=mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if(null != mMobileNetworkInfo){
				return mMobileNetworkInfo.isAvailable();
			}
		}
		
		return false;
	}
	/**
	 * 获取当前可用连接网络的类型
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context){
		if(null != context){
			ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if( null != mNetworkInfo && mNetworkInfo.isAvailable()){
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}
}
