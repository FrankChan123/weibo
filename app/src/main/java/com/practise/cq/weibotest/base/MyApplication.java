package com.practise.cq.weibotest.base;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.practise.cq.weibotest.util.ImageOptionHelper;

/**
 * Created by CQ on 2016/5/14 0014.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
    }

    private void initImageLoader(Context context) {
        int maxMemory = (int)Runtime.getRuntime().maxMemory()/1024/8;
//        int maxMemory = 20*1024*1024;
//        File diskCacheDir = new File(Environment.getExternalStorageDirectory(), "cacheDir");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .threadPoolSize(5)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LruMemoryCache(maxMemory))
                .memoryCacheSize(maxMemory)
                .memoryCacheSizePercentage(12)
//                .diskCache(new UnlimitedDiskCache(diskCacheDir))
//                .diskCacheSize(50*maxMemory)
//                .diskCacheFileCount(100)
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(ImageOptionHelper.getImageOptions())
                .build();
        ImageLoader.getInstance().init(config);
    }
}
