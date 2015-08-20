package com.example.maxwu.smsgroupsend.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.maxwu.smsgroupsend.Config;
import com.example.maxwu.smsgroupsend.Logger;
import com.example.maxwu.smsgroupsend.MyApplication;
import com.example.maxwu.smsgroupsend.core.DataBusiness;
import com.example.maxwu.smsgroupsend.imgcache.ImageFetcher;
import com.example.maxwu.smsgroupsend.ui.data.ContactData;
import com.example.maxwu.smsgroupsend.widgetFragment.ViewPagerWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxwu on 8/16/15.
 */
public class ContainerViewPager extends ViewPagerWidget<Integer>{

    private final int PAGE_ONE = 10;
    private final int PAGE_TWO = 11;

    private boolean pageOne_add =false;
    private boolean pageTwo_add =false;

    private LinearLayout pageOne , pageTwo ;
    private List<Integer> list ;

    private ContactListView contactListView;
    private SendSmsLayout sendSmsLayout;

    private SendSmsLayout.OnSendClick mOnSendClick = new SendSmsLayout.OnSendClick() {
        @Override
        public void onSendClick(final String content) {
            if(null != contactListView){
                final List<ContactData> list = contactListView.getDataList();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        groupSendMsg(list,content);
                    }
                }).start();

            }
        }
    };


    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (msg.what){
                case PAGE_ONE:
                    if(!pageOne_add){
                        pageOne_add = true ;
                        ft.add(sendSmsLayout,sendSmsLayout.getClass().getName());
                        ft.commit();
                    }
                    break;
                case PAGE_TWO:
                    if(!pageTwo_add){
                        pageTwo_add = true;
                        ft.add(contactListView,contactListView.getClass().getName());
                        ft.commit();
                    }
                    break;
            }
        }
    };

    @Override
    protected void doRequest(DataBusiness business, Handler handler, int what) {
        pageOne = new LinearLayout(MyApplication.instance);
        pageTwo = new LinearLayout(MyApplication.instance);

        ViewGroup.LayoutParams paramOne = new ViewGroup.LayoutParams(
                480,
                800);
        ViewGroup.LayoutParams paramTwo =new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        pageOne.setLayoutParams(paramOne);
        pageTwo.setLayoutParams(paramTwo);

        contactListView = new ContactListView();
        sendSmsLayout = new SendSmsLayout();
        sendSmsLayout.setOnSendClick(mOnSendClick);

        sendSmsLayout.setContainer(pageOne);
        contactListView.setContainer(pageTwo);

        list = new ArrayList<Integer>() ;
        list.add(PAGE_ONE);
        list.add(PAGE_TWO);

        handler.sendMessage(handler.obtainMessage(what, list));
    }

    @Override
    protected List<Integer> doResponse(Message msg) {

        return list;
    }

    @Override
    protected View getPageView(int position, View convertView, Integer itemInfo, ImageFetcher fetcher) {
        View view = null;
        Logger.m("getPageView position = " + position + " itemInfo=" + itemInfo);
        switch(itemInfo){
            case PAGE_ONE:
                view = pageOne;
                mainHandler.sendEmptyMessageDelayed(PAGE_ONE, 500);
                break;
            case PAGE_TWO:
                view = pageTwo;
                mainHandler.sendEmptyMessageDelayed(PAGE_TWO,600);
                break;
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainHandler.removeCallbacksAndMessages(null);
        mainHandler = null ;

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(pageOne_add){
            ft.remove(sendSmsLayout);
            fm.executePendingTransactions();
        }
        if(pageTwo_add){
            ft.remove(contactListView);
            fm.executePendingTransactions();
        }

        getActivity().unregisterReceiver(receiverOne);
        getActivity().unregisterReceiver(receiverTwo);
    }

    /**
     *  group send sms, must be called at background thread
     * @param list
     * @param content
     */
    private void groupSendMsg(List<ContactData> list , String content){
        String resultContent = null ;
        if(null != list && list.size()>0 && null != content){
            Logger.m("list sizie() = "+list.size());
            for(ContactData item : list){
                if(item.isChecked()){
                    Logger.m("item checked = true ");
                    resultContent = content.replace(Config.LXR,item.getName());
                    sendMsg(resultContent, item.getTel());
                    try{
                        Logger.m("sleep");
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Logger.m("after sleep");
                }

            }
        }
    }

    /**
     * send sms
     * @param message
     * @param phoneNumber
     */
    private void sendMsg(String message , String phoneNumber){
        if(null != phoneNumber &&
                null != message &&
                !"".equals(message.trim()) &&
                !"".equals(phoneNumber.trim())){
            Logger.m("message = "+message+" phoneNumber="+phoneNumber);
            Intent flagOneIntent = new Intent(SENT);
            flagOneIntent.putExtra(PHONE_NUM,phoneNumber);
            PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0,
                    flagOneIntent, 0);
            Intent flagTwoIntent = new Intent(DELIVERED);
            flagTwoIntent.putExtra(PHONE_NUM,phoneNumber);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,
                    flagTwoIntent, 0);

            //---when the SMS has been sent---
            getActivity().registerReceiver(receiverOne, filterOne);

            //---when the SMS has been delivered---
            getActivity().registerReceiver(receiverTwo, filterTwo);

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }
    }
    private String SENT = "SMS_SENT";
    private String DELIVERED = "SMS_DELIVERED";
    private String PHONE_NUM ="PHONE_NUMBER";
    private IntentFilter filterOne = new IntentFilter(SENT);
    private IntentFilter filterTwo = new IntentFilter(DELIVERED);

    private BroadcastReceiver receiverOne = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String phoneNum = arg1.getStringExtra(PHONE_NUM);
            Logger.m("onReceiver intent = "+arg1);
            Logger.m("onReceiver phoneNum = "+phoneNum);
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getActivity().getBaseContext(), "SMS sent",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getActivity().getBaseContext(), "Generic failure",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getActivity().getBaseContext(), "No service",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getActivity().getBaseContext(), "Null PDU",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getActivity().getBaseContext(), "Radio off",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private BroadcastReceiver receiverTwo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String phoneNum = arg1.getStringExtra(PHONE_NUM);
            Logger.m("onReceiver intent = "+arg1);
            Logger.m("onReceiver phoneNum = "+phoneNum);
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getActivity().getBaseContext(), "SMS delivered",
                            Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity().getBaseContext(), "SMS not delivered",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
