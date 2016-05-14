package com.practise.cq.weibotest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.util.ActivityCollector;

/**
 * Created by CQ on 2016/5/13 0013.
 */
public class BaseActivity extends Activity{

    protected TextView textLeft;
    protected TextView title;
    protected TextView textRight;
    protected LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCollector.addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        LinearLayout root = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.base_activity, null);
        initView(root);
        LinearLayout container = (LinearLayout)root.findViewById(R.id.container);
        LayoutInflater.from(this).inflate(layoutResID, container);
        super.setContentView(root);
    }

    @Override
    public void setContentView(View view) {
        LinearLayout root = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.base_activity, null);
        initView(root);
        LinearLayout container = (LinearLayout)root.findViewById(R.id.container);
        container.addView(view);
        super.setContentView(root);
    }

    protected void initView(View view) {
        mContainer = (LinearLayout)view.findViewById(R.id.container);
        textLeft = (TextView)view.findViewById(R.id.left_text);
        title = (TextView)view.findViewById(R.id.center_text);
        textRight = (TextView)view.findViewById(R.id.right_text);
    }

    protected void setTitle(String title){
        this.title.setText(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
