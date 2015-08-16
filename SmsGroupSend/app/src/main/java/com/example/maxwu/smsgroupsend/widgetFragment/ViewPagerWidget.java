package com.example.maxwu.smsgroupsend.widgetFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.maxwu.smsgroupsend.R;
import com.example.maxwu.smsgroupsend.core.DataBusiness;
import com.example.maxwu.smsgroupsend.imgcache.ImageFetcher;
import com.example.maxwu.smsgroupsend.imgcache.SharedImageFetcher;
import com.example.maxwu.smsgroupsend.inject.ViewInjectUtils;
import com.example.maxwu.smsgroupsend.inject.interf.ViewInject;
import com.example.maxwu.smsgroupsend.widget.CirclePageIndicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * ViewPagerFragment    ------ 封装了对ViewPager的操作
 *
 * User: MaxWu
 * Date: 2015-04-27
 * Time: 13:38
 */
public abstract class ViewPagerWidget<T> extends ExWidgetFragment {
    private static final int GET_DATA= 0x001 ;

    @ViewInject(R.id.vp_widget_viewpager_fragment)
    private ViewPager mViewPager;
    @ViewInject(R.id.cpi_widget_viewpager_fragment)
    private CirclePageIndicator mPagerIndicator;

    private MyPagerAdapter mPagerAdapter ;

    private DataBusiness mBusiness ;
    private ImageFetcher mFetcher ;
    private Handler mainHandler ;

    private List<T> mDataList = new ArrayList<T>();
    private List<MyPage> mMyPageList = new ArrayList<MyPage>();

    @Override
    protected View getWidget(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_viewpager, null);
    }

    @Override
    protected void initViewState() {
        ViewInjectUtils.injectViews(ViewPagerWidget.class, this, widget);
        mPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mPagerIndicator.setViewPager(mViewPager);

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

                            updatePageList();
                        }
                        break;
                }
            }
        };

        doRequest(mBusiness ,mainHandler , GET_DATA);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainHandler.removeCallbacksAndMessages(null);
        mainHandler  = null;
    }

    public class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mMyPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MyPage page = mMyPageList.get(position);

            View view = page.getView(getActivity().getLayoutInflater());

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View)object);
        }
    }

    private class MyPage{

        public MyPage(int pageNo){
            this.pageNo = pageNo ;
        }

        private int pageNo = 0;

        private View view = null;

        public View onCreateView(LayoutInflater inflater){
            return getPageView( pageNo ,  null , mDataList.get(pageNo) , mFetcher);
        }

        public View getView(LayoutInflater inflater){
            if(null == view){
                view = onCreateView(inflater);
            }
            return view;
        }
    }
    /**
     * 更新viewPage页面数目
     */
    private void updatePageList(){
        mMyPageList.clear();
        int size = mDataList.size();

        for(int i=0 ; i<size ; i++){
            MyPage page = new MyPage(i);
            mMyPageList.add(page);
        }

        mPagerAdapter.notifyDataSetChanged();
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

    protected abstract View getPageView(int position ,  View convertView , T itemInfo , ImageFetcher fetcher);
}
