package com.practise.cq.weibotest.base;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.practise.cq.weibotest.util.ImageOptionHelper;

/**
 * Created by CQ on 2016/5/14 0014.
 */
public class MyApplication extends Application {

    public static RequestQueue mQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
        mQueue = Volley.newRequestQueue(this);
    }

    private void initImageLoader(Context context) {
//        int maxMemory = (int)Runtime.getRuntime().maxMemory()/1024/8;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheSizePercentage(12)
                .defaultDisplayImageOptions(ImageOptionHelper.getImageOptions())
                .build();
        ImageLoader.getInstance().init(config);
    }
}
