package com.practise.cq.weibotest.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CQ on 2016/5/16 0016.
 */
public class FragmentController {

    private static FragmentController controller;
    private int mContainerId;
    private FragmentManager manager;
    private List<Fragment> fragments;


    private FragmentController(FragmentActivity activity, int containerId) {
        mContainerId = containerId;
        manager = activity.getSupportFragmentManager();

        initFragments();
    }

    /**懒汉方式创建单例*/
    public static FragmentController getInstance(FragmentActivity activity, int containerId){
        if (controller == null){
            synchronized (FragmentController.class){
                if (controller == null){
                    controller = new FragmentController(activity, containerId);
                }
            }
        }
        return controller;
    }
    /**将碎片加入到容器后，全部添加到指定ID布局*/
    private void initFragments() {
        fragments = new ArrayList<Fragment>();

        fragments.add(new HomeFragment());
        fragments.add(new MessageFragment());
        fragments.add(new SquareFragment());
        fragments.add(new MyInfoFragment());

        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragments){
            transaction.add(mContainerId, fragment);
        }
        transaction.commit();
    }
    /**显示指定碎片*/
    public void showFragment(int position){
        hideFragment();
        Fragment fragment = getFragment(position);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }
    /**隐藏所有碎片*/
    public void hideFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragments){
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

    public Fragment getFragment(int position){
        return fragments.get(position);
    }

}
