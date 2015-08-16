package com.example.maxwu.smsgroupsend.core;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;

import com.example.maxwu.smsgroupsend.Logger;
import com.example.maxwu.smsgroupsend.MyApplication;
import com.example.maxwu.smsgroupsend.ui.data.ContactData;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口业务逻辑处理类
 * @author ZKC82111
 *
 */
public class DataBusiness {

	private static DataBusiness mInstance = new DataBusiness();
	private static DataProvider provider;

	/*********************inner class **********************************/
	public interface ResponseHandlerT<T> {
		public void onResponse(T result);
	}
	/******************************************************************/

	Context context;
	/**
	 * 构造函数
	 * 
	 * @author
	 */
	private DataBusiness() {
		
	}

	public static DataBusiness getInstance(Context context) {
		provider = new DataProvider(context);
		return mInstance;
	}
	
//	public void setGetMore(boolean flag) {
//		// TODO Auto-generated method stub
//		provider.setGetMore(flag);
//	}
//	
	public void setIfNeighbor(boolean flag) {
		// TODO Auto-generated method stub
		provider.setIfNeighbor(flag);
	}

	/**
	 * set whether show update dialog
	 * @param flag
	 */
	public void setIfShow(boolean flag) {
		// TODO Auto-generated method stub
		provider.setIfShow(flag);
	}

	/**
	 * set update dialog's style
	 * @param text
	 */
	public void setStyleText(String text) {
		provider.setStyleText(text);
	}

	/**
	 * set update dialog's style
	 */
	public void setStyleNoText() {
		provider.setStyleNoText();
	}
	/**
	 * 获取理财产品列表
	 */
	public void getOnSaleList(final Handler handler,final int what){

		/*provider.httpGetRequest(request, GetOnSaleListResp.class,new ResponseHandlerT<GetOnSaleListResp>() {

			@Override
			public void onResponse(GetOnSaleListResp result) {
				doCallBack(handler, what, result);
			}
		} );*/
	}
	
	/**
	 * 理财产品详情页
	 */
	public void getProductDetail(final Handler handler,final int what,BaseRequest request){
		/*request.requestUrl = Config.getProductDetail;
		provider.httpGetRequest(request, GetProductDetailResp.class,new ResponseHandlerT<GetProductDetailResp>() {
			
			@Override
			public void onResponse(GetProductDetailResp result) {
				doCallBack(handler, what, result);
			}
		} );*/
	}


	/**
	 * 赎回
	 */
	public void redeem(final Handler handler,final int what,BaseRequest request) {
		/*request.requestUrl = Config.redeem;
		provider.httpPostRequest(request, BaseResponse.class,new ResponseHandlerT<BaseResponse>() {
			
			@Override
			public void onResponse(BaseResponse result) {
				doCallBack(handler, what, result);
			}
		} );*/
	}

	@SuppressLint("ShowToast")
	public void doCallBack(final Handler handler,final int what,BaseResponse response){
		handler.sendMessage(handler.obtainMessage(what,response));
	}

	/**
	 * 获取联系人列表
	 * @param handler
	 * @param what
	 */
	public void getContacts(final Handler handler , final int what  ){
		new Thread(new Runnable() {

			/**
			 * 获取 联系人电话号码
			 * @return
			 */
			private List<ContactData> getContacts(){

				List<ContactData> result = new ArrayList<ContactData>();

				//生成ContentResolver对象
				ContentResolver contentResolver = MyApplication.instance.getContentResolver();

				// 获得所有的联系人
					/*Cursor cursor = contentResolver.query(
							ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
					 */
				//这段代码和上面代码是等价的，使用两种方式获得联系人的Uri
				Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/contacts"),null,null,null,null);

				// 循环遍历
				if (cursor.moveToFirst()) {

					int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
					int displayNameColumn = cursor
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
					Logger.m("displayNameColumn = "+displayNameColumn);
					do {
						// 获得联系人的ID
						String contactId = cursor.getString(idColumn);
						Logger.m("contactId = "+contactId);
						// 获得联系人姓名
						String displayName = cursor.getString(displayNameColumn);
						Logger.m("displayName = "+displayName);
						// 查看联系人有多少个号码，如果没有号码，返回0
						int phoneCount = cursor
								.getInt(cursor
										.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

						if (phoneCount > 0) {
							// 获得联系人的电话号码列表
							Cursor phoneCursor = MyApplication.instance.getContentResolver().query(
									ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
									null,
									ContactsContract.CommonDataKinds.Phone.CONTACT_ID
											+ "=" + contactId, null, null);
							if(phoneCursor.moveToFirst())
							{
								do
								{
									//遍历所有的联系人下面所有的电话号码
									String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									Logger.m("phoneNumber = "+phoneNumber);
									ContactData item =new ContactData();
									item.setName(displayName);
									item.setTel(phoneNumber);
									result.add(item);
								}while(phoneCursor.moveToNext());
							}
							phoneCursor.close();
						}
					} while (cursor.moveToNext());
					cursor.close();
				}
				return result;
			}

			@Override
			public void run() {
					List<ContactData> result = getContacts();
				    Message msg = handler.obtainMessage(what,result);
					handler.sendMessage(msg);
				}
		}).start();
	}
}
