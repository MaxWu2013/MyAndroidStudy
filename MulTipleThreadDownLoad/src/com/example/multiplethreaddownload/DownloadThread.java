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
	
	private int block ; //每条线程下载的大小
	
	private int threadId = -1 ;// 初始化线程id
	
	private int downloadedLength ; // 该线程已经下载的数据长度
	
	private boolean finished = false ;
	
	private FileDownloader downloader  ;// 文件下载器
	
	
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
			//未下载完成
			try{
				HttpURLConnection http = (HttpURLConnection) downUrl.openConnection(); // 开启HttpURLConnection 连接
				
				http.setConnectTimeout(5*1000); // 设置连接超时时间为5秒
				http.setRequestMethod("GET");	//设置请求的方式 get
				http.setRequestProperty("Accept" , "image/gif , image/jpeg, image/pjpeg , " +
						"application/x-shockwave-flash, application/xaml+xml , " +
						"applicaion/vnd.ms-xpsdocument, application/x-ms-xbap, " +
						"applicaion/vnd.ms-excel , application/vnd.ms-powerpoint,application/msword," +
						" */*");//设置客户端接受的返回数据类型
				
				http.setRequestProperty("Accept-Language", "zh-CN");//设置客户端使用的语言为中文
				
				http.setRequestProperty("Referer", downUrl.toString());//设置请求的来源， 便于访问来源进行统计
				
				http.setRequestProperty("Charset", "UTF-8");//设置通讯编码为UTF-8
				
				//开始位置
				int startPos = block*(threadId-1)+downloadedLength;
				//结束位置
				int endPos = block*threadId-1 ; 
				//设置获取试题数据的范围， 如果超过了实体数据的大小会自动返回实际的数据的大小
				http.setRequestProperty("Range", "bytes="+startPos+"-"+endPos);
				//设置客户端用户代理
				http.setRequestProperty("User_Agent", "Mozilla/4.0(compatible; MSIE 8.0; " +
						"Window NT 5.2; Trident/4.0; .NET clr 1.1.4322; .NET CLR 2.0.50727; " +
						".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
				//设置长连接
				http.setRequestProperty("Connection", "Keep-Alive");
				
				InputStream inStream = http.getInputStream(); // 获取远程连接的输入流
				
				byte[] buffer = new byte[1024];// 设置本地数据缓存大小为1MB
				
				int offset =0 ; // 设置每次读取的数据量
				
				print("Thread"+this.threadId+"starts to download from position "+startPos);
				//if the file does not already exist then an attempt will be made to create it 
				//and it require that every update to the file's content 
				//be written synchronously to the underlying storage device
				RandomAccessFile threadFile = new RandomAccessFile(this.saveFile,"rwd") ;
				
				threadFile.seek(startPos);// 文件指针指向开始下载的位置
				
				while(!downloader.getExited() && (offset=inStream.read(buffer,0,1024))!=-1){
					//用户没有要求停止下载， 同时没有到达请求数据的末尾时候会一直循环读取数据
					threadFile.write(buffer , 0 , offset);// 直接把数据写到文件中
					
					downloadedLength +=offset ; //把心下载的数据长度加入下载长度中
					
					//把该线程已经下载的数据长度更新到数据库和内存哈希表zhong
					downloader.update(this.threadId, downloadedLength);
					
					downloader.append(offset); // 把下载的数据长度加入到已经下载的数据总长度中
					
					
				}
				//线程下载数据完毕或者下载被用户停止
				// Closees this random access file stream and releases any system resources associated with the stream
				threadFile.close();
				inStream.close(); //Concrete implementations of this class should free any resources during close
				
				if(downloader.getExited()){
					print("Thread"+this.threadId+" has been paused");
				}else{
					print("Thread"+this.threadId+" download finish");
				}
				this.finished = true;// 设置下载完成表示为true ， 无论下载完成还是用户主动中断下载
				
			}catch(Exception e){
				this.downloadedLength=-1 ; // 设置该线程已经下载的长度为-1 
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
