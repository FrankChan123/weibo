package com.practise.cq.weibotest.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by CQ on 2016/6/1 0001.
 */
public class ViewPagerAdapter extends PagerAdapter{

    private List<GridView> mGvs;

    public ViewPagerAdapter(List<GridView> gvs) {
        mGvs = gvs;
    }

    @Override
    public int getCount() {
        return mGvs.size();
    }

    @Override
    public GridView instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(mGvs.get(position));
        return mGvs.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(mGvs.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
