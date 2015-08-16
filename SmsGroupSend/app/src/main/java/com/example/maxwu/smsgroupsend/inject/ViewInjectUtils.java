package com.example.maxwu.smsgroupsend.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.maxwu.smsgroupsend.inject.interf.ViewInject;


public class ViewInjectUtils {



    /**
     *
     * findViewbyId
     * 为View控件注入Id
     * @param activity
     */
	public static void injectViews(Activity activity){
        View rootView = activity.getWindow().getDecorView();
		Class<? extends Activity> clazz = activity.getClass();
		Field[] fields =clazz.getDeclaredFields();
		
		for(Field field : fields){

			ViewInject viewInjectAnnotation =
					field.getAnnotation(ViewInject.class);
			
			if(null != viewInjectAnnotation){
				int viewId = viewInjectAnnotation.value();
				if(-1 != viewId){
					
					//初始化View
					try{

						Object resView = rootView.findViewById(viewId);
						field.setAccessible(true);
						field.set(activity, resView);
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
		}
	}

    /**
     * 为View控件注入Id
     *findViewbyId
     */
    public static void injectViews(Fragment fragment , View rootView){
        Class<? extends Fragment> clazz = fragment.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for(Field field :fields){
            ViewInject viewInjectAnnotation =
                    field.getAnnotation(ViewInject.class);

            if(null !=viewInjectAnnotation){
                int viewId = viewInjectAnnotation.value();
                if(-1 != viewId){
                    //初始化view
                    try{

                        Object resView = null;

                        if(rootView.getId()> 0 && viewId == rootView.getId()){
                            resView = rootView;
                        }else{
                            resView = rootView.findViewById(viewId);
                        }

                        field.setAccessible(true);
                        field.set(fragment,resView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**View Id 注入， 这个接口相对于 injectViews(Fragment fragment , View rootView) 的不同之处
     * 是，添加了一个强制把fragment 转类型的 变量 forceClass , 这样能把fragment 看成他的父类来进行id注入
     *
     * @param forceClass
     * @param fragment
     * @param rootView
     */
    public static void injectViews(Class<?> forceClass ,Fragment fragment , View rootView){

        Class clazz = forceClass ;
        Field[] fields = clazz.getDeclaredFields();

        for(Field field :fields){
            ViewInject viewInjectAnnotation =
                    field.getAnnotation(ViewInject.class);

            if(null !=viewInjectAnnotation){
                int viewId = viewInjectAnnotation.value();
                if(-1 != viewId){
                    //初始化view
                    try{

                        Object resView = null;

                        if(rootView.getId()> 0 && viewId == rootView.getId()){
                            resView = rootView;
                        }else{
                            resView = rootView.findViewById(viewId);
                        }

                        field.setAccessible(true);
                        field.set(fragment,resView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
