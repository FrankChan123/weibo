package com.practise.cq.weibotest.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.base.BaseActivity;
import com.practise.cq.weibotest.util.StringUtils;

/**
 * Created by CQ on 2016/5/22 0022.
 */
public class TestActivity extends BaseActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        tv = (TextView)findViewById(R.id.tv_content);
        tv.setText("@微博上的大V，[呵呵]");
        String text = (String) tv.getText();
        tv.setText(StringUtils.getWeiboContent(this, tv, text));

    }
}
