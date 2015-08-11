package com.example.myandroidstores.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;

public class HttpUtils {

	// 请求的超时时间
		private static final int TIME_OUT = 30000; 
	
	
	/**
	 * Get请求
	 * 
	 * @param url
	 *            请求的路径
	 * @param param
	 *            请求的参数
	 * @return
	 */
	public static String httpGet(String url, Map<String, String> param,Context con) {
		String paramStr = "";
		String requestStr = null;
		if (param == null) {
			param = new HashMap<String, String>();
		}
		
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		Iterator<Entry<String, String>> iter = param.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> en = iter.next();
			params.add(new BasicNameValuePair(en.getKey(), en.getValue()));
			//ILog.i(TAG, en.getKey()+"---------------------------"+ en.getValue());
		}
		paramStr = URLEncodedUtils.format(params, "UTF-8");
		url +=(paramStr.equals("") ? "" : "?") + paramStr;
		
		HttpGet getMethod = new HttpGet(url );
		
		
		/////////////////////////////////////////////////////////////////
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
		/////////////////////////////////////////////////////////////////
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		/////////////////////////////////////////////////////////////////
		
		HttpResponse response = null;
		
		
		
		getMethod.setHeader("User-Agent", "android_api");
		
		
		
		
		
		
		try {
			response = httpClient.execute(getMethod); // 发起GET请求
			if (response.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			requestStr = EntityUtils.toString(response.getEntity(), "utf-8");// 获取服务器响应内容
		}catch(SocketTimeoutException ex){
			return null;
		}catch(ConnectTimeoutException ex){
			return null;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestStr;
	}



	/**
	 * POST请求
	 * 
	 * @param url
	 *            请求的路径
	 * @param param
	 *            请求的参数
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> param,Context con) {
		String requestStr = null;
		HttpPost postMethod = new HttpPost(url);
		if (param == null) {
			param = new HashMap<String, String>();
		}

		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		Iterator<Entry<String, String>> iter = param.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> en = iter.next();
			params.add(new BasicNameValuePair(en.getKey(), en.getValue()));
		}
		try {
			postMethod.setEntity(new UrlEncodedFormEntity(params,
					HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
		
		///////////////////////////////////////////////////////////////
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
		///////////////////////////////////////////////////////////
		
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

		/////////////////////////////////////////////////////////////////
		
		
		try {
			HttpResponse response = httpClient.execute(postMethod);
			if (response.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			requestStr = EntityUtils.toString(response.getEntity(), "utf-8");
		}catch(SocketTimeoutException ex){

			return null;
		} catch(ConnectTimeoutException ex){
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return requestStr;
	}
	
	
	
	public static HttpClient getNetHttpsClient(){
        HttpClient httpClient = null;
        try{
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());//证书类型
            trustStore.load(null , null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);//允许所有

            //设置http https 支持
            SchemeRegistry scheReg = new SchemeRegistry();
            scheReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
            scheReg.register(new Scheme("https",sf,443));

            HttpParams params = getHttpParams();
            ClientConnectionManager conManager = new ThreadSafeClientConnManager(params ,scheReg);
            httpClient = new DefaultHttpClient(conManager ,params);

        }catch(Exception e){
            e.printStackTrace();
            HttpParams params = getHttpParams();
            return new DefaultHttpClient(params);
        }
        return httpClient ;
    }

    /**
     * 设置支持 证书访问https 的 httpClient
     * @param is    认证证书， PKCS12    BC   二进制编码
     * @return
     */
    public static HttpClient getNetHttpsClient(InputStream is){
        CertificateFactory cerfactory = null;
        Certificate cer = null ;
        KeyStore keyStore = null;
        SSLSocketFactory socketFactory = null;
        HttpClient mHttpClient = null;

        try{
            cerfactory = CertificateFactory.getInstance("X.509");
            cer = cerfactory.generateCertificate(is);
            keyStore = KeyStore.getInstance("PKCS12","BC");
            keyStore.load(null,null);
            keyStore.setCertificateEntry("trust",cer);

            HttpParams params = getHttpParams();
            socketFactory =new SSLSocketFactory(keyStore);
            Scheme sch = new Scheme("https",socketFactory,443);
            Scheme sch1 = new Scheme("http",PlainSocketFactory.getSocketFactory(),80);
            mHttpClient = new DefaultHttpClient(params);
            mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);
            mHttpClient.getConnectionManager().getSchemeRegistry().register(sch1);
        }catch(KeyManagementException e){
            e.printStackTrace();
        }catch(UnrecoverableKeyException e){
            e.printStackTrace();
        }catch(CertificateException e){
            e.printStackTrace();
        }catch(KeyStoreException e){
            e.printStackTrace();
        }catch(NoSuchProviderException e){
            e.printStackTrace();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return mHttpClient ;
    }
    

    public static HttpParams getHttpParams(){
        HttpParams params = new BasicHttpParams();

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params,true);

        //设置连接管理器的超时
        ConnManagerParams.setTimeout(params,TIME_OUT);
        //设置连接超时
        HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
        //设置socket超时
        HttpConnectionParams.setSoTimeout(params ,TIME_OUT);
        return params;

    }
    
    
}

class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    public MySSLSocketFactory(KeyStore truststore)throws
            NoSuchAlgorithmException,
            KeyManagementException,
            KeyStoreException,
            UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager(){
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        sslContext.init(null ,new TrustManager[]{tm},null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket ,host,port ,autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}