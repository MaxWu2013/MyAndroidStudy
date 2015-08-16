package com.example.maxwu.smsgroupsend.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.maxwu.smsgroupsend.Config;
import com.example.maxwu.smsgroupsend.Logger;
import com.example.maxwu.smsgroupsend.R;
import com.example.maxwu.smsgroupsend.widgetFragment.ExWidgetFragment;

/**
 * Created by maxwu on 8/16/15.
 */
public class SendSmsLayout extends ExWidgetFragment implements View.OnClickListener{

    private View layout ;
    private Button mBtInsert , mBtSend ;
    private EditText mEtContent ;

    private OnSendClick onSendClick ;

    private ViewGroup container ;

    @Override
    protected View getWidget(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.write_sms,null);
        return layout;
    }

    @Override
    protected void initViewState() {
        mBtInsert = (Button)layout.findViewById(R.id.bt_write_sms_insert);
        mBtSend =(Button)layout.findViewById(R.id.bt_write_sms_send);
        mEtContent=(EditText)layout.findViewById(R.id.et_write_sms_content);

        mBtInsert.setOnClickListener(this);
        mBtSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String content = null ;
        switch (v.getId()){
            case R.id.bt_write_sms_insert:
                Logger.m("onInsert");
                content = mEtContent.getText().toString();
                Logger.m("before insert content="+content);
                content += Config.LXR;
                Logger.m("after insert content="+content);
                mEtContent.setText(content);
                break;
            case R.id.bt_write_sms_send:
                content = mEtContent.getText().toString();
                Logger.m("Send");
                Logger.m("content="+content);
                if(null != onSendClick){
                    onSendClick.onSendClick(content);
                }
                break;
        }
    }

    public interface OnSendClick{
        void onSendClick(String content);
    }

    @Override
    protected ViewGroup getContainerView() {
        return container;
    }

    /**
     * set this widget's container
     * @param container
     */
    public void setContainer(ViewGroup container){
        this.container = container;
    }

    public void setOnSendClick(OnSendClick onSendClick) {
        this.onSendClick = onSendClick;
    }
}
