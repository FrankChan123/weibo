package com.practise.cq.weibotest.fragment;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.adapter.StatusAdapter;
import com.practise.cq.weibotest.api.WeiboApi;
import com.practise.cq.weibotest.base.BaseFragment;
import com.practise.cq.weibotest.constants.AccessTokenKeeper;
import com.practise.cq.weibotest.constants.Constants;
import com.practise.cq.weibotest.entity.Status;
import com.practise.cq.weibotest.entity.User;
import com.practise.cq.weibotest.util.ImageOptionHelper;
import com.practise.cq.weibotest.util.TitleBarBuilder;
import com.practise.cq.weibotest.widget.Pull2RefreshListView;
import com.practise.cq.weibotest.widget.TabIndicatorView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CQ on 2016/5/16 0016.
 */
public class MyInfoFragment extends BaseFragment
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    /**标题栏*/
    private View title;
    private ImageView titlebar_iv_left;
    private TextView titlebar_tv;

    /**headerView - 用户信息*/
    private View user_info_head;
    private ImageView iv_avatar;
    private TextView tv_name;
    private TextView tv_follows;
    private TextView tv_fans;
    private TextView tv_sign;

    /**shadow_tab - 顶部悬浮的菜单栏*/
    private View shadow_user_info_tab;
    private RadioGroup shadow_rg_user_info;
    private TabIndicatorView shadow_uliv_user_info;
    private View user_info_tab;
    private RadioGroup rg_user_info;
    private TabIndicatorView uliv_user_info;

    /**headerView - 添加至列表中作为header的菜单栏*/
    private ImageView iv_user_info_head;
    private Pull2RefreshListView plv_user_info;
    private View footView;

    /**用户相关信息*/
    private boolean isCurrentUser;
    private User user;
    private String userName;

    /**个人微博列表*/
    private List<Status> statuses = new ArrayList<Status>();
    private StatusAdapter statusAdapter;
    private long curPage = 1;

    /**背景图片最小高度*/
    private int minImageHeight = -1;

    /**背景图片最大高度*/
    private int maxImageHeight = -1;

    private int curScrollY;

    private View view;
    private ImageLoader imageLoader;
    private WeiboApi api;
    private long currentPage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.myinfo_fragment, null);

        initView();

        return view;
    }

    private void initView(){
        title = new TitleBarBuilder(activity)
                .setTitleBgRes(R.color.transparent)
                .setLeftImage(R.drawable.navigationbar_back_sel)
                .setRightOnClickListener(this)
                .build();
        titlebar_iv_left = (ImageView)title.findViewById(R.id.titlebar_iv_left);
        titlebar_tv = (TextView)title.findViewById(R.id.titlebar_tv);
        imageLoader = ImageLoader.getInstance();

        initInfoHead();
        initTab();
        initListView();
    }

    /**初始化用户信息*/
    private void initInfoHead() {
        iv_user_info_head = (ImageView)view.findViewById(R.id.iv_user_info_head);

        user_info_head = View.inflate(activity, R.layout.user_info_head, null);
        iv_avatar = (ImageView) user_info_head.findViewById(R.id.iv_avatar);
        tv_name = (TextView) user_info_head.findViewById(R.id.tv_name);
        tv_follows = (TextView) user_info_head.findViewById(R.id.tv_follows);
        tv_fans = (TextView) user_info_head.findViewById(R.id.tv_fans);
        tv_sign = (TextView) user_info_head.findViewById(R.id.tv_sign);
    }

    /**初始化菜单栏*/
    private void initTab() {

        /**悬浮显示的菜单栏*/
        shadow_user_info_tab = view.findViewById(R.id.user_info_tab);
        shadow_rg_user_info = (RadioGroup) shadow_user_info_tab.findViewById(R.id.rg_user_info);
        shadow_uliv_user_info = (TabIndicatorView) shadow_user_info_tab.findViewById(R.id.uliv_user_info);
        shadow_rg_user_info.setOnCheckedChangeListener(this);
        shadow_uliv_user_info.setCurrentItemWithoutAnim(1);

        /**标题栏下面固定的菜单栏*/
        user_info_tab = View.inflate(activity, R.layout.user_info_tab, null);
        rg_user_info = (RadioGroup) user_info_tab.findViewById(R.id.rg_user_info);
        uliv_user_info = (TabIndicatorView) user_info_tab.findViewById(R.id.uliv_user_info);

        rg_user_info.setOnCheckedChangeListener(this);
        uliv_user_info.setCurrentItemWithoutAnim(1);
    }

    private void initListView() {
        plv_user_info = (Pull2RefreshListView)view.findViewById
                (R.id.plv_user_info);
        initLoadingLayout();
        footView = View.inflate(activity, R.layout.foot_view, null);
        final ListView lv = plv_user_info.getRefreshableView();
        statusAdapter = new StatusAdapter(activity, statuses);
        plv_user_info.setAdapter(statusAdapter);

        lv.addHeaderView(user_info_head);
        lv.addHeaderView(user_info_tab);

        /**下拉刷新，加载最新微博*/
        plv_user_info.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                loadStatus(1);
            }
        });

        /**上拉加载上一页微博*/
        plv_user_info.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                loadStatus(curPage + 1);
            }
        });

        /**下拉时，头图变化逻辑*/
        plv_user_info.setOnPlvScrollListener(new Pull2RefreshListView.OnPlvScrollListener() {
            @Override
            public void OnScrollChanged(int l, int t, int oldl, int oldt) {
                int scrollY = curScrollY = t;
                if (minImageHeight == -1){
                    minImageHeight = iv_user_info_head.getHeight();
                }
                if (maxImageHeight == -1){
                    Rect rect = iv_user_info_head.getDrawable().getBounds();
                    maxImageHeight = rect.bottom - rect.top;
                }

                if (minImageHeight - scrollY < maxImageHeight){
                    iv_user_info_head.layout(0, 0,
                            iv_user_info_head.getWidth(), minImageHeight-scrollY);
                }else {
                    iv_user_info_head.layout(0, -scrollY - (maxImageHeight - minImageHeight),
                            iv_user_info_head.getWidth(),
                            -scrollY - (maxImageHeight - minImageHeight) + iv_user_info_head.getHeight());
                }
            }
        });

        iv_user_info_head.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (curScrollY == bottom - oldBottom){
                    iv_user_info_head.layout(0, 0, iv_user_info_head.getWidth(), oldBottom);
                }
            }
        });

        plv_user_info.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                /**上滑时，头图也向上滑动*/
                iv_user_info_head.layout(0, user_info_head.getTop(),
                        iv_user_info_head.getWidth(), user_info_head.getTop() + iv_user_info_head.getHeight());

                if(user_info_head.getBottom() < title.getBottom()) {
                    shadow_user_info_tab.setVisibility(View.VISIBLE);
                    title.setBackgroundResource(R.color.transparent);
                    titlebar_iv_left.setImageResource(R.drawable.navigationbar_back_sel);
                    titlebar_tv.setVisibility(View.VISIBLE);
                } else {
                    shadow_user_info_tab.setVisibility(View.GONE);
                    title.setBackgroundResource(R.color.transparent);
                    titlebar_iv_left.setImageResource(R.drawable.userinfo_navigationbar_back_sel);
                    titlebar_tv.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            loadData();
        }
    }

    /**将刷新提示信息清空*/
    private void initLoadingLayout() {
        ILoadingLayout loadingLayout = plv_user_info.getLoadingLayoutProxy();
        loadingLayout.setPullLabel("");
        loadingLayout.setRefreshingLabel("");
        loadingLayout.setReleaseLabel("");
        loadingLayout.setLoadingDrawable(new ColorDrawable(
                getResources().getColor(R.color.transparent)));
    }

    private void loadData() {
        loadUserInfo();
//        loadStatus(1);
    }

    private void loadUserInfo() {
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity);
        String uid = accessToken.getUid();
        api = new WeiboApi(activity, Constants.APP_KEY, accessToken);

        api.userShow(uid, "", new RequestListener() {
            @Override
            public void onComplete(String response) {
                user = User.parse(response);
                setUserInfo();
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        });


    }

    private void setUserInfo() {
        if (user == null){
            return;
        }
        tv_name.setText(user.getName());
        titlebar_tv.setText(user.getName());
        imageLoader.displayImage(user.getAvatar_large(), new ImageViewAware(iv_avatar),
                ImageOptionHelper.getAvatarOptions());
        tv_follows.setText("关注 " + user.getFriends_count());
        tv_fans.setText("粉丝 " + user.getFollowers_count());
        tv_sign.setText("简介:" + user.getDescription());
    }

    /**加载微博*/
    private void loadStatus(final long page) {
        api.statusUser_timeline(user == null ? -1 : user.getId(), user.getScreen_name(), page,
                new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (page == 1){
                    statuses.clear();
                }
                currentPage = page;

                try {
                    addStatus(Status.parse2List(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        });
    }

    private void addStatus(List<Status> statuses) {
        for (Status status : statuses){
            if (!statuses.contains(status)){
                statuses.add(status);
            }
        }
        statusAdapter.notifyDataSetChanged();

        if (currentPage < 50){
            addFootView(plv_user_info, footView);
        }else {
            removeFootView(plv_user_info, footView);
        }
    }

    private void addFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() == 1) {
            lv.addFooterView(footView);
        }
    }

    private void removeFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() > 1) {
            lv.removeFooterView(footView);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titlebar_iv_left:
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = group.indexOfChild(group.findViewById(checkedId));

        if(shadow_user_info_tab.getVisibility() == View.VISIBLE) {
            shadow_uliv_user_info.setCurrentItem(index);

            ((RadioButton)rg_user_info.findViewById(checkedId)).setChecked(true);
            uliv_user_info.setCurrentItemWithoutAnim(index);
        } else {
            uliv_user_info.setCurrentItem(index);

            ((RadioButton)shadow_rg_user_info.findViewById(checkedId)).setChecked(true);
            shadow_uliv_user_info.setCurrentItemWithoutAnim(index);
        }
    }
}
