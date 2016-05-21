package com.practise.cq.weibotest.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.practise.cq.weibotest.R;

/**
 * Created by CQ on 2016/5/17 0017.
 */
public class ImageOptionHelper {

    public static DisplayImageOptions getImageOptions(){
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                /*图片的颜色类型*/
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                /*加载时对应图片*/
                .showImageOnLoading(R.mipmap.timeline_image_loading)
                /*空地址对应图片*/
                .showImageForEmptyUri(R.mipmap.timeline_image_loading)
                /*加载失败对应图片*/
                .showImageOnFail(R.mipmap.timeline_image_failure)
                .build();
        return imageOptions;

    }

    /**显示圆形头像的设置*/
    public static DisplayImageOptions getAvatarOptions() {
        DisplayImageOptions	avatarOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc()
                .cacheInMemory()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showStubImage(R.mipmap.avatar_default)
                .showImageForEmptyUri(R.mipmap.avatar_default)
                .showImageOnFail(R.mipmap.avatar_default)
                .displayer(new RoundedBitmapDisplayer(999))
                .build();
        return avatarOptions;
    }
    /**显示圆角图片的设置*/
    public static DisplayImageOptions getCornerOptions(int cornerRadiusPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc()
                .cacheInMemory()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showStubImage(R.mipmap.timeline_image_loading)
                .showImageForEmptyUri(R.mipmap.timeline_image_loading)
                .showImageOnFail(R.mipmap.timeline_image_loading)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).build();
        return options;
    }
}
