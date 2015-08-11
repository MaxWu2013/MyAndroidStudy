package com.example.androidiocframework.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.androidiocframework.annotation.interf.ContentView;
import com.example.androidiocframework.annotation.interf.EventBase;
import com.example.androidiocframework.annotation.interf.ViewInject;

public class ViewInjectUtils {
	/* setContentView(int)*/
	private static final String METHOD_SET_CONTENTVIEW="setContentView";
//	findViewById(int)
	private static final String METHOD_FIND_VIEW_BY_ID="findViewById";
	
	/*setContentView*/
	private static void injectContentView(Activity activity){
		
		Class<? extends Activity>clazz = activity.getClass();
		
		ContentView contentView = clazz.getAnnotation(ContentView.class);
		
		if(null != contentView){
			int contentViewLayoutId = contentView.value();
			try{
				Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW,
						int.class);
				method.setAccessible(true);
				method.invoke(activity, contentViewLayoutId);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/*findViewbyId*/
	private static void injectViews(Activity activity){
		Class<? extends Activity> clazz = activity.getClass();
		Field[] fields =clazz.getDeclaredFields();
		
		for(Field field : fields){
			ViewInject viewInjectAnnotation = 
					field.getAnnotation(ViewInject.class);
			
			if(null != viewInjectAnnotation){
				int viewId = viewInjectAnnotation.value();
				if(-1 != viewId){
					Log.i("Max","viewId="+viewId);
					
					//初始化View
					try{
						Method method = 
								clazz.getMethod(METHOD_FIND_VIEW_BY_ID,
										int.class);
						
						Object resView = method.invoke(activity, viewId);
						field.setAccessible(true);
						field.set(activity, resView);
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	private static void injectEvent(Activity activity){
		Class<? extends Activity> clazz = activity.getClass();
		Method[] methods = clazz.getMethods();
		
		//search all methods
		for(Method method : methods){
			Annotation[] annotations = method.getAnnotations();
			//get annotations
			for(Annotation annotation : annotations){
				Class<? extends Annotation> annotationType =
						annotation.annotationType();
				
				//get annotaiton type == Eventbase  注意： 这个是注解上面还有注解
				EventBase eventBaseAnnotation = 
						annotationType.getAnnotation(EventBase.class);
				
				if(null != eventBaseAnnotation){
					// get variables
					String listenerSetter = 
							eventBaseAnnotation.listenerSetter();
					Class<?> listenerType = 
							eventBaseAnnotation.listenerType();
					String methodName = 
							eventBaseAnnotation.methodName();
					
					try{
						//get values in Annotation of OnClick
						Method aMethod = 
								annotationType.getDeclaredMethod("value");
						//get all ids
						int[] viewIds =(int[])aMethod.invoke(annotation,null);
						
						//make dynamic proxy through InvocationHandler
						DynamicHandler handler = new DynamicHandler(activity);
						handler.addMethod(methodName, method);
						Object listener = Proxy.newProxyInstance(
								listenerType.getClassLoader(),
								new Class<?>[]{ listenerType },handler);

						//遍历所有的View,设置事件
						for(int viewId: viewIds){
							View view = activity.findViewById(viewId);
							Method setEventListenerMethod =
									view.getClass().getMethod(listenerSetter, listenerType);
							
							setEventListenerMethod.invoke(view, listener);
						}
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	public static void inject(Activity activity){
		injectContentView(activity);
		injectViews(activity);
		injectEvent(activity);
	}
}
