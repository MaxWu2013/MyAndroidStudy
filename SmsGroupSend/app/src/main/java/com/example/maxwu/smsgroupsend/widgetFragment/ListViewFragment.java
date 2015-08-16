package com.example.maxwu.smsgroupsend.widgetFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.maxwu.smsgroupsend.R;
import com.example.maxwu.smsgroupsend.core.DataBusiness;
import com.example.maxwu.smsgroupsend.imgcache.ImageFetcher;
import com.example.maxwu.smsgroupsend.imgcache.SharedImageFetcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxwu on 8/16/15.
 *
 * Commom  ListView widget ,
 *
 *
 */
public abstract class ListViewFragment<T> extends ExWidgetFragment{

    private static final int GET_DATA= 0x001 ;

    private ListView mListView ;
    private LvAdapter mLvAdapter ;

    private DataBusiness mBusiness ;
    private ImageFetcher mFetcher ;
    private Handler mainHandler ;

    private List<T> mDataList = new ArrayList<T>();

    @Override
    protected View getWidget(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListView = (ListView)inflater.inflate(R.layout.widget_listview,null);
        return mListView;
    }

    @Override
    protected void initViewState() {
        mLvAdapter = new LvAdapter();
        mListView.setAdapter(mLvAdapter);


        mBusiness = DataBusiness.getInstance(getActivity().getApplicationContext());
        mFetcher = SharedImageFetcher.getNewFetcher(getActivity().getApplicationContext(), 4);
        mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(null == getActivity()){
                    return ;
                }
                switch(msg.what){
                    case GET_DATA:
                        List<T> data = doResponse(msg);

                        if(null != data ){
                            mDataList.clear();
                            mDataList.addAll(data);
                            notifyDataSetChanged();
                        }
                        break;
                }
            }
        };

        doRequest(mBusiness, mainHandler, GET_DATA);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainHandler.removeCallbacksAndMessages(null);
        mainHandler  = null;
    }


    /**
     *回调请求，用所给的business , handler , what 处理请求任务
     *
     */
    protected abstract void doRequest(final DataBusiness business ,final Handler handler , final int what );

    /**
     * 处理请求任务， 用所给的 msg ,处理 doRequest 所请求的任务，完成任务处理，返回需要的 数据 T 列表
     * @param msg
     * @return
     */
    protected abstract List<T> doResponse(final Message msg);

    /**
     *  获取 position 位置的 listView 的布局
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    protected abstract View getView(LayoutInflater inflater,int position,View convertView, ViewGroup parent,T data);


    /**
     * update listView
     */
    public void notifyDataSetChanged(){
        if (null != mLvAdapter){
            mLvAdapter.notifyDataSetChanged();
        }
    }

    /**
     * get the Data list for this listView
     * @return
     */
    public List<T> getDataList(){
        return mDataList;
    }

    private class LvAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return null==mDataList?0:mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return ListViewFragment.this.getView(LayoutInflater.from(getActivity()),position,convertView,parent,mDataList.get(position));
        }
    }
}
