package com.example.maxwu.smsgroupsend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.maxwu.smsgroupsend.core.DataBusiness;
import com.example.maxwu.smsgroupsend.inject.ViewInjectUtils;
import com.example.maxwu.smsgroupsend.inject.interf.ViewInject;
import com.example.maxwu.smsgroupsend.ui.ContactListView;
import com.example.maxwu.smsgroupsend.ui.ContainerViewPager;
import com.example.maxwu.smsgroupsend.ui.data.ContactData;
import com.example.maxwu.smsgroupsend.widgetFragment.ListViewFragment;

import java.util.List;

/**
 * Created by maxwu on 8/15/15.
 */
public class TmpFragment extends Fragment{


    @ViewInject(R.id.ll_tmp_container)
    private LinearLayout mContainer ;

    private ContainerViewPager containerViewPager ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("Max", "----------------tmpFragment-----onCreateView---------------------");

        View view = inflater.inflate(R.layout.tmp_blank_fragment,null);

        ViewInjectUtils.injectViews(this, view);

        getParams();

        initViewState();

        return view ;
    }

    protected void initViewState(){
        Bundle bundle = new Bundle();
        bundle.putInt("container_id" , mContainer.getId());

/*        HeaderWidget headerWidget = new HeaderWidget();
        headerWidget.setArguments(bundle);*/

/*        OneLinePagerWidget oneLinePagerWidget = new OneLinePagerWidget();
        oneLinePagerWidget.setArguments(bundle);*/
/*        EduOneLineWidget eduOneLineWidget = new EduOneLineWidget();
        eduOneLineWidget.setArguments(bundle);
        EduTwoLineWidget eduTwoLineWidget = new EduTwoLineWidget();
        eduTwoLineWidget.setArguments(bundle);*/

/*        HomeFragPageGridView homeFragPageGridView = new HomeFragPageGridView();
        homeFragPageGridView.setArguments(bundle);

        HomeFragViewPager homeFragViewPager = new HomeFragViewPager();
        homeFragViewPager.setArguments(bundle);

        HomeFragNewsLayor homeFragNewsLayor = new HomeFragNewsLayor();
        homeFragNewsLayor.setArguments(bundle);

        HomeFragFooterLayor homeFragFooterLayor = new HomeFragFooterLayor();
        homeFragFooterLayor.setArguments(bundle);*/

        /*eduHeaderWidget.setArguments(bundle);
        eduBodyWidget.setArguments(bundle);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(eduHeaderWidget, eduHeaderWidget.getClass().getName());
        ft.add(eduBodyWidget,eduBodyWidget.getClass().getName());
        ft.commit();*/

        containerViewPager = new ContainerViewPager();
        containerViewPager.setArguments(bundle);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(containerViewPager,containerViewPager.getClass().getName());
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        Log.e("Max", "----------------tmpFragment-----onDestroyView---------------------");
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
//        ft.remove(eduBodyWidget);
//        ft.remove(eduHeaderWidget);

        ft.remove(containerViewPager);
        fm.executePendingTransactions();

        super.onDestroyView();
    }

    protected void getParams(){

    }
}
