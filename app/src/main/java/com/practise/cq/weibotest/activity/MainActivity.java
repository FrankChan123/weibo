package com.practise.cq.weibotest.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.practise.cq.weibotest.R;
import com.practise.cq.weibotest.base.BaseActivity;
import com.practise.cq.weibotest.fragment.FragmentController;
import com.practise.cq.weibotest.util.TitleBarBuilder;
import com.practise.cq.weibotest.util.ToastUtil;

/**
 * Created by CQ on 2016/5/15 0015.
 */
public class MainActivity extends BaseActivity implements
        View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    private FragmentController controller;
    private RadioGroup rg;
    private ImageView ivAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        new TitleBarBuilder(this).setTitleBarEnable(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        controller = FragmentController.getInstance(this, R.id.fl_content);
        controller.showFragment(0);

        initView();

    }

    private void initView() {
        rg = (RadioGroup)findViewById(R.id.rg_tab);
        ivAdd = (ImageView)findViewById(R.id.iv_add);

        rg.setOnCheckedChangeListener(this);
        ivAdd.setOnClickListener(this);
    }

    /**点击radioButton实现页面切换*/
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId){
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_message:
                controller.showFragment(1);
                break;
            case R.id.rb_square:
                controller.showFragment(2);
                break;
            case R.id.rb_myInfo:
                controller.showFragment(3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_add:
                ToastUtil.show(this, "发送一条微博！", Toast.LENGTH_LONG);
                break;
            default:
                break;
        }
    }
}
