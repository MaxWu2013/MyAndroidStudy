package com.example.testwebtoactivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestWebToActivity extends Activity{
	public final static String TAG = "MAX";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView textView = (TextView) findViewById(R.id.textview);
		textView.setText(" here comes to activity");
		
		
		Intent intent = getIntent();
		if(null != intent){
			String scheme = intent.getScheme();
			
			Uri uri = intent.getData();
			if(null != uri){
				String host = uri.getHost();
				String dataString = intent.getDataString();
				String id = uri.getQueryParameter("id");
				String path = uri.getPath();
				String path1 = uri.getEncodedPath();
				String queryString =uri.getQuery();
				
				Log.e(TAG, "scheme="+scheme);
				Log.e(TAG, "host="+host);
				Log.e(TAG, "dataString="+dataString);
				Log.e(TAG, "id="+id);
				Log.e(TAG, "path="+path);
				Log.e(TAG, "path1="+path1);
				Log.e(TAG, "queryString="+queryString);
				Log.e(TAG, "");
			}
			
		}
		
		
	}
}
