package com.example.multiplethreaddownload;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private static final int PROCESSING =1 ;// 正在下载实时数据传输 MESSAGE 标志
	
	private static final int FAILURE=-1 ;//下载失败时的MESSAGE 标志
	
	private EditText pathText ;			//下载输入文本框
	private TextView resultView ;		//自安装进度显示百分比文本框
	
	private Button downloadButton; 		// 下载按钮， 可以触发下载事件
	private Button stopbutton ; 		//停止下载按钮， 可以停止下载
	private ProgressBar progressBar ; // 下载进度条， 实时图形化的显示进度信息
	
	//handler 对象的作用是向创建handler对象所在的线程绑定消息队列发送消息并处理消息
	private Handler handler = new UIHandler();
	
	private final class UIHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case PROCESSING://下载时
				int size = msg.getData().getInt("size"); // 从消息中获取已经下载的数据长度
				
				progressBar.setProgress(size); // 设置进度条的进度
				
				float num = (float)progressBar.getProgress()/(float)progressBar.getMax();// 计算已经下载的百分比， 此处需要转换为浮点数计算
				
				int result =(int )(num*100);// 把获取的浮点数计算结构转化为整数
				
				resultView.setText(result+"%"); 
				
				if(progressBar.getProgress()== progressBar.getMax()){
					//下载完成时
					Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_LONG).show();
				}
				break;
			case FAILURE:
				Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        pathText=(EditText)this.findViewById(R.id.path);
        
        resultView =(TextView)this.findViewById(R.id.resultView);
        
        downloadButton =(Button)this.findViewById(R.id.downloadbutton);
        
        stopbutton =(Button)this.findViewById(R.id.stopbutton);
        
        progressBar =(ProgressBar)this.findViewById(R.id.progressBar);
        
        ButtonClickListener listener = new ButtonClickListener();
        
        downloadButton.setOnClickListener(listener);
        
        stopbutton.setOnClickListener(listener);
    }


    private final class ButtonClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.downloadbutton:
				String path = pathText.getText().toString();
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					File saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
					
					download(path , saveDir);
				}else{
					Toast.makeText(getApplicationContext(), "sd 卡不存在", Toast.LENGTH_LONG).show();
				}
				
				downloadButton.setEnabled(false);
				stopbutton.setEnabled(true);
				
				break;
			case R.id.stopbutton:
				exit(); // 停止下载
				downloadButton.setEnabled(true);
				stopbutton.setEnabled(false);
				break;
			}
			
		}
    	
    }
    
    private DownloadTask task ;//声明下载执行者
    
    private final class DownloadTask implements Runnable{
    	private String path ; 
    	
    	private File saveDir ;
    	
    	private FileDownloader loader ;
    	
    	public DownloadTask(String path , File saveDir){
    		this.path = path ;
    		this.saveDir = saveDir ;
    	}
    	
    	public void exit(){
    		if(loader !=null){
    			loader.exit();
    		}
    	}
    	
    	
    	DownloadProgressListener downloadProgressListener = new DownloadProgressListener(){

			@Override
			public void onDownloadSize(int size) {
				Message msg = new Message();
				msg.what = PROCESSING ;
				msg.getData().putInt("size", size); 
				
				handler.sendMessage(msg);
				
			}
    		
    	};
    	
    	
		@Override
		public void run() {
			try{
				loader = new FileDownloader(getApplicationContext(), path ,saveDir , 3);
				
				progressBar.setMax(loader.getFileSize());
				
				loader.download(downloadProgressListener);
			}catch(Exception e){
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(FAILURE));
			}
			
		}
    	
    }
    
    
    public void exit(){
    	if(null != task){
    		task.exit();
    	}
    }
    
    private void download(String path ,File saveDir){
    	task = new DownloadTask(path , saveDir);
    	new Thread(task).start();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
