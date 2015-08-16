/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * package-level logging flag
 */

package com.example.maxwu.smsgroupsend;

public class Logger {
	public final static String LOGTAG = "Ecommunity";

	public static boolean IFLOG = true;

	public static void v(String logMe) {
		if (IFLOG)
			android.util.Log.v(LOGTAG, logMe);
	}

	public static void i(String logMe) {
		if (IFLOG)
			android.util.Log.i(LOGTAG, logMe);
	}

	public static void e(String logMe) {
		if (IFLOG)
			android.util.Log.e(LOGTAG, logMe);
	}

    public static void w(String logMe){
        if(IFLOG){
            android.util.Log.w(LOGTAG,logMe);
        }
    }

	public static void e(String logMe, Exception ex) {
		if (IFLOG)
			android.util.Log.e(LOGTAG, logMe, ex);
	}
	public static void d(String string) {
		if (IFLOG)
			android.util.Log.d(LOGTAG, string);
	}

	/**
	 * 打印 log 信息
	 * @param string
	 */
	public static void m(String string){
		if(IFLOG){
			android.util.Log.e("MaxWu",string);
		}
	}

	public static void printMemory(String msg)
	{
		d(msg);
		d("maxMemory: " + Runtime.getRuntime().maxMemory()/1024 + "KB");
		d("totalMemory: " + Runtime.getRuntime().totalMemory()/1024 + "KB");
		d("freeMemory: " + Runtime.getRuntime().freeMemory()/1024 + "KB");
		d("nativeHeapSize: " + android.os.Debug.getNativeHeapSize()/1024 + "KB");
		d("nativeHeapAllocatedSize: " + android.os.Debug.getNativeHeapAllocatedSize()/1024 + "KB");
		d("nativeHeapFreeSize: " + android.os.Debug.getNativeHeapFreeSize()/1024 + "KB");
	}
}
