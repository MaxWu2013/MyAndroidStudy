package com.example.testcamera;

import java.util.Date;
import java.util.TimeZone;

/**
 * 不同时区对应的时间处理工具类
 * 
 * @author HuangWenwei
 * 
 * @date 2014年7月31日
 */
public class TimeZoneUtils {
	
	/**
	 * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
	 * 
	 * @return
	 */
	public static boolean isInEasternEightZones(){
		boolean defaultValue = true ;
		if(TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08")){
			defaultValue = true;
		}else{
			defaultValue = false;
		}
		return defaultValue ;
	}
	/**
	 * 根据不同时区，转换时间 2014年7月31日
	 * 
	 * @param oldDate
	 * @param oldZone 
	 * @param new Zone
	 * @return  new date
	 */
	public static Date transformTime(Date oldDate , TimeZone oldZone , TimeZone newZone){
		Date finalDate = null ;
		
		if(null != oldDate){
			int timeOffset = oldZone.getOffset(oldDate.getTime()) -
					newZone.getOffset(oldDate.getTime());
			
			finalDate = new Date(oldDate.getTime() - timeOffset);
		}
		
		return finalDate ;
	}
}
