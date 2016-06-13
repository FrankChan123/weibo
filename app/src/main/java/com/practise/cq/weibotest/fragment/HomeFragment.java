package com.practise.cq.weibotest.fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.adapter.StatusAdapter;
import com.practise.cq.weibotest.api.WeiboApi;
import com.practise.cq.weibotest.base.BaseFragment;
import com.practise.cq.weibotest.constants.AccessTokenKeeper;
import com.practise.cq.weibotest.constants.Constants;
import com.practise.cq.weibotest.entity.Status;
import com.practise.cq.weibotest.util.DateUtils;
import com.practise.cq.weibotest.util.TitleBarBuilder;
import com.practise.cq.weibotest.util.ToastUtil;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CQ on 2016/5/16 0016.
 */
public class HomeFragment extends BaseFragment {

    private View view;
    private PullToRefreshListView plv_home;
    private TextView testContentDisplay;
    private View footView;
    private ListView lv;

    private StatusAdapter adapter;
    private List<Status> data;

    private long lastRefreshTime = 0;
    private int currentPage = 1;

    private static final String LAST_REFRESHED_AT = "last_refreshed_at";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initView();
        loadData(1);

        return view;
    }

    private void initView() {
        view = View.inflate(activity, R.layout.home_fragment, null);
        plv_home = (PullToRefreshListView) view.findViewById(R.id.plv_home);
        testContentDisplay = (TextView)view.findViewById(R.id.test_content_display);
        testContentDisplay.setMovementMethod(new ScrollingMovementMethod());
        lv = plv_home.getRefreshableView();

        new TitleBarBuilder(view)
                .setTitleText("首页");

        data = new ArrayList<Status>();
        adapter = new StatusAdapter(activity, data);
        plv_home.setAdapter(adapter);

        /**注入下拉刷新监听器，实现刷新方法
         * 载入第一页（即最新的一页）*/
        plv_home.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                /**获取上次刷新时间信息*/
                lastRefreshTime = pf.getLong(LAST_REFRESHED_AT, 0);
                if (lastRefreshTime != 0) {
                    String lastRefreshInfo = DateUtils.getRefreshTimeInfo(lastRefreshTime, System.currentTimeMillis());

                    plv_home.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新时间："+lastRefreshInfo);
                }else {
                    plv_home.getLoadingLayoutProxy().setLastUpdatedLabel("刚刚刷新");
                }
                /**载入最新的一页*/
                loadData(1);
            }
        });

        /**注入上拉至底部item时的监听器，实现上拉方法
         * 载入上一页，即第二页*/
        plv_home.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                loadData(currentPage+1);
            }
        });

        footView = View.inflate(activity, R.layout.foot_view, null);
    }

    private void loadData(final int page) {

        WeiboApi api = new WeiboApi(activity, Constants.APP_KEY,
                AccessTokenKeeper.readAccessToken(activity));
        api.statusesFriends_timeline(6, page, new RequestListener() {
            @Override
            public void onComplete(String response) {

//                testContentDisplay.setText(response);

                /**json数据解析测试代码，勿删！！！*/
//                try{
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.optJSONArray("statuses");
//                    JSONObject tmpJsonObject = null;
//                    for (int i=0; i<jsonArray.length(); i++) {
//                        tmpJsonObject = jsonArray.optJSONObject(0);
//                        Status retweeted_status = Status.parse(tmpJsonObject.optJSONObject("retweeted_status"));
//                        if (retweeted_status == null){
//                            testContentDisplay.setText("retweeted_status不存在！");
//                        }else {
//                            testContentDisplay.setText(retweeted_status.getText());
//                        }
//
////                        data.add(status);
////                        adapter.notifyDataSetChanged();
//                    }
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }

                /**测试json数据中是否有多图url*/
//                try {
//                    List<Status> statuses = Status.parse2List(response);
//                    ArrayList<String> urls = null;
//                    for (Status status : statuses){
//                        urls = status.getPic_urls();
//                        if (urls == null){
//                            testContentDisplay.setText("该微博没有多图！");
//                        }else {
//                            testContentDisplay.setText("");
//                            for (String url : urls) {
//                                testContentDisplay.append(url);
//                            }
//                            break;
//                        }
//                    }
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }

                /**每次请求第一页数据时，将数据集合清空，保证第一页总是在最前面显示*/
                if (page == 1){
                    data.clear();
                }
                currentPage = page;

                try {
                    addData(Status.parse2List(response));
                }catch (JSONException e){
                    e.printStackTrace();
                }
                ToastUtil.show(activity, "数据加载成功！", Toast.LENGTH_SHORT);

                /**记录本次刷新时间*/
                lastRefreshTime = System.currentTimeMillis();
                pf.edit().putLong(LAST_REFRESHED_AT, lastRefreshTime).apply();

                /**刷新完成后，通知控件已经完成操作*/
                plv_home.onRefreshComplete();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.show(activity, e.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    /**将网络请求返回的Jason数据解析后，添加到集合中，并通知适配器更新*/
    private void addData(List<Status> response){
        for (Status status : response){
            if (!data.contains(status)){
                data.add(status);
            }
        }
        adapter.notifyDataSetChanged();

        if (currentPage <= 50){
            addFootView(plv_home, footView);
        }else {
            removeFootView(plv_home, footView);
        }
    }

    private void addFootView(PullToRefreshListView plv_home, View footView){
        ListView lv = plv_home.getRefreshableView();
        if (lv.getFooterViewsCount() == 1){
            lv.addFooterView(footView);
        }
    }

    private void removeFootView(PullToRefreshListView plv_home, View footView) {
        ListView lv = plv_home.getRefreshableView();
        if (lv.getFooterViewsCount() > 1){
            lv.removeFooterView(footView);
        }
    }
}
