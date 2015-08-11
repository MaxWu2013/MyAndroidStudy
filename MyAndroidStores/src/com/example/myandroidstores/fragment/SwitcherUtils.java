package com.example.myandroidstores.fragment;


import android.app.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * 这是一个辅助类，用于各个界面切换或者通用的fragment切换之类的
 * @author meifei
 *
 */
public class SwitcherUtils {
	
	private static final String TAG="SwitcherUtils";
	
	/**插入一个fragment
	 * 不会压栈原先的fragment
	 * @param activity
	 * @param layoutId
	 * @param fragment
	 */
	public static void replaceFragment(FragmentActivity activity,int layoutId,Fragment fragment){
		if(fragment == null){
			Log.e(TAG,"fragment is null! please check the source!");
		}else{
			FragmentTransaction transaction =activity.getSupportFragmentManager().beginTransaction();
			transaction.replace(layoutId,fragment);
			//transaction.r
			transaction.commitAllowingStateLoss();
		}
		
	}
}
