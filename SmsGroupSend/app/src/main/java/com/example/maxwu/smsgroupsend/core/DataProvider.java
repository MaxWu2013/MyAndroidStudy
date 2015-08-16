package com.example.maxwu.smsgroupsend.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.maxwu.smsgroupsend.Logger;
import com.example.maxwu.smsgroupsend.MainActivity;
import com.example.maxwu.smsgroupsend.MyApplication;
import com.example.maxwu.smsgroupsend.R;
import com.example.maxwu.smsgroupsend.http.RequestParams;
import com.example.maxwu.smsgroupsend.http.cache.JSONApiHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * json数据处理类
 * 
 * @author ASRock
 * 
 */
public class DataProvider {

	Context context;
	private MyApplication application;
	private int postType = 1;//1: 2：注册时未登录但需要token和key
//	public boolean ifGetMore = false;
	public boolean ifNeighbor = false;
	public boolean ifShow = true;
	public Dialog loadingDialog;



	/**************inner class ********************************************/

	public OnKeyListener onDialogKeyListener = new OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
				if(loadingDialog.isShowing())
					loadingDialog.dismiss();
			}
			return false;
		}
	};

	public interface ResponseHandlerT<T> {
		public void onResponse(T result);
	}
	/******************************************************************/



	public DataProvider(Context context) {
		this.context = context;
		application = (MyApplication) context.getApplicationContext();
		loadingDialog = new Dialog(context, R.style.LoadDialog);
		loadingDialog.setContentView(R.layout.loading_dialog);
		loadingDialog.setOnKeyListener(onDialogKeyListener);
		loadingDialog.setCancelable(true);
	}

	/**
	 * set loading dialog's style
	 * @param text
	 */
	public void setStyleText(String text) {
		// TODO Auto-generated method stub
		if(loadingDialog!=null)loadingDialog = null;
		loadingDialog = new Dialog(context, R.style.LoadDialogText);
		loadingDialog.setContentView(R.layout.loading_dialog_text);
		loadingDialog.setOnKeyListener(onDialogKeyListener);
		loadingDialog.setCancelable(true);
		TextView textView = (TextView) loadingDialog.findViewById(R.id.loading_text);
		textView.setText(text);
	}

	/**
	 * set loading dialog's style
	 */
	public void setStyleNoText() {
		if(loadingDialog!=null)loadingDialog = null;
		loadingDialog = new Dialog(context, R.style.LoadDialog);
		loadingDialog.setContentView(R.layout.loading_dialog);
		loadingDialog.setOnKeyListener(onDialogKeyListener);
		loadingDialog.setCancelable(true);
	}
	
	public void setIfNeighbor(boolean flag) {
		// TODO Auto-generated method stub
		this.ifNeighbor = flag;
	}
	
	public void setIfShow(boolean ifShow) {
		this.ifShow = ifShow;
	}

	public int getPostType() {
		return postType;
	}


	public void setPostType(int postType) {
		this.postType = postType;
	}


	public <E> void getData(JSONApiHelper.HttpType httpType,String url, RequestParams params,
			final Class<E> type, final ResponseHandlerT<E> handler) {
		String httpUrl = "";
		httpUrl += url;
		Logger.d("getData-httpUrl:" + httpUrl);
		JSONApiHelper.CallbackType callbackType = null;
		callbackType = (httpType== JSONApiHelper.HttpType.Get)? JSONApiHelper.CallbackType.ForceUpdate: JSONApiHelper.CallbackType.NoCache;
		Header[] headers = authHead();
		JSONApiHelper.callJSONAPI(httpType,context, callbackType, httpUrl,headers,
				params, new JSONApiHelper.StringResponseListener() {
					@Override
					public void onResponse(String content) {
						Logger.d("getData-content:"+content);
						try {
							if(content.startsWith("ERROR:")){				
								/*if(context instanceof EnterActivity){
									application.setLogin(false);
									Intent intent = new Intent(context, MainActivity.class);
									context.startActivity(intent);
									((Activity) context).finish();
								}else{
									if(content.contains("401")){
										Toast.makeText(context, "您的登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
										userManage.loginOut();
										Intent intent = new Intent(context, LoginActivity.class);
										context.startActivity(intent);
										((Activity) context).finish();	
									}else{
										Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
									}
								}*/
														
							}else{
								Logger.d("Max strat-gson");
								Gson gson = new GsonBuilder().create();
                                Logger.d("Max strat-gson gson ");
								E result = null;
								result = gson.fromJson(content, type);
                                Logger.d("Max strat-gson result");
								if (handler != null){
                                    Logger.d("strat-gson handler != null");
                                    handler.onResponse(result);
                                }
								Logger.d("strat-gson-end");
							}		
						} catch (Exception e) {
//							e.printStackTrace();
                            Logger.w("e="+e);
                            /*if(context instanceof EnterActivity){
								application.setLogin(false);
								Intent intent = new Intent(context, MainActivity.class);
								context.startActivity(intent);
								((Activity) context).finish();
							}*/
						}finally{
							hideLoading();
						}
						
					}
				});
	}

	private void showLoading() {
		// TODO Auto-generated method stub
		try {
			if(ifShow){
				loadingDialog.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void hideLoading() {
		// TODO Auto-generated method stub
		if(loadingDialog.isShowing())
			loadingDialog.dismiss();
	}

	/**
	 * get请求
	 * 
	 * @param type
	 * @param handler
	 */
	public <E> void httpGetRequest(Object object, final Class<E> type,
			final ResponseHandlerT<E> handler) {
		 httpRequest(JSONApiHelper.HttpType.Get, object, type,handler);
	}
	
	/**
	 * post请求
	 * 
	 * @param type
	 * @param handler
	 */
	public <E> void httpPostRequest(Object object, final Class<E> type,
			final ResponseHandlerT<E> handler) {
		httpRequest(JSONApiHelper.HttpType.Post, object, type,handler);
	}
	
	/**
	 * 请求接口
	 * 
	 * @param type
	 * @param handler
	 */
	public <E> void httpRequest(JSONApiHelper.HttpType httpType,Object object, final Class<E> type,
			final ResponseHandlerT<E> handler) {
		showLoading();
		RequestParams params = new RequestParams();
		String httpUrl = "";
		try {
			Gson gson = new Gson();
			String str = gson.toJson(object);
			if (!TextUtils.isEmpty(str)) {
				JSONObject jsonObjet = new JSONObject(str);
				Iterator<?> it = jsonObjet.keys();
				while (it.hasNext()) {

					String key = it.next().toString();

					Object obj = jsonObjet.get(key);
					String value = "";
					if (obj != null) {
						value = String.valueOf(obj);
					}
//					Logger.d("httpRequest-key:"+key+";value:"+value);
					if (key.equals("requestUrl")) {
						httpUrl = value;
					} else {
						params.put(key, value);
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
//		if (Config.isTestData) {//开发时，跳过认证
//			params.put("isadmin", "1");
//			if(application.getUid()==null||application.getUid().equals("")){
//				params.put("admin_uid", "1");
//			}else{
//				params.put("admin_uid", application.getUid());
//			}
//			
//		}
		getData(httpType,httpUrl, params, type, handler);
	}


	public Header[] authHead() {
		// TODO Auto-generated method stub
		return assembHead(authMap());
	}

	public static Header[] assembHead(Map<String, String> headers) {
		Header[] allHeader = new BasicHeader[headers.size()];
		int i = 0;
		for (String str : headers.keySet()) {
			allHeader[i] = new BasicHeader(str, headers.get(str));
			i++;
		}
		return allHeader;
	}

	public Map<String, String> authMap() {
//		String tenNum = StringUtils.getTenNum();
		String tenNum = "4362478233";
		StringBuffer buffer = new StringBuffer();
		buffer.append("OAuth (");
/*		buffer.append("oauth_consumer_key=");
		buffer.append(Config.oauth_consumer_key);*/
		buffer.append(",oauth_nonce=");
		buffer.append(tenNum);
		buffer.append(",oauth_sign_type=");
		/*buffer.append(Config.oauth_sign_type);
		if(application.isLogin()&&postType==1){
			Logger.d("OAuth-go-1");
			buffer.append(",oauth_token=");
			buffer.append(application.getAuthTokenKey());
			buffer.append(",oauth_signature=");
			String authSignature="";
			try {
				authSignature = StringUtils.generateSignature("nonce="+tenNum, Config.consumer_secret, application.getAuthTokenSecret());
				application.setAuthSignature(authSignature);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			buffer.append(authSignature);
		}else if(postType==2){
			Logger.d("OAuth-go-2");
			buffer.append(",oauth_token=");
			buffer.append(application.getRegist_key());
			buffer.append(",oauth_signature=");
			String authSignature="";
			try {
				authSignature = StringUtils.generateSignature("nonce="+tenNum, Config.consumer_secret, application.getRegist_secret());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			buffer.append(authSignature);
		}*/
		buffer.append(",)");
		Logger.d("authHead:str="+buffer.toString());
		Map<String, String> map = new HashMap<String, String>();
		map.put("Authorization", buffer.toString());
		return map;
	}

}
