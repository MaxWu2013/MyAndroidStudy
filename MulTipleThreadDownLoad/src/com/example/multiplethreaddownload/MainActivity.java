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

	private static final int PROCESSING =1 ;// ��������ʵʱ���ݴ��� MESSAGE ��־
	
	private static final int FAILURE=-1 ;//����ʧ��ʱ��MESSAGE ��־
	
	private EditText pathText ;			//���������ı���
	private TextView resultView ;		//�԰�װ������ʾ�ٷֱ��ı���
	
	private Button downloadButton; 		// ���ذ�ť�� ���Դ��������¼�
	private Button stopbutton ; 		//ֹͣ���ذ�ť�� ����ֹͣ����
	private ProgressBar progressBar ; // ���ؽ������� ʵʱͼ�λ�����ʾ������Ϣ
	
	//handler ������������򴴽�handler�������ڵ��̰߳���Ϣ���з�����Ϣ��������Ϣ
	private Handler handler = new UIHandler();
	
	private final class UIHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case PROCESSING://����ʱ
				int size = msg.getData().getInt("size"); // ����Ϣ�л�ȡ�Ѿ����ص����ݳ���
				
				progressBar.setProgress(size); // ���ý������Ľ���
				
				float num = (float)progressBar.getProgress()/(float)progressBar.getMax();// �����Ѿ����صİٷֱȣ� �˴���Ҫת��Ϊ����������
				
				int result =(int )(num*100);// �ѻ�ȡ�ĸ���������ṹת��Ϊ����
				
				resultView.setText(result+"%"); 
				
				if(progressBar.getProgress()== progressBar.getMax()){
					//�������ʱ
					Toast.makeText(getApplicationContext(), "�������", Toast.LENGTH_LONG).show();
				}
				break;
			case FAILURE:
				Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_LONG).show();
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
					Toast.makeText(getApplicationContext(), "sd ��������", Toast.LENGTH_LONG).show();
				}
				
				downloadButton.setEnabled(false);
				stopbutton.setEnabled(true);
				
				break;
			case R.id.stopbutton:
				exit(); // ֹͣ����
				downloadButton.setEnabled(true);
				stopbutton.setEnabled(false);
				break;
			}
			
		}
    	
    }
    
    private DownloadTask task ;//��������ִ����
    
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
