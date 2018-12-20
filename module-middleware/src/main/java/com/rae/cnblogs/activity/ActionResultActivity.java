package com.rae.cnblogs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.middleware.R;
import com.rae.cnblogs.middleware.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 操作结果提示
 * Created by rae on 2018/12/20.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_ACTION_RESULT)
public class ActionResultActivity extends SwipeBackBasicActivity {

    @BindView(R2.id.tv_message)
    TextView mMessageView;

    @BindView(R2.id.img_avatar)
    ImageView mAvatarView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_result);
        setTitle(" ");
        mMessageView.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onHomeClick();
    }

    @OnClick(R2.id.btn_ensure)
    public void onHomeClick() {
        AppRoute.routeToMain(this);
        finish();
    }
}
