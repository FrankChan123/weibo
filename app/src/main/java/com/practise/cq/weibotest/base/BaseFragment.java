package com.practise.cq.weibotest.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.practise.cq.weibotest.activity.MainActivity;

/**
 * Created by CQ on 2016/5/16 0016.
 */
public class BaseFragment extends Fragment{

    protected MainActivity activity;
    protected SharedPreferences pf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();

        pf = PreferenceManager.getDefaultSharedPreferences(activity);
    }
}
