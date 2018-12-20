package com.rae.cnblogs.user.personal;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PersonalPresenterImpl extends BasicPresenter<PersonalContract.View> implements PersonalContract.Presenter {

    public PersonalPresenterImpl(PersonalContract.View view) {
        super(view);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        UserInfoBean user = UserProvider.getInstance().getLoginUserInfo();
        if (user == null) {
            getView().onLoginExpired();
            return;
        }

        getView().onLoadUserInfo(user);

    }

    @Subscribe
    public void onEvent(UserInfoChangedEvent event) {
        if (event.getUserInfo() != null) {
            getView().onLoadUserInfo(event.getUserInfo());
        }
    }
}
