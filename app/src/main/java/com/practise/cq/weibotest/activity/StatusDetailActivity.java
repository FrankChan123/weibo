package com.practise.cq.weibotest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.adapter.StatusCommentAdapter;
import com.practise.cq.weibotest.adapter.StatusGridImgAdapter;
import com.practise.cq.weibotest.api.WeiboApi;
import com.practise.cq.weibotest.base.BaseActivity;
import com.practise.cq.weibotest.constants.AccessTokenKeeper;
import com.practise.cq.weibotest.constants.Constants;
import com.practise.cq.weibotest.entity.Comment;
import com.practise.cq.weibotest.entity.CommentResponse;
import com.practise.cq.weibotest.entity.PicUrls;
import com.practise.cq.weibotest.entity.Status;
import com.practise.cq.weibotest.entity.User;
import com.practise.cq.weibotest.util.DateUtils;
import com.practise.cq.weibotest.util.ImageOptionHelper;
import com.practise.cq.weibotest.util.StringUtils;
import com.practise.cq.weibotest.util.TitleBarBuilder;
import com.practise.cq.weibotest.util.ToastUtil;
import com.practise.cq.weibotest.widget.WrapHeightGridView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CQ on 2016/5/21 0021.
 */
public class StatusDetailActivity extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener{

    /**跳转至发送评论活动的请求码*/
    private static final int REQUEST_CODE_SEND_COMMENT = 12345;

    /**listView headerView - 微博信息*/
    private View status_detail_info;
    private ImageView iv_avatar;
    private TextView tv_subhead;
    private TextView tv_caption;
    private FrameLayout include_status_image;
    private WrapHeightGridView gv_images;
    private ImageView iv_image;
    private TextView tv_content;
    private View include_retweeted_status;
    private TextView tv_retweeted_content;
    private FrameLayout fl_retweeted_imageview;
    private GridView gv_retweeted_images;
    private ImageView iv_retweeted_image;

    /**shadow_tab - 顶部悬浮的菜单栏*/
    private View shadow_status_detail_tab;
    private RadioGroup shadow_rg_status_detail;
    private RadioButton shadow_rb_retweets;
    private RadioButton shadow_rb_comments;
    private RadioButton shadow_rb_likes;

    /**listView headerView - 添加至列表中作为header的菜单栏*/
    private View status_detail_tab;
    private RadioGroup rg_status_detail;
    private RadioButton rb_retweets;
    private RadioButton rb_comments;
    private RadioButton rb_likes;

    /**listView - 下拉刷新控件*/
    private PullToRefreshListView plv_status_detail;

    /**footView - 加载更多*/
    private View footView;
    private ProgressBar progressBar;

    /**bottom_control - 底部互动栏,包括转发/评论/点赞*/
    private LinearLayout ll_bottom_control;
    private LinearLayout ll_share_bottom;
    private TextView tv_share_bottom;
    private LinearLayout ll_comment_bottom;
    private TextView tv_comment_bottom;
    private LinearLayout ll_like_bottom;
    private TextView tv_like_bottom;

    private Status status;
    private boolean scroll2comment;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    /**评论当前已经加载的页数*/
    private long currentPage = 1;

    private List<Comment> datas = new ArrayList<Comment>();
    private StatusCommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_detail_activity);

        /**若点击首页微博信息底部操作栏“评论”选项，则直接进入评论界面*/
        status = (Status) getIntent().getSerializableExtra("status");
        scroll2comment = getIntent().getBooleanExtra("scroll2comment", false);

        initView();

        /**设置微博详情信息*/
        setStatusDetail();

        /**刷新控件底部添加“加载更多”视图*/
        addFootView(plv_status_detail, footView);
        loadData(1);

    }

    private void initView() {

        /**初始化标题栏*/
        initTitle();

        /**初始化listView headerView - 微博正文信息*/
        initDetailHead();

        /**初始化tab - 菜单栏，包括转发/评论/赞数量信息*/
        initTab();

        /**初始化listView - 转发/评论/赞详情列表，下拉刷新控件*/
        initListView();

        /**初始化bottom_control - 底部互动栏,包括转发/评论/点赞*/
        initControlBar();
    }

    private void initTitle() {
        new TitleBarBuilder(this)
                .setTitleText("微博正文")
                .setLeftImage(R.drawable.navigationbar_back_sel)
                .setLeftOnClickListener(this);
    }

    private void initDetailHead() {
        status_detail_info = View.inflate(this, R.layout.item_statues, null);
        status_detail_info.setBackgroundResource(R.color.white);

        /**此处微博正文部分底部操作栏隐藏掉*/
        status_detail_info.findViewById(R.id.ll_bottom_control).setVisibility(View.GONE);

        iv_avatar = (ImageView) status_detail_info.findViewById(R.id.iv_avatar);
        tv_subhead = (TextView) status_detail_info.findViewById(R.id.tv_subhead);
        tv_caption = (TextView) status_detail_info.findViewById(R.id.tv_caption);
        include_status_image = (FrameLayout) status_detail_info.findViewById(R.id.include_status_image);
        gv_images = (WrapHeightGridView) status_detail_info.findViewById(R.id.gv_images);
        iv_image = (ImageView) status_detail_info.findViewById(R.id.iv_image);
        tv_content = (TextView) status_detail_info.findViewById(R.id.tv_content);
        include_retweeted_status = status_detail_info.findViewById(R.id.include_retweeted_status);
        tv_retweeted_content = (TextView) status_detail_info.findViewById(R.id.tv_retweeted_content);
        fl_retweeted_imageview = (FrameLayout) include_retweeted_status.findViewById(R.id.include_status_image);
        gv_retweeted_images = (GridView) fl_retweeted_imageview.findViewById(R.id.gv_images);
        iv_retweeted_image = (ImageView) fl_retweeted_imageview.findViewById(R.id.iv_image);
        iv_image.setOnClickListener(this);

    }

    private void initTab() {

        /**悬浮导航栏先找到，一开始不加载*/
        shadow_status_detail_tab = findViewById(R.id.status_detail_tab);
        shadow_rg_status_detail = (RadioGroup) shadow_status_detail_tab
                .findViewById(R.id.rg_status_detail);
        shadow_rb_retweets = (RadioButton) shadow_status_detail_tab
                .findViewById(R.id.rb_retweets);
        shadow_rb_comments = (RadioButton) shadow_status_detail_tab
                .findViewById(R.id.rb_comments);
        shadow_rb_likes = (RadioButton) shadow_status_detail_tab
                .findViewById(R.id.rb_likes);
        shadow_rg_status_detail.setOnCheckedChangeListener(this);

        /**导航栏加载并引用控件*/
        status_detail_tab = View.inflate(this, R.layout.status_detail_tab, null);
        rg_status_detail = (RadioGroup) status_detail_tab
                .findViewById(R.id.rg_status_detail);
        rb_retweets = (RadioButton) status_detail_tab
                .findViewById(R.id.rb_retweets);
        rb_comments = (RadioButton) status_detail_tab
                .findViewById(R.id.rb_comments);
        rb_likes = (RadioButton) status_detail_tab
                .findViewById(R.id.rb_likes);
        rg_status_detail.setOnCheckedChangeListener(this);
    }

    private void initListView() {
        plv_status_detail = (PullToRefreshListView)findViewById(R.id.plv_status_detail);

        adapter = new StatusCommentAdapter(this, datas);
        plv_status_detail.setAdapter(adapter);

        /**载入"加载更多"视图*/
        footView = View.inflate(this, R.layout.foot_view, null);
        progressBar = (ProgressBar)footView.findViewById(R.id.load_progress);

        /**PullToRefreshListView本身并不是一个ListView
         * 该控件默认情况下包含一个刷新视图和一个ListView
         * ListView头部和尾部还能加入若干视图
         * 这里我们在头部先加入一个微博详情信息视图
         * 头部再加入一个导航栏
         * 视图位置按加入顺序往下排布*/
        final ListView lv = plv_status_detail.getRefreshableView();
        lv.addHeaderView(status_detail_info);
        lv.addHeaderView(status_detail_tab);

        /**下拉刷新监听器及监听逻辑*/
        plv_status_detail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(1);
            }
        });

        /**滑动至底部位置时，继续加载一页评论列表*/
        plv_status_detail.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                loadData(currentPage+1);
            }
        });

        /**监听滑动事件*/
        plv_status_detail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            /**这里的处理逻辑是：
             * 为了达到导航栏滑动至标题栏下面时，产生“被挡住而停止跟随继续向上”的效果
             * 当被添加在commentList上面的导航栏到达firstVisibleItem = 2的位置时
             * 让隐藏在主布局中标题栏下面的导航栏显示出来*/
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // 0-pullHead 1-detailHead 2-tab 3-commentList 4-footView
                if (firstVisibleItem >= 2) {
                    shadow_status_detail_tab.setVisibility(View.VISIBLE);
                }else {
                    shadow_status_detail_tab.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initControlBar() {
        ll_bottom_control = (LinearLayout) findViewById(R.id.status_detail_controlbar);
        ll_share_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_share_bottom);
        tv_share_bottom = (TextView) ll_bottom_control.findViewById(R.id.tv_share_bottom);
        ll_comment_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_comment_bottom);
        tv_comment_bottom = (TextView) ll_bottom_control.findViewById(R.id.tv_comment_bottom);
        ll_like_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_like_bottom);
        tv_like_bottom = (TextView) ll_bottom_control.findViewById(R.id.tv_like_bottom);
        ll_bottom_control.setBackgroundResource(R.color.white);
        ll_share_bottom.setOnClickListener(this);
        ll_comment_bottom.setOnClickListener(this);
        ll_like_bottom.setOnClickListener(this);
    }

    /**将intent传过来的status元素提取出来，显示在微博详情视图上*/
    private void setStatusDetail() {

        User user = status.getUser();

        /**微博正文信息*/
        imageLoader.displayImage(user.getProfile_image_url(), iv_avatar,
                ImageOptionHelper.getAvatarOptions());
        tv_subhead.setText(user.getName());
        tv_caption.setText(DateUtils.getShortTime(status.getCreated_at()) +
                "  来自" + Html.fromHtml(status.getSource()));

        setImages(status, include_status_image, gv_images, iv_image);

        if (TextUtils.isEmpty(status.getText())) {
            tv_content.setVisibility(View.GONE);
        } else {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(StringUtils.getWeiboContent(this, tv_content, status.getText()));
        }


        /**转发微博信息*/
        Status retweetedStatus = status.getRetweeted_status();
        if (retweetedStatus != null) {
            include_retweeted_status.setVisibility(View.VISIBLE);
            String retweetContent = "@" + retweetedStatus.getUser().getName()
                    + ":" + retweetedStatus.getText();
            tv_retweeted_content.setText(StringUtils.getWeiboContent(this, tv_retweeted_content,
                    retweetContent));
            setImages(retweetedStatus, fl_retweeted_imageview,
                    gv_retweeted_images, iv_retweeted_image);
        }else {
            include_retweeted_status.setVisibility(View.GONE);
        }

        /**shadow_tab - 顶部悬浮的菜单栏*/
        shadow_rb_retweets.setText("转发 " + status.getReposts_count());
        shadow_rb_comments.setText("评论 " + status.getComments_count());
        shadow_rb_likes.setText("赞 " + status.getAttitudes_count());
        // listView headerView - 添加至列表中作为header的菜单栏
        rb_retweets.setText("转发 " + status.getReposts_count());
        rb_comments.setText("评论 " + status.getComments_count());
        rb_likes.setText("赞 " + status.getAttitudes_count());

        /**bottom_control - 底部互动栏,包括转发/评论/点赞*/
        tv_share_bottom.setText(status.getReposts_count() == 0 ?
                "转发" : status.getReposts_count() + "");
        tv_comment_bottom.setText(status.getComments_count() == 0 ?
                "评论" : status.getComments_count() + "");
        tv_like_bottom.setText(status.getAttitudes_count() == 0 ?
                "赞" : status.getAttitudes_count() + "");
    }

    /**此处加载图片逻辑与首页基本一致*/
    private void setImages(final Status status, ViewGroup vgContainer,
                           GridView  gvImgs, ImageView ivImg) {
        if (status == null){
            return;
        }

        ArrayList<String> pic_urls = status.getPic_urls();
        ArrayList<PicUrls> picUrls = status.getPicUrls();

        String picUrl = status.getBmiddle_pic();

        if (pic_urls != null && pic_urls.size() == 1){
            vgContainer.setVisibility(View.VISIBLE);
            gvImgs.setVisibility(View.GONE);
            ivImg.setVisibility(View.VISIBLE);

            imageLoader.displayImage(picUrl, ivImg);
            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StatusDetailActivity.this, ImageBrowserActivity.class);
                    intent.putExtra("status", status);
                    startActivity(intent);
                }
            });
        }else if (pic_urls != null && pic_urls.size() > 1){
            vgContainer.setVisibility(View.VISIBLE);
            gvImgs.setVisibility(View.VISIBLE);
            ivImg.setVisibility(View.GONE);

            StatusGridImgAdapter imagesAdapter = new StatusGridImgAdapter(this, picUrls);
            gvImgs.setAdapter(imagesAdapter);

            gvImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(StatusDetailActivity.this, ImageBrowserActivity.class);
                    intent.putExtra("status", status);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }else {
            vgContainer.setVisibility(View.GONE);
        }
    }

    private void loadData(final long page) {
        WeiboApi api = new WeiboApi(this, Constants.APP_KEY,
                AccessTokenKeeper.readAccessToken(this));
        api.commentsShow(status.getId(), page, new RequestListener() {
            @Override
            public void onComplete(String response) {

                if (page == 1){
                    datas.clear();
                }

                Gson gson = new Gson();
                CommentResponse commentResponse = gson.fromJson(response, CommentResponse.class);
                tv_comment_bottom.setText(commentResponse.getTotal_number() == 0 ?
                        "评论" : commentResponse.getTotal_number() + "");
                shadow_rb_comments.setText("评论 " + commentResponse.getTotal_number());
                rb_comments.setText("评论 " + commentResponse.getTotal_number());
                addData(commentResponse);

//                try{
//                    List<Comment> comments = Comment.parse2List(response);
//                    commentResponse.setComments(comments);
//                    /**数据加入到适配器数据容器中*/
//                    addData(commentResponse);
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }

                /**判断是否需要滚动至评论部分*/
                if(scroll2comment) {
                    plv_status_detail.getRefreshableView().setSelection(2);
                    scroll2comment = false;
                }

                ToastUtil.show(StatusDetailActivity.this, "评论加载成功！", Toast.LENGTH_SHORT);

                plv_status_detail.onRefreshComplete();
            }


            @Override
            public void onWeiboException(WeiboException e) {

            }
        });
    }

    private void addData(CommentResponse response) {
//        if (comments.size() == 0){
//            ToastUtil.show(this, "还没有人评论~~", Toast.LENGTH_LONG);
//            return;
//        }

        /**将获取到的数据添加至列表中,重复数据不添加*/
        for (Comment comment : response.getComments()) {
            if (!datas.contains(comment)) {
                datas.add(comment);
            }
        }

        /**添加完后,通知ListView刷新页面数据*/
        adapter.notifyDataSetChanged();

        /**用条数判断,当前评论数是否达到指定评论数
         * 未达到则添加更多加载footView,反之移除*/
        if (datas.size() < response.getTotal_number()) {
            addFootView(plv_status_detail, footView);
        } else {
            removeFootView(plv_status_detail, footView);
        }
    }

    private void addFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if (lv.getFooterViewsCount() == 1) {
            lv.addFooterView(footView);
        }
    }

    private void removeFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if (lv.getFooterViewsCount() > 1) {
            lv.removeFooterView(footView);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titlebar_iv_left:
                StatusDetailActivity.this.finish();
                break;
            case R.id.ll_comment_bottom:
                Intent intent2comment = new Intent(this, WriteCommentActivity.class);
                intent2comment.putExtra("status", status);
                startActivityForResult(intent2comment, REQUEST_CODE_SEND_COMMENT);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        /**更新tab菜单栏某个选项时,注意header的菜单栏和shadow菜单栏的选中状态同步*/
        switch (checkedId) {
            case R.id.rb_retweets:
                rb_retweets.setChecked(true);
                shadow_rb_retweets.setChecked(true);
                break;
            case R.id.rb_comments:
                rb_comments.setChecked(true);
                shadow_rb_comments.setChecked(true);
                break;
            case R.id.rb_likes:
                rb_likes.setChecked(true);
                shadow_rb_likes.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_SEND_COMMENT:
                    boolean sendSuccess = getIntent().getBooleanExtra("sendSuccess", false);
                    if (sendSuccess){
                        scroll2comment = true;
                        loadData(1);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
