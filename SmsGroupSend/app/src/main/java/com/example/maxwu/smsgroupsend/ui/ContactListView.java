package com.example.maxwu.smsgroupsend.ui;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.maxwu.smsgroupsend.Logger;
import com.example.maxwu.smsgroupsend.R;
import com.example.maxwu.smsgroupsend.core.DataBusiness;
import com.example.maxwu.smsgroupsend.ui.data.ContactData;
import com.example.maxwu.smsgroupsend.widgetFragment.ListViewFragment;

import java.util.List;

/**
 * Created by maxwu on 8/16/15.
 */
public class ContactListView extends ListViewFragment<ContactData>{

    private ViewGroup container ;

    @Override
    protected void doRequest(DataBusiness business, Handler handler, int what) {
        Logger.m("ContactListView doRequest");
        business.getContacts(handler,what);
    }

    @Override
    protected List<ContactData> doResponse(Message msg) {
        Logger.m("ContactListView doResponse msg=" + msg);
        Object result = msg.obj;
        if(null != result && (result instanceof List)){
            return (List)result;
        }
        return null;
    }

    @Override
    protected View getView(LayoutInflater inflater,int position, View convertView, ViewGroup parent , final ContactData data) {
        if(null == convertView){
            convertView = inflater.inflate(R.layout.listitem_one,null);
        }

        CheckBox mCheck = (CheckBox)convertView.findViewById(R.id.cb_listitem_one_select);
        TextView mName = (TextView)convertView.findViewById(R.id.tv_listitem_one_name);
        TextView mPhone = (TextView)convertView.findViewById(R.id.tv_listitem_one_phone);
        //因为 converView 复用 的原因 ，如果这里不把checkbox的监听清空，会改变其他位置的 data数据
        mCheck.setOnCheckedChangeListener(null);
        mCheck.setChecked(data.isChecked());
        mName.setText(data.getName());
        mPhone.setText(data.getTel());

        mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Logger.m("onCheckedChanged isChecked = " + isChecked);
                data.setIsChecked(isChecked);
            }
        });

        return convertView;
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
}
