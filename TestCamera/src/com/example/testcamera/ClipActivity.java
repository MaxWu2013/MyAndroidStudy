package com.example.testcamera;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class ClipActivity extends Activity{

	Button ok ;
	Button cancel ;
	ImageView imageView  ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip);
		ok = (Button)findViewById(R.id.ok);
		cancel = (Button)findViewById(R.id.cancel);
		imageView = (ImageView)findViewById(R.id.imageview);
	}
	
}
