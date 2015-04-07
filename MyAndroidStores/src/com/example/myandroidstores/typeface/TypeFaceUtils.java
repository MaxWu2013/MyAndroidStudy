package com.example.myandroidstores.typeface;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

public class TypeFaceUtils {
	
	private static Typeface def_characters;
	/**
	 * 通过 TextView 设置 TextView 的显示字体
	 * @param v
	 */
	public static void applyTypeface(TextView v){
		if(v.getTypeface() == null){
			v.setTypeface(getDefaultTypeFace(v.getContext()));
			return;
		}
		switch(v.getTypeface().getStyle()){
		case Typeface.BOLD:
			v.setTypeface(getDefaultTypeFace(v.getContext()));
			v.getPaint().setFakeBoldText(true);
			break;
		case Typeface.NORMAL:
			v.setTypeface(getDefaultTypeFace(v.getContext()));
			break;
		case Typeface.ITALIC:
			v.setTypeface(getDefaultTypeFace(v.getContext()));
			v.getPaint().setTextSkewX(-0.25f);
			break;
		case Typeface.BOLD_ITALIC:
			v.setTypeface(getDefaultTypeFace(v.getContext()));
			v.getPaint().setFakeBoldText(true);
			v.getPaint().setTextSkewX(-0.25f);
			break;
		}
		
	}
	/**
	 * 同过 TextView 的 Paint 设置 TextView 的显示的字体
	 * @param context
	 * @param p
	 */
	public static void applyTypeface(Context context, Paint p){
		if(p.getTypeface() == null){
			p.setTypeface(getDefaultTypeFace(context));
			return;
		}
		switch(p.getTypeface().getStyle()){
		case Typeface.BOLD:
			p.setTypeface(getDefaultTypeFace(context));
			p.setFakeBoldText(true);
			break;
		case Typeface.NORMAL:
			p.setTypeface(getDefaultTypeFace(context));
			break;
		case Typeface.ITALIC:
			p.setTypeface(getDefaultTypeFace(context));
			p.setTextSkewX(-0.25f);
			break;
		case Typeface.BOLD_ITALIC:
			p.setTypeface(getDefaultTypeFace(context));
			p.setFakeBoldText(true);
			p.setTextSkewX(-0.25f);
			break;
		}
	}
	
	/**
	 * 获取默认默认字库
	 * @param context
	 * @return
	 */
	public synchronized static Typeface getDefaultTypeFace(Context context){
		if(def_characters == null)
			def_characters = loadTtfFileFromAsset(context.getAssets(), "FZY3JW.TTF");
		return def_characters;
	}

	/**
	 * 从 asset 文件夹里加载 ttf 字库文件 ， 返回字符
	 * @param am
	 * @param path
	 * @return
	 */
	private static Typeface loadTtfFileFromAsset(AssetManager am, String path){
		try{
			Typeface tf = Typeface.createFromAsset(am, path);
			return tf;
		}catch(Exception e){
			e.printStackTrace();
			return Typeface.DEFAULT;
		}
	}
}
