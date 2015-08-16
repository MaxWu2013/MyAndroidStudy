package com.example.maxwu.smsgroupsend.widgetFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ExWidgetFragment  是 WidgetFragment 的增强类， 提供一种初始化 WidgetFragent的使用方法。
 *
 * 可以继承ExWidgetFragment 完成快速创建，只需要实现抽象方法getWidget ， initViewState
 * 也可以继承 WidgetFragment 完成自定义创建
 *
 * User: MaxWu
 * Date: 2015-04-28
 * Time: 09:57
 */
public abstract class ExWidgetFragment extends WidgetFragment {

    protected int childIndex = -1 ;

    protected ViewGroup mContainer;

    protected View widget ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        widget = getWidget(inflater ,container ,savedInstanceState);

        getParams();

        initViewState();

        if(childIndex > -1){
            mContainer.addView(widget,childIndex);
        }else{
            mContainer.addView(widget);
        }

        return null ;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != mContainer){
            mContainer.removeView(widget);
        }
    }

    protected void getParams(){
        mContainer = getContainerView();
        childIndex = getChildIndex();
    }

    @Override
    protected ViewGroup getContainerView() {
        Bundle bundle = getArguments();
        int containerId = bundle.getInt("container_id",0);

        View root = getActivity().getWindow().getDecorView();

        View target = root.findViewById(containerId);

        if(null !=target && target instanceof ViewGroup){
            return (ViewGroup)target;
        }else{
            return null;
        }
    }

    @Override
    public int getChildIndex() {
        Bundle bundle = getArguments();
        int index = -1;
        if(null != bundle){
            index = bundle.getInt("widget_index",-1);
        }
        return index ;
    }

    public int getDpSize(int size){

        return (int) (size*getResources().getDisplayMetrics().density);
    }

    /**
     * 获取要初始化要添加的控件
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View getWidget(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    /**
     * 初始化控件状态
     */
    protected abstract void initViewState();


}
