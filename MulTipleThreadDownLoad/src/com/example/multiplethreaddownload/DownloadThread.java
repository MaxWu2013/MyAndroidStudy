package com.example.multiplethreaddownload;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class DownloadThread extends Thread{
	private static final String TAG="DownloadThread";
	
	private File saveFile ; 
	
	private URL downUrl ;
	
	private int block ; //ÿ���߳����صĴ�С
	
	private int threadId = -1 ;// ��ʼ���߳�id
	
	private int downloadedLength ; // ���߳��Ѿ����ص����ݳ���
	
	private boolean finished = false ;
	
	private FileDownloader downloader  ;// �ļ�������
	
	
	public DownloadThread(FileDownloader downloader , URL downUrl , File saveFile , int block , int downloadedLength , int threadId){
		this.downUrl = downUrl ;
		this.saveFile = saveFile ;
		this.block = block ;
		this.downloader = downloader ;
		this.threadId = threadId; 
		this.downloadedLength = downloadedLength ;
	}


	@Override
	public void run() {
		if(downloadedLength < block ){
			//δ�������
			try{
				HttpURLConnection http = (HttpURLConnection) downUrl.openConnection(); // ����HttpURLConnection ����
				
				http.setConnectTimeout(5*1000); // �������ӳ�ʱʱ��Ϊ5��
				http.setRequestMethod("GET");	//��������ķ�ʽ get
				http.setRequestProperty("Accept" , "image/gif , image/jpeg, image/pjpeg , " +
						"application/x-shockwave-flash, application/xaml+xml , " +
						"applicaion/vnd.ms-xpsdocument, application/x-ms-xbap, " +
						"applicaion/vnd.ms-excel , application/vnd.ms-powerpoint,application/msword," +
						" */*");//���ÿͻ��˽��ܵķ�����������
				
				http.setRequestProperty("Accept-Language", "zh-CN");//���ÿͻ���ʹ�õ�����Ϊ����
				
				http.setRequestProperty("Referer", downUrl.toString());//�����������Դ�� ���ڷ�����Դ����ͳ��
				
				http.setRequestProperty("Charset", "UTF-8");//����ͨѶ����ΪUTF-8
				
				//��ʼλ��
				int startPos = block*(threadId-1)+downloadedLength;
				//����λ��
				int endPos = block*threadId-1 ; 
				//���û�ȡ�������ݵķ�Χ�� ���������ʵ�����ݵĴ�С���Զ�����ʵ�ʵ����ݵĴ�С
				http.setRequestProperty("Range", "bytes="+startPos+"-"+endPos);
				//���ÿͻ����û�����
				http.setRequestProperty("User_Agent", "Mozilla/4.0(compatible; MSIE 8.0; " +
						"Window NT 5.2; Trident/4.0; .NET clr 1.1.4322; .NET CLR 2.0.50727; " +
						".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
				//���ó�����
				http.setRequestProperty("Connection", "Keep-Alive");
				
				InputStream inStream = http.getInputStream(); // ��ȡԶ�����ӵ�������
				
				byte[] buffer = new byte[1024];// ���ñ������ݻ����СΪ1MB
				
				int offset =0 ; // ����ÿ�ζ�ȡ��������
				
				print("Thread"+this.threadId+"starts to download from position "+startPos);
				//if the file does not already exist then an attempt will be made to create it 
				//and it require that every update to the file's content 
				//be written synchronously to the underlying storage device
				RandomAccessFile threadFile = new RandomAccessFile(this.saveFile,"rwd") ;
				
				threadFile.seek(startPos);// �ļ�ָ��ָ��ʼ���ص�λ��
				
				while(!downloader.getExited() && (offset=inStream.read(buffer,0,1024))!=-1){
					//�û�û��Ҫ��ֹͣ���أ� ͬʱû�е����������ݵ�ĩβʱ���һֱѭ����ȡ����
					threadFile.write(buffer , 0 , offset);// ֱ�Ӱ�����д���ļ���
					
					downloadedLength +=offset ; //�������ص����ݳ��ȼ������س�����
					
					//�Ѹ��߳��Ѿ����ص����ݳ��ȸ��µ����ݿ���ڴ��ϣ��zhong
					downloader.update(this.threadId, downloadedLength);
					
					downloader.append(offset); // �����ص����ݳ��ȼ��뵽�Ѿ����ص������ܳ�����
					
					
				}
				//�߳�����������ϻ������ر��û�ֹͣ
				// Closees this random access file stream and releases any system resources associated with the stream
				threadFile.close();
				inStream.close(); //Concrete implementations of this class should free any resources during close
				
				if(downloader.getExited()){
					print("Thread"+this.threadId+" has been paused");
				}else{
					print("Thread"+this.threadId+" download finish");
				}
				this.finished = true;// ����������ɱ�ʾΪtrue �� ����������ɻ����û������ж�����
				
			}catch(Exception e){
				this.downloadedLength=-1 ; // ���ø��߳��Ѿ����صĳ���Ϊ-1 
				print("Thread"+this.threadId+" :"+e.toString());
			}
		}
		//super.run();
	}
	
	private static void print(String msg){
		Log.i(TAG, msg);
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public long getDownloadedLength(){
		return downloadedLength;
	}
}
