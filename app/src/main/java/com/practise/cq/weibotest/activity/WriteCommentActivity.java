package com.practise.cq.weibotest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.api.WeiboApi;
import com.practise.cq.weibotest.base.BaseActivity;
import com.practise.cq.weibotest.constants.AccessTokenKeeper;
import com.practise.cq.weibotest.constants.Constants;
import com.practise.cq.weibotest.entity.Status;
import com.practise.cq.weibotest.util.TitleBarBuilder;
import com.practise.cq.weibotest.util.ToastUtil;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

/**
 * Created by CQ on 2016/5/22 0022.
 */
public class WriteCommentActivity extends BaseActivity implements View.OnClickListener{

    /**评论编辑框*/
    private EditText edit_comment;

    /**底部辅助栏*/
    private ImageView iv_insert_pic;
    private ImageView iv_at;
    private ImageView iv_topic;
    private ImageView iv_emoji;
    private ImageView iv_add_more;

    /**待评论的微博*/
    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_comment_activity);

        status = (Status) getIntent().getSerializableExtra("status");

        initView();
    }

    private void initView() {
        new TitleBarBuilder(this)
                .setTitleText("发评论")
                .setLeftText("取消")

                /**点击左侧“取消”，返回上一活动*/
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WriteCommentActivity.this.finish();
                    }
                })
                .setRightText("发送")

                /**点击右侧“发送”，内容非空则发送*/
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendComment();
                    }
                });

        edit_comment = (EditText) findViewById(R.id.et_write_status);
        iv_insert_pic = (ImageView) findViewById(R.id.iv_image);
        iv_at = (ImageView) findViewById(R.id.iv_at);
        iv_topic = (ImageView) findViewById(R.id.iv_topic);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        iv_add_more = (ImageView) findViewById(R.id.iv_add);

        iv_insert_pic.setOnClickListener(this);
        iv_at.setOnClickListener(this);
        iv_topic.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_add_more.setOnClickListener(this);
    }

    private void sendComment() {
        String comment2send = String.valueOf(edit_comment.getText());
        if (TextUtils.isEmpty(comment2send)){
            ToastUtil.show(this, "发送内容不能为空！", Toast.LENGTH_LONG);
            return;
        }

        WeiboApi api = new WeiboApi(this, Constants.APP_KEY,
                AccessTokenKeeper.readAccessToken(this));
        api.commentCreate(comment2send, status.getId(), new RequestListener() {
            @Override
            public void onComplete(String response) {
                ToastUtil.show(WriteCommentActivity.this, "评论发送成功！", Toast.LENGTH_LONG);

                Intent intent4result = new Intent();
                intent4result.putExtra("sendSuccess", true);
                setResult(RESULT_OK, intent4result);

                WriteCommentActivity.this.finish();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.show(WriteCommentActivity.this, e.getMessage(), Toast.LENGTH_LONG);
            }
        });

    }

    @Override
    public void onClick(View view) {

    }
}
