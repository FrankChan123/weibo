package com.practise.cq.weibotest.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast 工具类
 * Created by CQ on 2016/5/13 0013.
 */
public class ToastUtil {

    private Toast mToast;

    public void show(Context context, CharSequence text, int duration){
        if (mToast == null){
            mToast = Toast.makeText(context, text, duration);
        }else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }
}
