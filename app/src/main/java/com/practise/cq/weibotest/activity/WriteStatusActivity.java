package com.practise.cq.weibotest.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.adapter.EmotionGvAdapter;
import com.practise.cq.weibotest.adapter.ViewPagerAdapter;
import com.practise.cq.weibotest.adapter.WriteStatusGridImgsAdapter;
import com.practise.cq.weibotest.api.WeiboApi;
import com.practise.cq.weibotest.base.BaseActivity;
import com.practise.cq.weibotest.constants.AccessTokenKeeper;
import com.practise.cq.weibotest.constants.Constants;
import com.practise.cq.weibotest.entity.Status;
import com.practise.cq.weibotest.util.DialogUtils;
import com.practise.cq.weibotest.util.DisplayUtils;
import com.practise.cq.weibotest.util.EmotionUtils;
import com.practise.cq.weibotest.util.ImgUtils;
import com.practise.cq.weibotest.util.StringUtils;
import com.practise.cq.weibotest.util.TitleBarBuilder;
import com.practise.cq.weibotest.util.ToastUtil;
import com.practise.cq.weibotest.widget.WrapHeightGridView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CQ on 2016/5/23 0023.
 */
public class WriteStatusActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    /**微博编辑区*/
    private EditText et_write_status;

    /**添加的九宫格图片*/
    private WrapHeightGridView gv_write_status;

    /**转发微博内容显示区*/
    private View include_retweeted_status_card;
    private ImageView iv_rstatus_img;;
    private TextView tv_rstatus_username;;
    private TextView tv_rstatus_content;;

    /**底部辅助添加栏*/
    private ImageView iv_image;
    private ImageView iv_at;
    private ImageView iv_topic;
    private ImageView iv_emoji;
    private ImageView iv_add;

    /**表情选择面板*/
    private LinearLayout ll_emotion_dashboard;
    private ViewPager vp_emotion_dashboard;

    /**进度框*/
    private ProgressDialog progressDialog;

    /**转发微博*/
    private Status retweeted_status;
    /**转发微博中包含的转发微博*/
    private Status cardStatus;
    
    /**待发送的微博*/
    private Status status2send;
    
    private WriteStatusGridImgsAdapter adapter;
    private ArrayList<Uri> imgUris = new ArrayList<Uri>();
    private ViewPagerAdapter emotionAdapter;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private boolean isPicInserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.write_status_activity);
        
        retweeted_status = (Status)getIntent().getSerializableExtra("status");
        
        initView();
    }

    private void initView() {
        new TitleBarBuilder(this)
                .setTitleText("发微博")
                .setLeftText("取消")
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(et_write_status.getText())||isPicInserted){
                            DialogUtils.showConfirmDialog(WriteStatusActivity.this, "确定退出编辑吗？", null,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            WriteStatusActivity.this.finish();
                                        }
                                    });
                        }else {
                            WriteStatusActivity.this.finish();
                        }
                    }
                })
                .setRightText("发送")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendStatus();
                    }
                });

        /**输入框*/
        et_write_status = (EditText) findViewById(R.id.et_write_status);

        /**添加的九宫格图片*/
        gv_write_status = (WrapHeightGridView) findViewById(R.id.gv_write_status);

        /**转发微博内容*/
        include_retweeted_status_card = findViewById(R.id.include_retweeted_status_card);
        iv_rstatus_img = (ImageView) findViewById(R.id.iv_rstatus_img);
        tv_rstatus_username = (TextView) findViewById(R.id.tv_rstatus_username);
        tv_rstatus_content = (TextView) findViewById(R.id.tv_rstatus_content);

        /**底部添加栏*/
        iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_at = (ImageView) findViewById(R.id.iv_at);
        iv_topic = (ImageView) findViewById(R.id.iv_topic);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        iv_add = (ImageView) findViewById(R.id.iv_add);

        /**进度框*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("微博发布中...");

        /**表情面板*/
        vp_emotion_dashboard = (ViewPager)findViewById(R.id.vp_emotion_dashboard);
        ll_emotion_dashboard = (LinearLayout)findViewById(R.id.ll_emotion_dashboard);


        adapter = new WriteStatusGridImgsAdapter(this, gv_write_status, imgUris);
        gv_write_status.setAdapter(adapter);

        gv_write_status.setOnItemClickListener(this);
        iv_image.setOnClickListener(this);
        iv_at.setOnClickListener(this);
        iv_topic.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_add.setOnClickListener(this);

        initRetweetedStatus();
        initEmotion();
    }

    /**设置转发微博显示*/
    private void initRetweetedStatus() {

        /**转发微博特殊处理*/
        if(retweeted_status != null) {
            /**转发的微博是否包含转发内容*/
            Status rrStatus = retweeted_status.getRetweeted_status();
            if(rrStatus != null) {
                String content = "//@" + retweeted_status.getUser().getName()
                        + ":" + retweeted_status.getText();
                et_write_status.setText(content);
                /**如果引用的为转发微博,则使用它转发的内容*/
                cardStatus = rrStatus;
            } else {
                et_write_status.setText("转发微博");
                /**如果引用的为原创微博,则使用它自己的内容*/
                cardStatus = retweeted_status;
            }

            /**设置转发图片内容*/
            String imgUrl = cardStatus.getThumbnail_pic();
            if(TextUtils.isEmpty(imgUrl)) {
                iv_rstatus_img.setVisibility(View.GONE);
            } else {
                iv_rstatus_img.setVisibility(View.VISIBLE);
                imageLoader.displayImage(cardStatus.getThumbnail_pic(), iv_rstatus_img);
            }
            /**设置转发文字内容*/
            tv_rstatus_username.setText("@" + cardStatus.getUser().getName());
            tv_rstatus_content.setText(cardStatus.getText());

            /**转发微博时,不能添加图片*/
            iv_image.setVisibility(View.GONE);
            include_retweeted_status_card.setVisibility(View.VISIBLE);
        }
    }

    /**发送微博*/
    private void sendStatus() {
        String status2send = String.valueOf(et_write_status.getText());
        if (status2send == null){
            ToastUtil.show(this, "发送内容不能为空！", Toast.LENGTH_LONG);
            return;
        }

        /**获取上传图片的绝对路径*/
        Bitmap bitmap2send = null;
        if (imgUris.size() > 0){
            Uri uri = imgUris.get(0);
            bitmap2send = ImgUtils.getBitmapFromUri(this, uri);
        }

        /**获取转发微博的id*/
        long retweetedId = 0;
        if (cardStatus == null){
            retweetedId = -1;
        }else {
            retweetedId = cardStatus.getId();
        }

        progressDialog.show();
        WeiboApi api = new WeiboApi(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        api.sendStatus(status2send, bitmap2send, retweetedId, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ToastUtil.show(WriteStatusActivity.this, "微博发送成功！", Toast.LENGTH_LONG);
                progressDialog.dismiss();
                setResult(RESULT_OK);
                WriteStatusActivity.this.finish();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.show(WriteStatusActivity.this, e.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }

    /**初始化表情面板*/
    private void initEmotion(){
        int gvWidth = DisplayUtils.getScreenWidthPixels(this);
        int spacing = DisplayUtils.dp2px(this, 8);
        int itemWidth = (gvWidth - spacing * 8) / 7;
        int gvHeight = itemWidth * 3 + spacing * 4;

        List<GridView> gvs = new ArrayList<GridView>();
        List<String> emotionNames = new ArrayList<String>();


        /**取出表情键值字符串，每20个一组，放入gridView中，最后一组可能不满20*/
        for (String emotionName : EmotionUtils.emojiMap.keySet()){
            emotionNames.add(emotionName);
            if (emotionNames.size() == 20){
                GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing,
                        itemWidth, gvHeight);
                gvs.add(gv);
                emotionNames = new ArrayList<String>();
            }
        }

        if (emotionNames.size() >0){
            GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing,
                    itemWidth, gvHeight);
            gvs.add(gv);
        }

        emotionAdapter = new ViewPagerAdapter(gvs);
        vp_emotion_dashboard.setAdapter(emotionAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gvWidth, gvHeight);
        vp_emotion_dashboard.setLayoutParams(params);

    }

    /**创建显示表情的GridView*/
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth,
                                           int padding, int itemWidth, int gvHeight) {
        GridView gv = new GridView(this);

        /**基本设置*/
        gv.setBackgroundResource(R.color.bg_gray);
        gv.setSelector(R.color.transparent);
        gv.setNumColumns(7);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);

        /**数据绑定*/
        EmotionGvAdapter emotionGvAdapter = new EmotionGvAdapter(this, emotionNames, itemWidth);
        gv.setAdapter(emotionGvAdapter);
        gv.setOnItemClickListener(this);

        return gv;
    }

    /**此处执行两个逻辑
     * 一是添加照片
     * 二是添加表情
     * 需要分别对adapter实例进行判断*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAdapter = parent.getAdapter();
        if (itemAdapter instanceof WriteStatusGridImgsAdapter){
            if (position == adapter.getCount()-1) {
                DialogUtils.showPickImgDialog(this);
            }
        }else if (itemAdapter instanceof EmotionGvAdapter){
            EmotionGvAdapter gvAdapter = (EmotionGvAdapter)itemAdapter;
            if (position == gvAdapter.getCount() - 1){

                /**给当前输入区域分发键盘事件
                 * 对应动作是按下，处理方式删除*/
                et_write_status.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }else {

                /**将当前点击表情的对应String与输入区域内容转化为SpannableString
                 * 设置到输入区域，同时光标定位到表情String后面位置*/
                String emotionName = gvAdapter.getItem(position);
                int curPosition = et_write_status.getSelectionStart();

                StringBuilder sb = new StringBuilder(String.valueOf(et_write_status.getText()));
                sb.insert(curPosition, emotionName);

                SpannableString spannableString = StringUtils.getWeiboContent(this, et_write_status, sb.toString());
                et_write_status.setText(spannableString);
                et_write_status.setSelection(curPosition + emotionName.length());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_image:
                DialogUtils.showPickImgDialog(this);
                break;
            case R.id.iv_emoji:
                if (ll_emotion_dashboard.getVisibility() == View.VISIBLE){
                    iv_emoji.setImageResource(R.drawable.btn_insert_emotion);
                    ll_emotion_dashboard.setVisibility(View.GONE);
                }else {
                    iv_emoji.setImageResource(R.drawable.btn_insert_keyboard_sel);
                    ll_emotion_dashboard.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case ImgUtils.PIC_FROM_CAMERA:
                if (resultCode == RESULT_CANCELED){
                    ImgUtils.deleteImgUri(this, ImgUtils.imgFromCameraUri);
                }else {
                    imgUris.add(ImgUtils.imgFromCameraUri);
                    isPicInserted = true;
                    updateImgs();
                }
                break;
            case ImgUtils.PIC_FROM_ALBUM:
                if (requestCode == RESULT_CANCELED){
                    return;
                }else {
                    imgUris.add(data.getData());
                    isPicInserted = true;
                    updateImgs();
                }
        }

    }

    /**每次进行照片添加操作后，检查imgUris中uri的数量
     * 为空，则不显示九宫格
     * 否则显示并更新*/
    private void updateImgs() {
        if (imgUris.size() > 0){
            gv_write_status.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }else {
            gv_write_status.setVisibility(View.GONE);
        }
    }
}
