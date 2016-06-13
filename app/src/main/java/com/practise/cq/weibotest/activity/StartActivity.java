package com.practise.cq.weibotest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.base.BaseActivity;
import com.practise.cq.weibotest.constants.AccessTokenKeeper;
import com.practise.cq.weibotest.constants.CommonContants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by CQ on 2016/5/13 0013.
 */
public class StartActivity extends BaseActivity {

    private Oauth2AccessToken mAccessToken;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CommonContants.WHAT_INTENT2MAIN:
                    Intent intent2Main = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent2Main);
                    finish();
                    break;
                case CommonContants.WHAT_INTENT2LOGIN:
                    Intent intent2Login = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent2Login);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()){
            handler.sendEmptyMessageDelayed(CommonContants.WHAT_INTENT2MAIN,
                    CommonContants.SPLASH_DUR);
        }else {
            handler.sendEmptyMessageDelayed(CommonContants.WHAT_INTENT2LOGIN,
                    CommonContants.SPLASH_DUR);
        }
    }
}
