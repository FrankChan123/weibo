package com.practise.cq.weibotest.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.practise.cq.weibotest.constants.URLset;
import com.practise.cq.weibotest.entity.Status;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public void statusesFriends_timeline(int count, int page, RequestListener listener){
        WeiboParameters parameters = new WeiboParameters(mAppKey);
        parameters.put("count", count);
        parameters.put("page", page);
        requestInMainThread(URLset.URLs.get(URLset.READ_API_FRIENDS_TIMELINE), parameters,
                HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前登录用户及其所关注用户的最新微博。
     *
     * @param since_id    若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id      若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
     * @param count       单页返回的记录条数，默认为50。
     * @param page        返回结果的页码，默认为1。
     * @param base_app    是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
     * @param featureType 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
     *
     * @param trim_user   返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
     * @param listener    异步请求回调接口
     */
    public void friendsTimeline(int page, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("page", page);
        requestAsync(URLset.URLs.get(URLset.READ_API_FRIENDS_TIMELINE), params, HTTPMETHOD_GET, listener);
    }

    /**组装statusesHome_timeline的参数*/
    private WeiboParameters buildTimeLineParamsBase(long since_id, long max_id, int count, int page,
                                                    boolean base_app, boolean trim_user, int featureType) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        params.put("base_app", base_app ? 1 : 0);
        params.put("trim_user", trim_user ? 1 : 0);
        params.put("feature", featureType);
        return params;
    }

    /**从返回的Jason中解析出Status集合*/
    public static ArrayList<Status> parseJsonArray(String response){
        ArrayList<Status> statuses = new ArrayList<Status>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                statuses.add(Status.parse(jsonObject));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return statuses;
    }

}


