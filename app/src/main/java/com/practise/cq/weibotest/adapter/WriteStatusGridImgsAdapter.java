package com.practise.cq.weibotest.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.widget.WrapHeightGridView;

import java.util.ArrayList;

/**
 * Created by CQ on 2016/5/23 0023.
 */
public class WriteStatusGridImgsAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Uri> imgUris;
    private WrapHeightGridView gv;

    private ImageLoader imageLoader;

    public WriteStatusGridImgsAdapter(Context context, WrapHeightGridView gv, ArrayList<Uri> imgUris) {
        this.context = context;
        this.imgUris = imgUris;
        this.gv = gv;

        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return imgUris.size() > 0 ? imgUris.size()+1 : 0 ;
    }

    @Override
    public Uri getItem(int position) {
        return imgUris.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_grid_view, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.iv_delete_image = (ImageView) convertView.findViewById(R.id.iv_delete_image);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

//        /**设置item的宽高*/
//        WrapHeightGridView gv = (WrapHeightGridView)parent;
//        int horizontalSpacing = gv.getHorizontalSpacing();
//        int columns = gv.getNumColumns();
//        int itemWidth = (gv.getWidth()-(columns-1)*horizontalSpacing
//                -gv.getPaddingLeft()-gv.getPaddingRight())/columns;
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemWidth, itemWidth);
//        holder.iv_image.setLayoutParams(params);

        if (position < getCount()-1){
            Uri item = getItem(position);
            holder.iv_image.setImageURI(item);

            holder.iv_delete_image.setVisibility(View.VISIBLE);
            holder.iv_delete_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgUris.remove(position);
                    notifyDataSetChanged();
                }
            });
        }else {
            holder.iv_image.setImageResource(R.drawable.compose_pic_add_more);
            holder.iv_delete_image.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static class ViewHolder{
        public ImageView iv_image;
        public ImageView iv_delete_image;
    }
}
