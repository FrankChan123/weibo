package com.practise.cq.weibotest.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.practise.cq.weibotest.constants.URLset;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * Created by CQ on 2016/5/17 0017.
 */
public class WeiboApi extends AbsOpenAPI{

    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());

    public WeiboApi(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    /**对requestAsync进行封装，使其耗时操作执行完毕后到主线程处理相应逻辑*/
    public void requestInMainThread(String url, WeiboParameters params,
                                    String httpMethod, final RequestListener listener){
        requestAsync(url, params, httpMethod, new RequestListener() {
            @Override
            public void onComplete(final String response) {
                mainLooperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onComplete(response);
                    }
                });
            }

            @Override
            public void onWeiboException(final WeiboException e) {
                mainLooperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onWeiboException(e);
                    }
                });
            }
        });
    }

    @Override
    protected void requestAsync(String url, WeiboParameters params,
                                String httpMethod, RequestListener listener) {
        super.requestAsync(url, params, httpMethod, listener);
    }


    /**
     * 获取当前登录用户的好友微博
     *
     * @param count
     *          每页显示的条数
     * @param page
     *          返回的页码 第一页为最新
     * @param listener
     */
    public void statusesFriends_timeline(int count, int page, RequestListener listener){
        WeiboParameters parameters = new WeiboParameters(mAppKey);
        parameters.put("count", count);
        parameters.put("page", page);
        requestInMainThread(URLset.URLs.get(URLset.READ_API_FRIENDS_TIMELINE), parameters,
                HTTPMETHOD_GET, listener);
    }

    /**
     * 根据微博ID返回某条微博的评论列表
     *
     * @param id
     *            需要查询的微博ID。
     * @param page
     *            返回结果的页码。(单页返回的记录条数，默认为50。)
     * @param listener
     */
    public void commentsShow(long id, long page, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("id", id);
        params.put("page", page);
        requestInMainThread(URLset.URLs.get(URLset.READ_API_SHOW), params , AbsOpenAPI.HTTPMETHOD_GET, listener);
    }

    /**
     * 对一条微博进行评论。
     *
     * @param comment     评论内容，内容不超过140个汉字。
     * @param id          需要评论的微博ID。
//     * @param comment_ori 当评论转发微博时，是否评论给原微博
     * @param listener    异步请求回调接口
     */
    public void commentCreate(String comment, long id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("comment", comment);
        params.put("id", id);

        requestInMainThread(URLset.URLs.get(URLset.WRITE_API_CREATE), params , AbsOpenAPI.HTTPMETHOD_POST, listener);
    }

    /**
     * 发布或转发一条微博
     *
     * @param status
     *            要发布的微博文本内容。
     * @param bitmap
     *            要上传的图片(为空则代表发布无图微博)。
     * @param retweetedStatsId
     *            要转发的微博ID(<=0时为原创微博)。
     * @param listener
     */
    public void sendStatus(String status, Bitmap bitmap, long retweetedStatsId, RequestListener listener) {
        String url;
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("status", status);
        if(retweetedStatsId > 0) {
            /**如果是转发微博,设置被转发者的id*/
            params.put("id", retweetedStatsId);
            url = URLset.URLs.get(URLset.WRITE_API_REPOST);
        } else if(bitmap != null) {
            /**如果有图片,则调用upload接口且设置图片路径*/
            params.put("pic", bitmap);
            url = URLset.URLs.get(URLset.WRITE_API_UPLOAD);
        } else {
            /**如果无图片,则调用update接口*/
            url = URLset.URLs.get(URLset.WRITE_API_UPDATE);
        }
        requestInMainThread(url, params, AbsOpenAPI.HTTPMETHOD_POST, listener);
    }

    /**
     * 查询登录用户的信息
     *
     * @param uid
     *      用户id
     * @param screen_name
     *      用户昵称
     * @param listener
     *
     * */
    public void userShow(String uid, String screen_name, RequestListener listener){
        WeiboParameters parameters = new WeiboParameters(mAppKey);
        if (!TextUtils.isEmpty(uid)){
            parameters.put("uid", uid);
        }else if (!TextUtils.isEmpty(screen_name)){
            parameters.put("screenName", screen_name);
        }

        requestInMainThread(URLset.URLs.get(URLset.API_USER_SHOW), parameters,
                AbsOpenAPI.HTTPMETHOD_GET, listener);
    }

    /**
     * 查询登录用户的微博列表
     *
     * @param id
     *      用户id
     * @param screen_name
     *      用户昵称
     * @param page
     *      查询返回页
     * @param listener
     *
     * */
    public void statusUser_timeline(long id, String screen_name, long page, RequestListener listener){
        WeiboParameters params = new WeiboParameters(mAppKey);
        if (id > 0){
            params.put("id", id);
        }else if (!TextUtils.isEmpty(screen_name)){
            params.put("screen_name", screen_name);
        }
        params.put("page", page);
        requestInMainThread(URLset.URLs.get(URLset.READ_API_USER_TIMELINE), params,
                AbsOpenAPI.HTTPMETHOD_GET, listener);
    }

//    /**
//     * 获取当前登录用户及其所关注用户的最新微博。
//     *
//     * @param since_id    若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
//     * @param max_id      若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
//     * @param count       单页返回的记录条数，默认为50。
//     * @param page        返回结果的页码，默认为1。
//     * @param base_app    是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
//     * @param featureType 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
//     *
//     * @param trim_user   返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
//     * @param listener    异步请求回调接口
//     */
//    public void friendsTimeline(int page, RequestListener listener) {
//        WeiboParameters params = new WeiboParameters(mAppKey);
//        params.put("page", page);
//        requestAsync(URLset.URLs.get(URLset.READ_API_FRIENDS_TIMELINE), params, HTTPMETHOD_GET, listener);
//    }
//
//    /**组装statusesHome_timeline的参数*/
//    private WeiboParameters buildTimeLineParamsBase(long since_id, long max_id, int count, int page,
//                                                    boolean base_app, boolean trim_user, int featureType) {
//        WeiboParameters params = new WeiboParameters(mAppKey);
//        params.put("since_id", since_id);
//        params.put("max_id", max_id);
//        params.put("count", count);
//        params.put("page", page);
//        params.put("base_app", base_app ? 1 : 0);
//        params.put("trim_user", trim_user ? 1 : 0);
//        params.put("feature", featureType);
//        return params;
//    }


}


