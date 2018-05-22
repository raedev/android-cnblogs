package com.rae.cnblogs.sdk.event;

import com.rae.cnblogs.sdk.bean.UserInfoBean;

/**
 * 用户信息事件
 * Created by ChenRui on 2017/10/10 0010 23:29.
 */
public class UserInfoEvent {
    private boolean isLogin;
    private UserInfoBean mUserInfoBean;

    public UserInfoEvent() {
    }

    public UserInfoEvent(boolean isLogin, UserInfoBean userInfoBean) {
        this.isLogin = isLogin;
        mUserInfoBean = userInfoBean;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }
}
