package com.practise.cq.weibotest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.practise.cq.weibotest.R;

import java.util.ArrayList;

/**
 * Created by CQ on 2016/5/18 0018.
 */
public class StatusGridImgAdapter extends BaseAdapter{

    private Context mcontext;
    private ArrayList<String> mpicurls;
    private ImageLoader imageLoader;

    public StatusGridImgAdapter(Context context, ArrayList<String> picUrls) {
        mcontext = context;
        mpicurls = picUrls;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return mpicurls.size();
    }

    @Override
    public String getItem(int position) {
        return mpicurls.get(position);
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
            convertView = View.inflate(mcontext, R.layout.item_gridview, null);
            holder.iv_image = (ImageView)convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        /**设置item宽高*/
//        GridView gv = (GridView)parent;
//        int horizontalSpacing = gv.getHorizontalSpacing();
//        int columns = gv.getNumColumns();
//        int itemWidth = (gv.getWidth()-(columns-1)*horizontalSpacing
//                -gv.getPaddingLeft()-gv.getPaddingRight())/columns;
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemWidth, itemWidth);
//        holder.iv_image.setLayoutParams(params);

        /**将图片加载到item的ImageView上*/
        String picUrl = mpicurls.get(position);
        imageLoader.displayImage(picUrl, holder.iv_image);

        return convertView;
    }

    public static class ViewHolder{
        private ImageView iv_image;
    }
}
