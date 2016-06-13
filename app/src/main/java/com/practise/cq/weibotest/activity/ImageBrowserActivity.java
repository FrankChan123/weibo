package com.practise.cq.weibotest.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.adapter.ImageBrowserAdapter;
import com.practise.cq.weibotest.base.BaseActivity;
import com.practise.cq.weibotest.entity.PicUrls;
import com.practise.cq.weibotest.entity.Status;
import com.practise.cq.weibotest.util.ImgUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CQ on 2016/6/6 0006.
 */
public class ImageBrowserActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager vp_image_browser;
    private TextView tv_image_index;
    private Button btn_save;
    private Button btn_original_image;

    private Status status;
    private int position;
    private ImageBrowserAdapter adapter;
    private ArrayList<PicUrls> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_browser_activity);

        initData();
        initView();
        setData();
    }

    private void initData(){
        status = (Status) getIntent().getSerializableExtra("status");
        position = getIntent().getIntExtra("position", 0);
        imageUrls = status.getPicUrls();
    }

    private void initView(){
        vp_image_browser = (ViewPager)findViewById(R.id.vp_image_brower);
        tv_image_index = (TextView)findViewById(R.id.tv_image_index);
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_original_image = (Button)findViewById(R.id.btn_original_image);

        btn_save.setOnClickListener(this);
        btn_original_image.setOnClickListener(this);
    }

    private void setData() {
        adapter = new ImageBrowserAdapter(this, imageUrls);
        vp_image_browser.setAdapter(adapter);

        final int size = imageUrls.size();
        int initPosition = Integer.MAX_VALUE / 2 / size * size + position;

        if (size > 1){
            tv_image_index.setVisibility(View.VISIBLE);
            tv_image_index.setText((position+1) + "/" + size);
        }else {
            tv_image_index.setVisibility(View.GONE);
        }

        vp_image_browser.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**切换图片时更新索引
             * 同时判断当前显示是否为原图，若为原图则隐藏原图Button*/
            @Override
            public void onPageSelected(int position) {
                int index = position % size;
                tv_image_index.setText((index + 1) + "/" + size);

                PicUrls pic = adapter.getPic(position);
                btn_original_image.setVisibility(pic.isShowOriImg() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp_image_browser.setCurrentItem(initPosition);
    }

    @Override
    public void onClick(View view) {
        PicUrls pic = adapter.getPic(vp_image_browser.getCurrentItem());

        switch (view.getId()){
            case R.id.btn_save:
                Bitmap bitmap = adapter.getBitmap(vp_image_browser.getCurrentItem());
                boolean isShowOriImg = pic.isShowOriImg();
                String imgName = isShowOriImg ? "ori-" : "mid-" + pic.getImageId();

                try {
                    String insertName = ImgUtils.saveFile(this, bitmap, imgName);
                    if (insertName != null){
                        Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
//                        ToastUtil.show(this, "保存成功！", Toast.LENGTH_SHORT);
                    }else {
                        Toast.makeText(this, "保存失败！", Toast.LENGTH_SHORT).show();
//                        ToastUtil.show(this, "保存失败！", Toast.LENGTH_SHORT);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_original_image:
                pic.setShowOriImg(true);
                adapter.notifyDataSetChanged();

                btn_original_image.setVisibility(View.GONE);
                break;
        }
    }
}
