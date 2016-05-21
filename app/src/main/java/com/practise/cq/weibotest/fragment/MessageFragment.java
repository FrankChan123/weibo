package com.practise.cq.weibotest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.base.BaseFragment;
import com.practise.cq.weibotest.util.TitleBarBuilder;

/**
 * Created by CQ on 2016/5/16 0016.
 */
public class MessageFragment extends BaseFragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.message_fragment, null);

        new TitleBarBuilder(view)
                .setTitleText("消息");
        return view;
    }
}
