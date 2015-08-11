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
		//��ȡ�ɶ����ݿ���,һ�����ϵ�ڸò������ڲ�ʵ���У� �䷵�ص���ʵ�ǿ�д�����ݿ���
		SQLiteDatabase db = openHelper.getReadableDatabase();
		//��������·����ѯ�����߳��������ݣ����ص�Cursor ָ���һ����¼֮ǰ
		Cursor cursor = db.rawQuery("select threadid, downlength from filedownlog where downpath=?", new String[]{path});
		//����һ����ϣ�����ڴ��ÿ���̵߳��Ѿ����ص��ļ�����
		Map<Integer , Integer>data = new HashMap<Integer , Integer>();
		
		while(cursor.moveToNext()){
			//�ӵ�һ����¼��ʼ����cursor 
			data.put(cursor.getInt(0),cursor.getInt(1));
		}
		
		cursor.close();
		db.close();
		return data;
	}
	
	public void save(String path , Map<Integer ,Integer>map){
		SQLiteDatabase db = openHelper.getWritableDatabase(); // ��ȡ��д�����ݿ���
		db.beginTransaction(); // ��ʼ���� ��Ϊ�˴�Ҫ�����������
		try{
			for(Map.Entry<Integer, Integer>entry: map.entrySet()){
				//�����ض�����·���� �ض��߳�ID�� �Ѿ����ص�����
				db.execSQL("insert into filedownlog(downpath , threadid , downlength) values(?,?,?)",
						new Object[]{path , entry.getKey() , entry.getValue()});
			}
			db.setTransactionSuccessful(); //��������ִ�еı�־λ�ɹ�
		}finally{
			//����һ������ ������������˳ɹ���־�� ���ύ���� ����ع�����
			db.endTransaction();
		}
		db.close();
	}
	
	public void update(String path , int threadId ,int pos){
		SQLiteDatabase db = openHelper.getWritableDatabase(); // ��ȡ��д���ݿ���
		
		//�����ض�����·���� �ض��̣߳� �Ѿ����ص��ļ�����
		db.execSQL("update filedownlog set downlength=? where downpath=? and threadid=?",
				new Object[]{pos , path , threadId});
		
		db.close();
	}
	
	public void delete(String path){
		SQLiteDatabase db = openHelper.getWritableDatabase(); // ��ȡ��д�����ݿ���
		//ɾ���ض�����·���������̼߳�¼
		db.execSQL("delete from filedownlog where downpath=?",new Object[]{path});
		
		db.close();
	}
}
