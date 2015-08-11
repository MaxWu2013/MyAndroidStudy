package com.example.multiplethreaddownload;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FileService {
	private DBOpenHelper openHelper ;
	
	public FileService(Context context){
		openHelper = new DBOpenHelper(context);
	}
	
	public Map<Integer , Integer> getData(String path){
		//获取可读数据库句柄,一般情况系在该操作的内部实现中， 其返回的其实是可写的数据库句柄
		SQLiteDatabase db = openHelper.getReadableDatabase();
		//根据下载路径查询所有线程下载数据，返回的Cursor 指向第一条记录之前
		Cursor cursor = db.rawQuery("select threadid, downlength from filedownlog where downpath=?", new String[]{path});
		//建立一个哈希表用于存放每条线程的已经下载的文件长度
		Map<Integer , Integer>data = new HashMap<Integer , Integer>();
		
		while(cursor.moveToNext()){
			//从第一条记录开始遍历cursor 
			data.put(cursor.getInt(0),cursor.getInt(1));
		}
		
		cursor.close();
		db.close();
		return data;
	}
	
	public void save(String path , Map<Integer ,Integer>map){
		SQLiteDatabase db = openHelper.getWritableDatabase(); // 获取可写的数据库句柄
		db.beginTransaction(); // 开始事务， 因为此处要插入多批数据
		try{
			for(Map.Entry<Integer, Integer>entry: map.entrySet()){
				//插入特定下载路径， 特定线程ID， 已经下载的数据
				db.execSQL("insert into filedownlog(downpath , threadid , downlength) values(?,?,?)",
						new Object[]{path , entry.getKey() , entry.getValue()});
			}
			db.setTransactionSuccessful(); //设置事务执行的标志位成功
		}finally{
			//结束一个事务， 如果事务设立了成功标志， 则提交事务， 否则回滚事务
			db.endTransaction();
		}
		db.close();
	}
	
	public void update(String path , int threadId ,int pos){
		SQLiteDatabase db = openHelper.getWritableDatabase(); // 获取可写数据库句柄
		
		//更新特定下载路径， 特定线程， 已经下载的文件长度
		db.execSQL("update filedownlog set downlength=? where downpath=? and threadid=?",
				new Object[]{pos , path , threadId});
		
		db.close();
	}
	
	public void delete(String path){
		SQLiteDatabase db = openHelper.getWritableDatabase(); // 获取可写的数据库句柄
		//删除特定下载路径的所有线程记录
		db.execSQL("delete from filedownlog where downpath=?",new Object[]{path});
		
		db.close();
	}
}
