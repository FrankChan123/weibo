package com.practise.cq.weibotest.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.activity.StatusDetailActivity;
import com.practise.cq.weibotest.activity.WriteCommentActivity;
import com.practise.cq.weibotest.entity.Status;
import com.practise.cq.weibotest.entity.User;
import com.practise.cq.weibotest.util.DateUtils;
import com.practise.cq.weibotest.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by CQ on 2016/5/17 0017.
 */
public class StatusAdapter extends BaseAdapter {

    private Context mcontext;
    private List<Status> mdatas;

    private ImageLoader imageLoader;

    public StatusAdapter(Context context, List<Status> datas) {
        mcontext = context;
        mdatas = datas;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
//        return 1;
         return mdatas.size();
    }

    @Override
    public Status getItem(int position) {
        return mdatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
//            convertView = View.inflate(mcontext, R.layout.item_statues, null);
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_statues, null);

            /**各种引用控件*/
            holder.ll_card_content = (LinearLayout) convertView
                    .findViewById(R.id.ll_card_content);
            holder.iv_avatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            holder.rl_content = (RelativeLayout) convertView
                    .findViewById(R.id.rl_content);
            holder.tv_subhead = (TextView) convertView
                    .findViewById(R.id.tv_subhead);
            holder.tv_caption = (TextView) convertView
                    .findViewById(R.id.tv_caption);

            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.include_status_image = (FrameLayout) convertView
                    .findViewById(R.id.include_status_image);
            holder.gv_images = (GridView) holder.include_status_image
                    .findViewById(R.id.gv_images);
            holder.iv_image = (ImageView) holder.include_status_image
                    .findViewById(R.id.iv_image);

            holder.include_retweeted_status = (LinearLayout) convertView
                    .findViewById(R.id.include_retweeted_status);
            holder.tv_retweeted_content = (TextView) holder.include_retweeted_status
                    .findViewById(R.id.tv_retweeted_content);
            holder.include_retweeted_status_image = (FrameLayout) holder.include_retweeted_status
                    .findViewById(R.id.include_status_image);
            holder.gv_retweeted_images = (GridView) holder.include_retweeted_status_image
                    .findViewById(R.id.gv_images);
            holder.iv_retweeted_image = (ImageView) holder.include_retweeted_status_image
                    .findViewById(R.id.iv_image);

            holder.ll_share_bottom = (LinearLayout) convertView
                    .findViewById(R.id.ll_share_bottom);
            holder.iv_share_bottom = (ImageView) convertView
                    .findViewById(R.id.iv_share_bottom);
            holder.tv_share_bottom = (TextView) convertView
                    .findViewById(R.id.tv_share_bottom);
            holder.ll_comment_bottom = (LinearLayout) convertView
                    .findViewById(R.id.ll_comment_bottom);
            holder.iv_comment_bottom = (ImageView) convertView
                    .findViewById(R.id.iv_comment_bottom);
            holder.tv_comment_bottom = (TextView) convertView
                    .findViewById(R.id.tv_comment_bottom);
            holder.ll_like_bottom = (LinearLayout) convertView
                    .findViewById(R.id.ll_like_bottom);
            holder.iv_like_bottom = (ImageView) convertView
                    .findViewById(R.id.iv_like_bottom);
            holder.tv_like_bottom = (TextView) convertView
                    .findViewById(R.id.tv_like_bottom);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        /**为控件绑定数据*/
        final Status status = getItem(position);
        User user = status.getUser();

        /**发布者头像*/
        imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatar);

        /**发布者名字*/
        holder.tv_subhead.setText(user.getName());

        /**发布时间+发布渠道（设备）*/
        holder.tv_caption.setText(DateUtils.getShortTime(status.getCreated_at())
                + " 来自" + Html.fromHtml(status.getSource()));

        /**发布微博正文*/
        holder.tv_content.setText(status.getText());

        /**调用图片加载方法加载微博正文图片*/
        setImages(status, holder.include_status_image,
                holder.gv_images, holder.iv_image);

        /**若返回数据中转发字段不为空
         * 加载转发微博内容
         * 调用图片加载方法加载转发微博图片
         * 运行时该段代码时程序崩溃！！！*/
        final Status retweeted_status = status.getRetweeted_status();
        if (retweeted_status == null){
            holder.include_retweeted_status.setVisibility(View.GONE);
        }else {
            User reUser = retweeted_status.getUser();
            holder.include_retweeted_status.setVisibility(View.VISIBLE);
            holder.tv_retweeted_content.setText("@"+reUser.getName()+":"+retweeted_status.getText());
            setImages(retweeted_status, holder.include_retweeted_status_image,
                    holder.gv_retweeted_images, holder.iv_retweeted_image);
        }

        /**底部操作栏文字部分显示
         * 若对应操作数为0，则显示操作名称即可，否则，显示操作数*/
        holder.tv_share_bottom.setText(status.getReposts_count() == 0 ?
                "转发" : status.getReposts_count()+"");
        holder.tv_comment_bottom.setText(status.getComments_count() == 0 ?
                "评论" : status.getComments_count()+"");
        holder.tv_like_bottom.setText(status.getAttitudes_count() == 0 ?
                "赞" : status.getAttitudes_count()+"");

        /**微博正文注册点击事件监听器，跳转到微博详情页面*/
        holder.ll_card_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, StatusDetailActivity.class);
                intent.putExtra("status", status);
                mcontext.startActivity(intent);
            }
        });

        /**转发微博注册点击事件监听器，跳转到微博详情页面*/
        holder.include_retweeted_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, StatusDetailActivity.class);
                intent.putExtra("status", retweeted_status);
                mcontext.startActivity(intent);
            }
        });

        /**操作栏转发选项注册点击事件监听器，跳转发送微博界面*/
        holder.ll_share_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(mcontext, "转个发~", Toast.LENGTH_SHORT);
            }
        });

        holder.ll_comment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.getComments_count() > 0) {
                    Intent intent = new Intent(mcontext, StatusDetailActivity.class);
                    intent.putExtra("status", status);
                    intent.putExtra("scroll2Comment", true);
                    mcontext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mcontext, WriteCommentActivity.class);
                    intent.putExtra("status", status);
                    mcontext.startActivity(intent);
                }
                ToastUtil.show(mcontext, "评个论~", Toast.LENGTH_SHORT);
            }
        });

        holder.ll_like_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(mcontext, "点个赞~", Toast.LENGTH_SHORT);
            }
        });

        return convertView;
    }

    /**用于加载返回数据中多图及单图的方法
     * 判断多图集合中图片url对象的数量，若不止一个，则加载到GridView中
     * 若只有一个缩略图url对象，则加载到ImageView中*/
    private void setImages(Status status, FrameLayout imgContainer,
                           GridView gv_images, ImageView iv_image) {
        ArrayList<String> picUrls = status.getPic_urls();
        String thumbnail_pic = status.getThumbnail_pic();
        String bmiddle_pic = status.getBmiddle_pic();
//        String original_pic = status.getOriginal_pic();

        if (picUrls != null&&picUrls.size() > 1){
            imgContainer.setVisibility(View.VISIBLE);
            gv_images.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.GONE);

            StatusGridImgAdapter gvAdapter = new StatusGridImgAdapter(mcontext, picUrls);
            gv_images.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true));
            gv_images.setAdapter(gvAdapter);
//        }else if (bmiddle_pic != null){
//            imgContainer.setVisibility(View.VISIBLE);
//            gv_images.setVisibility(View.GONE);
//            iv_image.setVisibility(View.VISIBLE);
//
//            imageLoader.displayImage(bmiddle_pic, iv_image);
        }else if (thumbnail_pic != null){
            imgContainer.setVisibility(View.VISIBLE);
            gv_images.setVisibility(View.GONE);
            iv_image.setVisibility(View.VISIBLE);

            imageLoader.displayImage(thumbnail_pic, iv_image);
        }else {
            imgContainer.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder{
        public LinearLayout ll_card_content;
        public ImageView iv_avatar;
        public RelativeLayout rl_content;
        public TextView tv_subhead;
        public TextView tv_caption;

        public TextView tv_content;
        public FrameLayout include_status_image;
        public GridView gv_images;
        public ImageView iv_image;

        public LinearLayout include_retweeted_status;
        public TextView tv_retweeted_content;
        public FrameLayout include_retweeted_status_image;
        public GridView gv_retweeted_images;
        public ImageView iv_retweeted_image;

        public LinearLayout ll_share_bottom;
        public ImageView iv_share_bottom;
        public TextView tv_share_bottom;
        public LinearLayout ll_comment_bottom;
        public ImageView iv_comment_bottom;
        public TextView tv_comment_bottom;
        public LinearLayout ll_like_bottom;
        public ImageView iv_like_bottom;
        public TextView tv_like_bottom;
    }
}
