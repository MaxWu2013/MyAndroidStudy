package com.example.maxwu.smsgroupsend;

import android.app.Application;

/**
 * Created by maxwu on 8/16/15.
 */
public class MyApplication extends Application{

    public static MyApplication instance = null ;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this ;
    }
}
