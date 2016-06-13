package com.practise.cq.weibotest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.practise.cq.weibotest.R;

/**
 * Created by CQ on 2016/6/11 0011.
 */
public class TabIndicatorView extends LinearLayout {

    private int mCurrentPosition;

    public TabIndicatorView(Context context) {
        this(context, null);
    }

    public TabIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        for (int i=1; i<=4; i++){
            View child = new View(context);
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            child.setLayoutParams(params);
            child.setBackgroundResource(R.color.transparent);
            addView(child);
        }
    }

    public void setCurrentItem(int position){
        final View oldChild = getChildAt(mCurrentPosition);
        final View newChild = getChildAt(position);

        TranslateAnimation animation = new
                TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, position-mCurrentPosition,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                oldChild.setBackgroundResource(R.color.transparent);
                newChild.setBackgroundResource(R.color.orange);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        oldChild.setAnimation(animation);
        mCurrentPosition = position;
        invalidate();
    }

    public void setCurrentItemWithoutAnim(int position){
        View oldChild = getChildAt(mCurrentPosition);
        View newChild = getChildAt(position);

        oldChild.setBackgroundResource(R.color.transparent);
        newChild.setBackgroundResource(R.color.orange);

        mCurrentPosition = position;
        invalidate();
    }
}
