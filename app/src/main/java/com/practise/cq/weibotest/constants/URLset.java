package com.practise.cq.weibotest.constants;

import android.util.SparseArray;

/**
 * Created by CQ on 2016/5/17 0017.
 */
public class URLset {
    
   public static final String BASE_URL = "https://api.weibo.com/2";

   public static final int READ_API_FRIENDS_TIMELINE    = 0;
   public static final int READ_API_MENTIONS         = 1;
   public static final int WRITE_API_UPDATE          = 2;
   public static final int WRITE_API_REPOST          = 3;
   public static final int WRITE_API_UPLOAD          = 4;
   public static final int WRITE_API_UPLOAD_URL_TEXT = 5;
    
    public static SparseArray<String> URLs = new SparseArray<String>();
    
    static {
      URLs.put(READ_API_FRIENDS_TIMELINE,    BASE_URL + "/statuses/friends_timeline.json");
      URLs.put(READ_API_MENTIONS,         BASE_URL + "/statuses/mentions.json");
      URLs.put(WRITE_API_REPOST,          BASE_URL + "/statuses/repost.json");
      URLs.put(WRITE_API_UPDATE,          BASE_URL + "/statuses/update.json");
      URLs.put(WRITE_API_UPLOAD,          BASE_URL + "/statuses/upload.json");
      URLs.put(WRITE_API_UPLOAD_URL_TEXT, BASE_URL + "/statuses/upload_url_text.json");
    }



}
