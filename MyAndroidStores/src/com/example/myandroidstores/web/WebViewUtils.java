package com.example.myandroidstores.web;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewUtils {
	
	/**
	 * 初始化webView 的基本配置 
	 * @param webView
	 */
	private void initDefaltWebView(WebView webView){
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.getSettings().setDomStorageEnabled(true);
		
		//使得url 的跳转在本webview里面，而不是在浏览器里打开
		webView.setWebViewClient(new WebViewClient(){
            
        });
	}
	
}
