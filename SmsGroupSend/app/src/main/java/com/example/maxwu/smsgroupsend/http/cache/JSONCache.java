package com.example.maxwu.smsgroupsend.http.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.maxwu.smsgroupsend.Logger;

/**
 * Simple JSON Cache
 * 
 */
public class JSONCache extends SQLiteOpenHelper {

	private static final Object sDatabaseSync = new Object();

	private static final int DATABASE_VERSION = 2;

	private static final String DATABASE_NAME = "cache.db";

	private static final String TABLE_NAME = "cache";

	private static final String _ID = "id";

	private static final String URL = "url";

	private static final String CONTENT = "content";

	private static final String ORDER_BY = _ID + " ASC";

	private static final String[] COLUMNS = { _ID, URL, CONTENT };

	public JSONCache(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + URL + " TEXT NOT NULL unique, "
				+ CONTENT + " TEXT);");
		db.execSQL("CREATE INDEX url_index" + " ON " + TABLE_NAME + String.format("(%s)", URL));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void clearAllData() {
		synchronized (sDatabaseSync) {
			SQLiteDatabase db = getWritableDatabase();
			try {
				db.delete(TABLE_NAME, null, null);
			} catch (Exception e) {
				Logger.e("clearAllData", e);
			} finally {
				db.close();
			}
		}
	}

	public void clearCacheData(String refId) {
		String selection = URL + "='" + refId + "'";
		synchronized (sDatabaseSync) {
			SQLiteDatabase db = getWritableDatabase();
			try {
				db.delete(TABLE_NAME, selection, null);
			} catch (Exception e) {
				Logger.e("getSectionData", e);
			} finally {
				db.close();
			}
		}
	}

	public String getCacheData(String refId) {
		String resp = null;
		String selection = URL + "='" + refId + "'";
		synchronized (sDatabaseSync) {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = null;
			try {
				cursor = db.query(TABLE_NAME, COLUMNS, selection, null, null, null, ORDER_BY);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToLast();
					resp = cursor.getString(2);
				}
			} catch (Exception e) {
				Logger.e("getCacheData", e);
			} finally {
				if (cursor != null) {
					cursor.close();
				}
				db.close();
			}
		}
		return resp;
	}

	public void addCacheData(String url, String resp) {
		ContentValues values = new ContentValues();
		values.put(URL, url);
		values.put(CONTENT, resp);
		synchronized (sDatabaseSync) {
			SQLiteDatabase db = getWritableDatabase();
			try {
				db.replaceOrThrow(TABLE_NAME, null, values);
			} catch (Exception e) {
				Logger.e("addCacheData", e);
			} finally {
				db.close();
			}
		}
	}
}
