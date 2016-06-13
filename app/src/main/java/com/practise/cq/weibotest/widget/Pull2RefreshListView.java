package com.practise.cq.weibotest.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by CQ on 2016/6/11 0011.
 */
public class Pull2RefreshListView extends PullToRefreshListView {

    private OnPlvScrollListener mListener;

    public Pull2RefreshListView(Context context) {
        super(context);
    }

    public Pull2RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Pull2RefreshListView(Context context, Mode mode) {
        super(context, mode);
    }

    public Pull2RefreshListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.OnScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnPlvScrollListener(OnPlvScrollListener listener){
        mListener = listener;
    }

    public static interface OnPlvScrollListener{
        void OnScrollChanged(int l, int t, int oldl, int oldt);
    }
}
