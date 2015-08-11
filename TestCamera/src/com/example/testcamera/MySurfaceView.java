package com.example.testcamera;

import java.io.IOException;    
import android.content.Context;    
import android.graphics.PixelFormat;    
import android.hardware.Camera;    
import android.hardware.Camera.PictureCallback;    
import android.hardware.Camera.ShutterCallback;    
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;    
import android.view.SurfaceHolder;    
import android.view.SurfaceView;    
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{    
    
	public static String TAG ="MAX";
	
	private byte[] jpegData ;
	
	
	SurfaceHolder holder;    
    Camera myCamera;    
    private ShutterCallback shutter = new ShutterCallback() {    
            
        @Override    
        public void onShutter() {    
            // TODO Auto-generated method stub    
            Log.d(TAG, "shutter");    
                
        }    
    };    
    private PictureCallback raw = new PictureCallback() {    
            
        @Override    
        public void onPictureTaken(byte[] data, Camera camera) {    
            // TODO Auto-generated method stub    
            Log.d(TAG, "raw");    
            jpegData = data ;
        }    
    };
    
    private PictureCallback postview = new PictureCallback(){

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "postview");
			
		}
    	
    };
    
    private PictureCallback jpeg = new PictureCallback() {    
            
        @Override    
        public void onPictureTaken(byte[] data, Camera camera) {    
            // TODO Auto-generated method stub    
            Log.d(TAG,"jpeg");    
                
        }    
    };    
    
    public MySurfaceView(Context context)    
    {    
        super(context);    
        init();
    }
    public MySurfaceView(Context context, AttributeSet attrs) {
		this(context, attrs , 0);
	}
    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	
    private void init(){
       holder = getHolder();
       holder.addCallback(this);    
       holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);      
    }

    public void tackPicture()    
    {    
        myCamera.takePicture(shutter,raw,postview,jpeg);    
    }

    public void voerTack()    
    {    
        myCamera.startPreview();    
        clearData();
    }

    public void clearData(){
    	jpegData = null ;
    }
    
    public byte[] getData(){
    	return jpegData ;
    }
    
    @Override    
    public void surfaceChanged(SurfaceHolder holder, int format, int width,    
            int height) {    
        myCamera.startPreview();            
    }    
    @Override    
    public void surfaceCreated(SurfaceHolder holder) {    
        // TODO Auto-generated method stub    
        if(myCamera == null)    
        {     
            try {
            	myCamera = Camera.open();   
            	
                myCamera.setPreviewDisplay(holder);    
            } catch (IOException e) {    
                // TODO Auto-generated catch block    
                e.printStackTrace();    
            }    
        }           
    }    
    @Override    
    public void surfaceDestroyed(SurfaceHolder holder) {    
        // TODO Auto-generated method stub    
        myCamera.stopPreview();
         myCamera.release();
         myCamera = null;    
                
    }    
}    