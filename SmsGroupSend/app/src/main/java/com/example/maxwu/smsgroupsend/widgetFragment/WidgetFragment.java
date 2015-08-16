package com.example.maxwu.smsgroupsend.widgetFragment;

import android.support.v4.app.Fragment;
import android.view.ViewGroup;


/**
 * WidgetFragment-------->只是用于封装独立控件的操作，有利于把界面各个独立操作的UI分离，让Fragment更简洁。
 * User: MaxWu
 * Date: 2015-04-27
 * Time: 13:39
 */
public abstract class WidgetFragment extends Fragment{

    /**
     * 获取要Widget 要 attach 的 父View
     * 一般方法是通过 argument 传相应的参数进行操作
     * @return
     */
    protected abstract ViewGroup getContainerView();

    /**
     * 获取Widget 要 attach 到 父View 的位置
     * 一般方法是通过 argument 传相应的参数进行操作
     * @return
     */
    protected abstract int getChildIndex();
}
