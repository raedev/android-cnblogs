package com.rae.cnblogs.user.login;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

/**
 * 登录
 * Created by rae on 2018/5/16.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface LoginContract {
    interface Presenter extends IPresenter {

        /**
         * 执行登录操作
         */
        void login();

        /**
         * 用户已经同意协议
         */
        void license();

        /**
         * 加载用户信息
         */
        void loadUserInfo();
    }

    interface View extends IPresenterView {

        /**
         * 请求用户阅读协议
         */
        void onUserLicense();

        /**
         * 跳转到网页登录
         */
        void onRouteToWebLogin();

        /**
         * 登录成功
         *
         * @param data
         */
        void onLoginSuccess(UserInfoBean data);

        /**
         * 登录失败
         */
        void onLoginFailed(String message);
    }
}
