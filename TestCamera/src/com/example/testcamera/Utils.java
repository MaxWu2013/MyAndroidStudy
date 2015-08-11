package com.example.testcamera;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.util.Log;

public class Utils {

	
    public static boolean addImage(String filename , String path , byte[] jpegdata) {
        boolean state = false;
    	OutputStream outputStream = null;
        try {
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            File file = new File(path, filename);
            //如果原文件存在，默认会覆盖原文件内容
            outputStream = new FileOutputStream(file ,false);
            outputStream.write(jpegdata);
            state = true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeSilently(outputStream);
        }
        return state;

    }
    
    public static void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }
    
    public static String createName(long dateTaken) {
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "IMG_yyyyMMdd_HHmmss");

        return dateFormat.format(date);
    }
}
