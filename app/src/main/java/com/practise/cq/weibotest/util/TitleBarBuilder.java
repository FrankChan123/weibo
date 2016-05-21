package com.practise.cq.weibotest.util;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.practise.cq.weibotest.R;

/**
 * Created by CQ on 2016/5/14 0014.
 */
public class TitleBarBuilder {

    private View titleBar;
    private ImageView ivLeft;
    private TextView tvLeft;
    private TextView tvTitle;
    private ImageView ivRight;
    private TextView tvRight;

    /**构造方法之一，传入Activity*/
    public TitleBarBuilder(Activity context){
        titleBar = context.findViewById(R.id.titlebar);
        ivLeft = (ImageView)titleBar.findViewById(R.id.titlebar_iv_left);
        tvLeft = (TextView)titleBar.findViewById(R.id.titlebar_tv_left);
        tvTitle = (TextView)titleBar.findViewById(R.id.titlebar_tv);
        ivRight = (ImageView)titleBar.findViewById(R.id.titlebar_iv_right);
        tvRight = (TextView)titleBar.findViewById(R.id.titlebar_tv_right);
    }

    /**构造方法之二，传入View*/
    public TitleBarBuilder(View view){
        titleBar = view.findViewById(R.id.titlebar);
        ivLeft = (ImageView)titleBar.findViewById(R.id.titlebar_iv_left);
        tvLeft = (TextView)titleBar.findViewById(R.id.titlebar_tv_left);
        tvTitle = (TextView)titleBar.findViewById(R.id.titlebar_tv);
        ivRight = (ImageView)titleBar.findViewById(R.id.titlebar_iv_right);
        tvRight = (TextView)titleBar.findViewById(R.id.titlebar_tv_right);
    }

    /**设置标题栏背景，传入resourceID*/
    public TitleBarBuilder setTitleBgRes(int resid) {
        titleBar.setBackgroundResource(resid);
        return this;
    }

    /**设置标题文字，若为空则不显示*/
    public TitleBarBuilder setTitleText(String text) {
        tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvTitle.setText(text);
        return this;
    }

    /**设置左侧图片，若为空则不显示*/
    public TitleBarBuilder setLeftImage(int resId) {
        ivLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        ivLeft.setImageResource(resId);
        return this;
    }

    /**设置左侧文字，若为空则不显示*/
    public TitleBarBuilder setLeftText(String text) {
        tvLeft.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvLeft.setText(text);
        return this;
    }

    /**为左侧文字（图片）注册点击事件监听器
     * 谁可见注册谁*/
    public TitleBarBuilder setLeftOnClickListener(View.OnClickListener listener) {
        if (ivLeft.getVisibility() == View.VISIBLE) {
            ivLeft.setOnClickListener(listener);
        } else if (tvLeft.getVisibility() == View.VISIBLE) {
            tvLeft.setOnClickListener(listener);
        }
        return this;
    }

    /**设置右侧图片，若为空则不显示*/
    public TitleBarBuilder setRightImage(int resId) {
        ivRight.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        ivRight.setImageResource(resId);
        return this;
    }

    /**设置右侧文字，若为空则不显示*/
    public TitleBarBuilder setRightText(String text) {
        tvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvRight.setText(text);
        return this;
    }

    /**为右侧文字（图片）注册点击事件监听器
     * 谁可见注册谁*/
    public TitleBarBuilder setRightOnClickListener(View.OnClickListener listener) {
        if (ivRight.getVisibility() == View.VISIBLE) {
            ivRight.setOnClickListener(listener);
        } else if (tvRight.getVisibility() == View.VISIBLE) {
            tvRight.setOnClickListener(listener);
        }
        return this;
    }


    public void setTitleBarEnable(boolean isTitleBarEnable){
        if (isTitleBarEnable == true){
            titleBar.setVisibility(View.VISIBLE);
        }else{
            titleBar.setVisibility(View.GONE);
        }
    }

}
