package com.practise.cq.weibotest.entity;

import android.text.TextUtils;

/**
 * Created by CQ on 2016/5/17 0017.
 */
public class PicUrls {

    private static final String BMIDDLE_URL = "http://ww3.sinaimg.cn/bmiddle";
    private static final String ORIGINAL_URL = "http://ww3.sinaimg.cn/large";

    private String thumbnail_pic;
    private String bmiddle_pic;
    private String original_pic;

    public String getImageId(){
        int index = thumbnail_pic.lastIndexOf("/");
        return thumbnail_pic.substring(index);
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return TextUtils.isEmpty(bmiddle_pic) ? (getImageId() + BMIDDLE_URL) : bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return TextUtils.isEmpty(original_pic) ? (getImageId() + ORIGINAL_URL) : original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }
}
