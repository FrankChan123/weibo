package com.practise.cq.weibotest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.entity.Comment;
import com.practise.cq.weibotest.entity.User;
import com.practise.cq.weibotest.util.DateUtils;
import com.practise.cq.weibotest.util.ToastUtil;

import java.util.List;

/**
 * Created by CQ on 2016/5/21 0021.
 */
public class StatusCommentAdapter extends BaseAdapter{


    private Context context;
    private List<Comment> datas;
    private ImageLoader imageLoader;

    public StatusCommentAdapter(Context context, List<Comment> datas) {
        this.context = context;
        this.datas = datas;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Comment getItem(int position) {
        return datas.get(position);
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
            convertView = View.inflate(context, R.layout.item_comment, null);
            holder.ll_comments = (LinearLayout) convertView
                    .findViewById(R.id.ll_comments);
            holder.iv_avatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            holder.tv_subhead = (TextView) convertView
                    .findViewById(R.id.tv_subhead);
            holder.tv_caption = (TextView) convertView
                    .findViewById(R.id.tv_caption);
            holder.tv_comment = (TextView) convertView
                    .findViewById(R.id.tv_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /**获取当前评论的用户信息*/
        if (datas != null) {
            Comment comment = getItem(position);
            User user = comment.getUser();
            imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatar);
            holder.tv_subhead.setText(user.getName());
            holder.tv_caption.setText(DateUtils.getShortTime(comment.getCreated_at()));
            holder.tv_comment.setText(comment.getText());

            /**点击评论*/
            holder.ll_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.show(context, "回复评论", Toast.LENGTH_LONG);
                }
            });
        }else {
            holder.tv_comment.setText("还没有人评论~~");
        }

        return convertView;
    }

     class ViewHolder{
        public LinearLayout ll_comments;
        public ImageView iv_avatar;
        public TextView tv_subhead;
        public TextView tv_caption;
        public TextView tv_comment;

    }
}
