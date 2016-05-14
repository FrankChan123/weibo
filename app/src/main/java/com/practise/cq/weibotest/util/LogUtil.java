package com.practise.cq.weibotest.util;

import android.util.Log;

/**
 * 自定义日志工具，通过更改LEVEL的值控制不同类型日志显示
 * LEVEL = NOTHING时，不显示日志
 * Created by CQ on 2016/5/13 0013.
 */
public class LogUtil  {

    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 6;

    private static final int LEVEL = VERBOSE;
//    private static final int LEVEL = NOTHING;

    public void v(String tag, String msg){
        if (LEVEL <= VERBOSE){
            Log.v(tag, msg);
        }
    }

    public void d(String tag, String msg){
        if (LEVEL <= DEBUG){
            Log.d(tag, msg);
        }
    }

    public void i(String tag, String msg){
        if (LEVEL <= INFO){
            Log.i(tag, msg);
        }
    }

    public void w(String tag, String msg){
        if (LEVEL <= WARN){
            Log.w(tag, msg);
        }
    }

    public void e(String tag, String msg){
        if (LEVEL <= ERROR){
            Log.e(tag, msg);
        }
    }
}
