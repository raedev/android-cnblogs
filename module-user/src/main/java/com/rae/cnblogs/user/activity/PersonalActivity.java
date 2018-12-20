package com.rae.cnblogs.user.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;
import com.rae.cnblogs.user.personal.PersonalContract;
import com.rae.cnblogs.user.personal.PersonalPresenterImpl;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rae on 2018/12/18.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_USER_CENTER)
public class PersonalActivity extends SwipeBackBasicActivity implements PersonalContract.View {

    private PersonalContract.Presenter mPresenter;

    @BindView(R2.id.tv_user_account)
    TextView mAccountView;
    @BindView(R2.id.tv_nickname)
    TextView mNicknameView;
    @BindView(R2.id.tv_desc)
    TextView mDescView;
    @BindView(R2.id.img_avatar)
    ImageView mAvatarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mPresenter = new PersonalPresenterImpl(this);
        mPresenter.start();
    }

    @Override
    public void onLoadUserInfo(UserInfoBean user) {
        mAccountView.setText(user.getBlogApp());
        mNicknameView.setText(user.getDisplayName());
        mDescView.setText(user.getIntroduce());
        AppImageLoader.displayAvatar(user.getAvatar(), mAvatarView);
    }

    @Override
    public void onLoginExpired() {
        AppRoute.routeToLogin(this);
        finish();
    }


    @OnClick(R2.id.ll_avatar)
    public void onAvatarClick() {
        AppRoute.routeToAvatar(this);
    }

    @OnClick(R2.id.ll_account)
    public void onAccountClick() {
        AppRoute.routeToPersonalAccount(this);
    }

    @OnClick(R2.id.ll_nickname)
    public void onNicknameClick() {
        AppRoute.routeToPersonalNickName(this);
    }

    @OnClick(R2.id.ll_pwd)
    public void onPwdClick() {
        AppRoute.routeToResetPassword(this);
    }

    @OnClick(R2.id.ll_introduce)
    public void onIntroduceClick() {
        AppRoute.routeToPersonalIntroduce(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
