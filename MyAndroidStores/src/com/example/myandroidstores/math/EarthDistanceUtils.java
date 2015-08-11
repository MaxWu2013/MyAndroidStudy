package com.example.myandroidstores.math;

/**
 * 计算地球上两点之间的距离
 * User: MaxWu
 * Date: 2015-03-17
 * Time: 19:50
 */
public class EarthDistanceUtils {

    public static double  EARTH_RADIUS = 6378137.0;    //单位M
    public static double PI = Math.PI;

    public static double getRad(double d){
        return d*PI/180.0;
    }

    /**
     * 计算地球上两点之间的距离
     * caculate the great circle distance
     * @param {Object} lat1
     * @param {Object} lng1
     * @param {Object} lat2
     * @param {Object} lng2
     */
    public static double getGreatCircleDistance(double lat1,double lng1,double lat2,double lng2){
        double radLat1 = getRad(lat1);
        double radLat2 = getRad(lat2);

        double a = radLat1 - radLat2;
        double b = getRad(lng1) - getRad(lng2);

        double s = 2*Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s*EARTH_RADIUS;
        s = Math.round(s*10000)/10000.0;

        return s;
    }
}
