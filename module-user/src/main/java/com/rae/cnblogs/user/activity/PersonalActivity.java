package com.rae.cnblogs.user.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.antcode.sdk.AntSessionManager;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.event.LoginInfoEvent;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;
import com.rae.cnblogs.user.personal.PersonalContract;
import com.rae.cnblogs.user.personal.PersonalPresenterImpl;

import org.greenrobot.eventbus.EventBus;

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
        mAccountView.setText(user.getAccount());
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

    @OnClick(R2.id.btn_logout)
    public void onLogoutClick() {
        AppMobclickAgent.onClickEvent(getContext(), "Logout");
        new DefaultDialogFragment
                .Builder()
                .cancelable(true)
                .message(getString(R.string.tips_logout))
                .confirmText("立即退出")
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performLogout();
                    }
                })
                .show(getSupportFragmentManager(), "Logout");
    }

    private void performLogout() {
        AppMobclickAgent.onProfileSignOff();
        UserProvider.getInstance().logout();
        AntSessionManager.getDefault().clear(); // 退出专栏
        EventBus.getDefault().post(new LoginInfoEvent());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
