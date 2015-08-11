package com.example.testnetworktoll;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;


public class NetStateEngine implements BaseEngine{
	
	private static int oldType = -1  ;   // ConnectivityManager.TYPE_NONE
	private static List<IStateObserver> observers = new ArrayList<IStateObserver>();
	private static NetStateEngine instance ; 

	private NetStateEngine(){
		
	}
	
	public static NetStateEngine getInstance(){
		if(null == instance){
			instance = new NetStateEngine();
		}
		return instance ;
	}

	@Override
	public boolean attachIStateObserver(IStateObserver observer) {
		if(null == observer){
			throw new NullPointerException("obser is null");
		}
		if(!(observer instanceof NetStateObserver)){
			throw new RuntimeException("observer need to be NetStateObserver instance");
		}
		
		if(!observers.contains(observer)){
			observers.add(observer);
		}
		
		return true;
	}
	
	@Override
	public boolean detachIStateObserver(IStateObserver observer) {
		if(null == observer || observers.size()==0){
			return false;
		}
		if(!(observer instanceof NetStateObserver)){
			throw new RuntimeException("observer need to be NetStateObserver instance");
		}
		if(observers.contains(observer)){
			observers.remove(observer);
		}
		return true;
	}
	
	void  notifyNetWorkStateChanged(boolean isConnected , int connectedType){
		
		if(oldType != connectedType){
			for(IStateObserver s : observers){
				((NetStateObserver)s).onNetStateChanged(isConnected, oldType, connectedType);
			}
		}
		oldType = connectedType ;
	}
}
