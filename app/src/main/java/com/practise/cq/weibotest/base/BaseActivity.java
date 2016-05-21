package com.practise.cq.weibotest.base;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.constants.CommonContants;
import com.practise.cq.weibotest.util.ActivityCollector;
import com.practise.cq.weibotest.util.LogUtil;

/**
 * Created by CQ on 2016/5/13 0013.
 */
public class BaseActivity extends FragmentActivity {

    protected String TAG;
    protected MyApplication application;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCollector.addActivity(this);
        super.onCreate(savedInstanceState);
        /**强制竖屏*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TAG = this.getClass().getSimpleName();
        application = (MyApplication)getApplication();
        sp = getSharedPreferences(CommonContants.SP_NAME, MODE_PRIVATE);
    }
    /**载入视图*/
    @Override
    public void setContentView(int layoutResID) {
        LinearLayout root = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.base_activity, null);
        LinearLayout container = (LinearLayout)root.findViewById(R.id.container);
        LayoutInflater.from(this).inflate(layoutResID, container);
        super.setContentView(root);
    }

    @Override
    public void setContentView(View view) {
        LinearLayout root = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.base_activity, null);
        LinearLayout container = (LinearLayout)root.findViewById(R.id.container);
        container.addView(view);
        super.setContentView(root);
    }

    /**打印日志*/
    protected void showLog(String msg){
        LogUtil.d(TAG, msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
