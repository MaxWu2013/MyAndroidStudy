package com.example.testcamera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * 文件操作工具包
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class FileUtils {
    /**
	 * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * 
	 * @param context
	 * @param msg
	 */
	public static void write(Context context, String fileName, String content) {
		if( null == content){
			content="";
		}
		try{
			
			FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			fos.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String read(Context context, String fileName) {
		try{
			FileInputStream in = context.openFileInput(fileName);
			return readInStream(in);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * InputStream 转 String 
	 * @param inStream
	 * @return
	 */
	public static String readInStream(InputStream inStream) {
		try{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length =-1;
			while((length=inStream.read(buffer)) !=-1){
				outStream.write(buffer, 0, length);
			}
			
			outStream.close();
			inStream.close();
			
			return outStream.toString();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 新建文件
	 * @param folderPath
	 * @param fileName
	 * @return
	 */
	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		
		return new File(folderPath , fileName);
	}

	/**
	 * android 向手机写文件
	 * 
	 * @param buffer
	 * @param folder 保存在sd卡的文件夹名称， 无需填写sd卡路径
	 * @param fileName 
	 * @return
	 */
	public static boolean writeFile(byte[] buffer, String folder,
			String fileName) {
		boolean writeSucc = false;
		
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		
		String folderPath="";
		if(sdCardExist){
			folderPath = Environment.getExternalStorageDirectory()
					+File.separator+folder+File.separator;
		}else{
			writeSucc = false;
		}
		
		File fileDir = new File(folderPath);
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
		
		File file = new File(folderPath+fileName);
		FileOutputStream out = null ;
		
		try{
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				out.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return writeSucc ;
	}

	/**
	 * 根据文件绝对路径获取文件名,包含扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if(StringUtils.isEmpty(filePath)){
			return "";
		}
		return filePath.substring(filePath.lastIndexOf(File.separator)+1);
	}

	/**
	 * 根据文件的绝对路径获取文件名但不包含扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if(StringUtils.isEmpty(filePath)){
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator)+1, point);
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if(StringUtils.isEmpty(fileName)){
			return "";
		}
		int point = fileName.lastIndexOf('.');
		return fileName.substring(point+1);
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size =0 ;
		File file = new File(filePath);
		if(null !=file && file.exists()){
			size = file.length();
		}
		return size;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param size ：long  文件字节数
	 *            字节
	 * @return   返回友好型文件字节数描述
	 */
	public static String getFileSize(long size) {
		if(size<=0){
			return "0" ;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		
		float temp = (float)size/1024 ;
		if(temp>=1024){
			return df.format(temp/1024)+"M";
		}else{
			return df.format(temp)+"K";
		}
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString ="";
		if(fileS < 1024){
			fileSizeString = df.format((double)fileS)+"B";
		}else if(fileS<1048576){
			fileSizeString = df.format((double)fileS/1024)+"KB";
		}else if(fileS<1073741842){
			fileSizeString=df.format((double)fileS/1048576)+"MB";
		}else{
			fileSizeString = df.format((double)fileS/1073741842)+"G";
		}
		return fileSizeString ;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if(null == dir){
			return 0;
		}
		if(!dir.isDirectory()){
			return 0 ;
		}
		long dirSize = 0 ;
		File[] files = dir.listFiles();
		
		for(File file : files){
			if(file.isFile()){
				dirSize +=file.length();
			}else if(file.isDirectory()){
				dirSize +=file.length();
				dirSize +=getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize ;
	}

	/**
	 * 获取目录文件个数
	 * 
	 * @param dir 目录文件路径
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0 ;
		File[] files = dir.listFiles();
		count = files.length;   //计算文件个数，包括文件夹
		for(File file : files){
			if(file.isDirectory()){
				count +=getFileList(file);  //递归调用
				count-- ;    // 减掉文件夹个数
			}
		}
		return count ;
	}
	/**
	 *  InputStream  转  byte[]
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch ;
		while ((ch=in.read())!=-1){
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

	/**
	 * 检查路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkFilePathExists(String path) {
		return new File(path).exists();
	}

	/**
	 * 计算SD卡的剩余空间
	 * 
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0 ;
		
		if(status.equals(Environment.MEDIA_MOUNTED)){
			try{
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				
				freeSpace = availableBlocks*blockSize/1024 ;
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			return -1;
		}
		
		return freeSpace ;
	}


	/**
	 * 检查是否安装SD卡
	 * 
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status = false;
		
		if(sDCardStatus.equals(Environment.MEDIA_MOUNTED)){
			status = true;
		}else{
			status = false;
		}
		
		return status ;
	}
	
	/**
	 * 检查是否安装外置的SD卡
	 * 
	 * @return
	 */
	public static boolean checkExternalSDExists() {
		Map<String , String> evn = System.getenv();
		
		return evn.containsKey("SECONDARY_STORAGE");
	}

	/**
	 * 删除目录(包括：目录里的所有文件)
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteDirectory(String fileName) {
		boolean status = false;
		
		SecurityManager checker = new SecurityManager();
		
		if(!StringUtils.isEmpty(fileName)){
			
			File newPath = new File(fileName);
			
			if(newPath.exists()){
				try{
					checker.checkDelete(newPath.toString());
				}catch(SecurityException e){
					return false;
				}
				
				if(newPath.isDirectory()){
					File[] listfile = newPath.listFiles();
					for(File deletedFile : listfile){
						if(deletedFile.isDirectory()){
							deleteDirectory(deletedFile.getPath());  //递归删除子文件
						}
						try{
							deletedFile.delete();
						}catch(SecurityException e){
							e.printStackTrace();
						}
					}
				}
				try{
					newPath.delete();
				}catch(SecurityException e){
					e.printStackTrace();
				}
				
				status = true;
			}
			
		}
		return status ;

	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		boolean status = false;
		SecurityManager checker = new SecurityManager();
		if(!StringUtils.isEmpty(fileName)){
			File newPath = new File(fileName);
			if(newPath.isFile()){
				try{
					checker.checkDelete(fileName);
					newPath.delete();
					status =  true;
				}catch(SecurityException se){
					se.printStackTrace();
				}
			}
		}
		return status ;
	}

	/**
	 * 删除空目录
	 * 
	 * 返回 0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误,4不存在该目录
	 * 
	 * @return
	 */
	public static int deleteBlankPath(String path) {
		File f = new File(path);
		if(!f.exists()){
			return 4;
		}
		
		if(!f.isDirectory()){
			return 4;
		}
		
		if(!f.canWrite()){
			return 1;
		}
		if(f.list() !=null && f.list().length>0){
			return 2;
		}
		
		try{
			f.delete();
			return 0 ;
		}catch(Exception e){
			e.printStackTrace();
			return 3;
		}
	}

	/**
	 * 重命名
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public static boolean reNamePath(String oldName, String newName) {
		File f = new File(oldName);
		if(!f.exists()){
			return false;
		}
		try{
			return f.renameTo(new File(newName));
		}catch(SecurityException e){
			e.printStackTrace();
			return false;
		}
	}



	/**
	 * 获取SD卡的根目录
	 * 
	 * @return
	 */
	public static String getSDRoot() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	/**
	 * 获取手机外置SD卡的根目录
	 * 
	 * @return
	 */
	public static String getExternalSDRoot() {
		Map<String , String>evn = System.getenv();
		
		return evn.get("SECONDARY_STORAGE");
	}

	/**
	 * 列出root目录下所有子 文件夹
	 * 
	 * @param path
	 * @return 绝对路径
	 */
	public static List<String> listPath(String root) {
		List<String> allDir = new ArrayList<String>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		
		checker.checkRead(root);
		// 过滤掉以 . 开始的文件夹
		if(path.isDirectory()){
			for(File f : path.listFiles()){
				if(f.isDirectory() && !f.getName().startsWith(".")){
					allDir.add(f.getAbsolutePath());
				}
			}
		}
		return allDir;
	}
	
	/**
	 * 获取一个文件夹下的所有子文件 , 不包括文件夹
	 * @param root
	 * @return
	 */
	public static List<File> listPathFiles(String root) {
		List<File> allFiles = new ArrayList<File>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		
		checker.checkRead(root);
		File[] files = path.listFiles();
		for(File f: files){
			if(f.isFile()){
				allFiles.add(f);
			}
		}
		return allFiles;
	}

	public enum PathStatus {
		SUCCESS, EXITS, ERROR
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 */
	public static PathStatus createPath(String newPath) {
		File path = new File(newPath);
		if(path.exists()){
			return PathStatus.EXITS;
		}
		if(path.mkdir()){
			return PathStatus.SUCCESS;
		}else{
			return PathStatus.ERROR;
		}
	}

	/**
	 * 截取路径名
	 * 
	 * @return
	 */
	public static String getPathName(String absolutePath) {
		int start = absolutePath.lastIndexOf(File.separator+1);
		int end = absolutePath.length();
		return absolutePath.substring(start, end);
	}
	
	/**
	 * 获取应用程序缓存文件夹下的指定目录
	 * @param context
	 * @param dir
	 * @return
	 */
	public static String getAppCache(Context context, String dir) {
		String savePath = context.getCacheDir().getAbsolutePath()+
				File.pathSeparator+
				dir+
				File.pathSeparator ;
		
		File saveDir = new File(savePath);
		if(!saveDir.exists()){
			saveDir.mkdirs();
		}
		return savePath ;
	}
}