package com.rae.cnblogs.sdk.event;

import com.rae.cnblogs.sdk.bean.UserInfoBean;

import io.reactivex.annotations.Nullable;

/**
 * 用户信息发生改变事件
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class UserInfoChangedEvent {

    // 更新后的用户信息
    @Nullable
    private UserInfoBean mUserInfo;

    public UserInfoChangedEvent() {

    }

    public UserInfoChangedEvent(UserInfoBean userInfoBean) {
        this.mUserInfo = userInfoBean;
    }


    public UserInfoBean getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        mUserInfo = userInfo;
    }

    /**
     * 是否刷新接口信息
     *
     * @return
     */
    public boolean isRefresh() {
        return mUserInfo == null;
    }
}
