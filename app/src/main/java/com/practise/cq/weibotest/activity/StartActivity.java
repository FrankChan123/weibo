package com.practise.cq.weibotest.activity;

import android.os.Bundle;

import com.practise.cq.weibotest.R;

/**
 * Created by CQ on 2016/5/13 0013.
 */
public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        setTitle("主页");
    }
}
