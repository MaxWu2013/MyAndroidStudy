package com.example.myandroidstores.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import android.text.format.Time;

/**
 * Log工具打印类
 * */
public class ILog {

    // 锁，是否关闭Log日志输出
    public static final boolean LOGON = true;

    public static final boolean isWriteLog = false;
    
    // 五种Log日志类型
    // 调试日志类型
    public static final int DEBUG = 111;

    // 错误日志类型
    public static final int ERROR = 112;

    // 信息日志类型
    public static final int INFO = 113;

    // 详细信息日志类型
    public static final int VERBOSE = 114;

    // 警告日志类型
    public static final int WARN = 115;

    private static boolean MFLAG = true;

    /**
     * 缓冲目录
     **/
    public static final String ROOT_ADDR = "/mnt/sdcard/temple/";

    /**
     * 日志文件路径
     * */
    public static final String LOG_ADDR = ROOT_ADDR + "Log.txt";

    public static void e(String Tag, String msg) {
	if (LOGON) {
	    if (null == Tag || null == msg) {
		return;
	    }
	    android.util.Log.e(Tag, msg);
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	   
	}
    }

    public static void e(String Tag, String title, String msg) {
	if (LOGON) {
	    if (null == Tag || null == msg || null == title) {
		return;
	    }
	    android.util.Log.e(Tag, title + " : " + msg);
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	}
    }

    public static void d(String Tag, String msg) {
	if (LOGON) {
	    if (null != Tag && null != msg) {
		android.util.Log.d(Tag, msg);
	    }
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	}
    }

    public static void d(String Tag, String title, String msg) {
	if (LOGON) {
	    if (null == Tag || null == msg || null == title) {
		return;
	    }
	    android.util.Log.d(Tag, title + " : " + msg);
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	}
    }

    public static void i(String Tag, String msg) {
	if (LOGON) {
	    if (null == Tag || null == msg) {
		return;
	    }
	    android.util.Log.i(Tag, msg);
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	}
    }

    public static void i(String Tag, String title, String msg) {
	if (LOGON) {
	    if (null == Tag || null == msg || null == title) {
		return;
	    }
	    android.util.Log.i(Tag, title + " : " + msg);
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	}
    }

    public static void v(String Tag, String msg) {
	if (LOGON) {
	    if (null == Tag || null == msg) {
		return;
	    }
	    android.util.Log.v(Tag, msg);
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	}
    }

    public static void v(String Tag, String title, String msg) {
	if (LOGON) {
	    if (null == Tag || null == msg || null == title) {
		return;
	    }
	    android.util.Log.v(Tag, title + " : " + msg);
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	}
    }

    public static void w(String Tag, String msg) {
	if (LOGON) {
	    if (null == Tag || null == msg) {
		return;
	    }
	    android.util.Log.w(Tag, msg);
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	}
    }

    public static void w(String Tag, String title, String msg) {
	if (LOGON) {
	    if (null == Tag || null == msg || null == title) {
		return;
	    }
	    android.util.Log.w(Tag, msg);
	    if(isWriteLog){
	    	writeLogToFile(Tag, msg);
	    }
	}
    }
    
    /**
     * 判断Log文件是否存在
     */
    public static void initFlag() {
	if (new File(LOG_ADDR).exists()) {
	    MFLAG = true;
	} else {
	    MFLAG = false;
	}

    }

    public static boolean getFlag() {
    	return MFLAG;
    }

    public static void setFlag(boolean flag) {
		MFLAG = flag;
		File file = new File(LOG_ADDR);
		if (flag == true) {
		    if (!file.exists()) {
				try {
				    file.createNewFile();
				} catch (IOException e) {
				    e.printStackTrace();
				}
				return;
		    }
		} else {
		    if (file.exists()) {
			file.delete();
		    }
		}

    }

    public static void writeLog(String Tag, String msg) {
		initFlag();
		writeLogToFile(Tag, msg);
    }

    private static void writeLogToFile(String Tag, String msg) {
	if (getFlag() == false) {
	    return;
	}
	File logfile = new File(LOG_ADDR);
	if (!logfile.exists()) {
	    try {
		logfile.createNewFile();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	StringBuffer logSb = new StringBuffer();
	Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料�?
	t.setToNow(); // 取得系统时间�?
	logSb.append(String.format("%d-%d-%d ", t.monthDay, t.hour, t.minute));
	logSb.append(Tag + "  ");
	logSb.append(msg + "\n");
	OutputStreamWriter fileWrite = null;
	try {
	    fileWrite = new OutputStreamWriter(new FileOutputStream(LOG_ADDR,
		    true), "utf-8");
	    if (fileWrite != null) {
		fileWrite.write(logSb.toString());
	    }

	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    return;
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return;
	} catch (IOException e) {

	    e.printStackTrace();
	} finally {
	    if (fileWrite != null) {
		try {
		    fileWrite.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
}

