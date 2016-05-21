package com.practise.cq.weibotest.util;

import android.util.Log;

/**
 * 自定义日志工具，通过更改LEVEL的值控制不同类型日志显示
 * LEVEL = NOTHING时，不显示日志
 * Created by CQ on 2016/5/13 0013.
 */
public class LogUtil  {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    public static final int LEVEL = VERBOSE;
//    private static final int LEVEL = NOTHING;

    static public void v(String tag, String msg){
        if (LEVEL <= VERBOSE){
            Log.v(tag, msg);
        }
    }

    static public void d(String tag, String msg){
        if (LEVEL <= DEBUG){
            Log.d(tag, msg);
        }
    }

    static public void i(String tag, String msg){
        if (LEVEL <= INFO){
            Log.i(tag, msg);
        }
    }

    static public void w(String tag, String msg){
        if (LEVEL <= WARN){
            Log.w(tag, msg);
        }
    }

    static public void e(String tag, String msg){
        if (LEVEL <= ERROR){
            Log.e(tag, msg);
        }
    }
}
