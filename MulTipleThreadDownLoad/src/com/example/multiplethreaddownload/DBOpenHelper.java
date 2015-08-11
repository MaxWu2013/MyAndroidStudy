package com.example.multiplethreaddownload;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{
	
	private static final String DBNAME ="eric.db"; //�������ݿ������
	
	private static final int VERSION =1 ;
	
	public DBOpenHelper(Context context){
		super(context , DBNAME , null , VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//�������ݿ��
		db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement , " +
				"								downpath varchar(100) , " +
				"								threadid INTEGER, " +
				"								downlength INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//���汾�仯ʱϵͳ����øûص�����
		db.execSQL("DROP TABLE IF EXISTS filedownlog");//�˴���ɾ�����ݿ�
		//ʵ��ҵ����һ������Ҫ���ݱ��ݵ�
		
		onCreate(db); // ����onCreate �������´������ݱ� ,Ҳ�����Լ�����ҵ����Ҫ�����µ����ݱ�
		
	}
}
