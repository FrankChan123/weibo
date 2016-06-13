package com.practise.cq.weibotest.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.entity.PicUrls;
import com.practise.cq.weibotest.util.DisplayUtils;

import java.util.ArrayList;

/**
 * Created by CQ on 2016/6/6 0006.
 */
public class ImageBrowserAdapter extends PagerAdapter {

    private Activity mContext;
    private ArrayList<PicUrls> mPics;

    /**每个url数据对应的视图*/
    private ArrayList<View> picViews = new ArrayList<View>();

    private ImageLoader imageLoader;

    public ImageBrowserAdapter(Activity context, ArrayList<PicUrls> pics) {
        mContext = context;
        mPics = pics;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (mPics.size() > 1){
            return Integer.MAX_VALUE;
        }
        return mPics.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        for (int i=0; i<mPics.size(); i++){
            View view = View.inflate(mContext, R.layout.item_image_browser, null);
            picViews.add(view);
        }

        int index = position % mPics.size();
        View view = picViews.get(index);
        PicUrls picUrl = mPics.get(index);
        final ImageView iv_image_browser = (ImageView)view.findViewById(R.id.iv_image_browser);

        /**若图片浏览时点击“原图”，则isShowOriImg为true
         * 此时应使用Original_pic_url
         * 否则，使用Bmiddle_pic_url*/
        String url = picUrl.isShowOriImg() ? picUrl.getOriginal_pic() : picUrl.getBmiddle_pic();
        imageLoader.loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                iv_image_browser.setImageResource(R.mipmap.timeline_image_failure);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                float scale = (float) loadedImage.getHeight() / loadedImage.getWidth();
                int screenWidth = DisplayUtils.getScreenWidthPixels(mContext);
                int screenHeight = DisplayUtils.getScreenHeightPixels(mContext);

                /**将显示高度设置为与原图相同宽高比例
                 * 此时宽度为屏幕宽度*/
                int height = (int) (screenWidth * scale);

                if (height < screenHeight){
                    height = screenHeight;
                }

                ViewGroup.LayoutParams params = iv_image_browser.getLayoutParams();
                params.height = height;
                params.width = screenWidth;
                iv_image_browser.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    public PicUrls getPic(int position){
        return mPics.get(position % mPics.size());
    }

    public Bitmap getBitmap(int position){
        Bitmap bitmap = null;
        View view = picViews.get(position % picViews.size());

        ImageView iv_image_browser = (ImageView)view.findViewById(R.id.iv_image_browser);
        Drawable drawable = iv_image_browser.getDrawable();

        if (drawable != null && drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
            bitmap = bitmapDrawable.getBitmap();
        }

        return bitmap;
    }
}
