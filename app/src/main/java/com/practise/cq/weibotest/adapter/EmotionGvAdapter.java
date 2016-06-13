package com.practise.cq.weibotest.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.util.EmotionUtils;

import java.util.List;

/**
 * Created by CQ on 2016/6/4 0004.
 */
public class EmotionGvAdapter extends BaseAdapter{

    private Context mContext;
    private List<String> mEmotionNames;
    private int mItemWidth;

    public EmotionGvAdapter(Context context, List<String> emotionNames, int itemWidth) {
        mContext = context;
        mEmotionNames = emotionNames;
        mItemWidth = itemWidth;
    }

    @Override
    public int getCount() {
        return mEmotionNames.size() + 1;
    }

    @Override
    public String getItem(int position) {
        return mEmotionNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv = new ImageView(mContext);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(mItemWidth, mItemWidth);
        iv.setPadding(mItemWidth/8, mItemWidth/8, mItemWidth/8, mItemWidth/8);
        iv.setLayoutParams(params);

        if (position == getCount() - 1){
            iv.setImageResource(R.drawable.emotion_delete_sel);
        }else {
            String emotionName = mEmotionNames.get(position);
            iv.setImageResource(EmotionUtils.getImgByName(emotionName));
        }
        return iv;
    }
}
