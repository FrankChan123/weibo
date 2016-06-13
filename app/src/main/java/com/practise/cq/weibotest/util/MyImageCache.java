package com.practise.cq.weibotest.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by CQ on 2016/5/29 0029.
 */
public class MyImageCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mCache;

    public MyImageCache() {
        int cacheSize = 10*1024*1024;
        mCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }
}
