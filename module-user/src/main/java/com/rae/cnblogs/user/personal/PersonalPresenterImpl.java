package com.rae.cnblogs.user.personal;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PersonalPresenterImpl extends BasicPresenter<PersonalContract.View> implements PersonalContract.Presenter {

    private IUserApi mUserApi;

    public PersonalPresenterImpl(PersonalContract.View view) {
        super(view);
        mUserApi = CnblogsApiFactory.getInstance(getContext()).getUserApi();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        final UserInfoBean user = UserProvider.getInstance().getLoginUserInfo();
        if (user == null) {
            getView().onLoginExpired();
            return;
        }

        getView().onLoadUserInfo(user);

        // 由于账号是跟blogApp不一样的，这里再去获取用户的账号
        AndroidObservable.create(mUserApi.getUserAccount())
                .with(this)
                .subscribe(new ApiDefaultObserver<String>() {
                    @Override
                    protected void onError(String message) {

                    }

                    @Override
                    protected void accept(String account) {
                        user.setAccount(account);
                        // 保存用户信息
                        UserProvider.getInstance().setLoginUserInfo(user);
                        getView().onLoadUserInfo(user);
                    }
                });
    }

    @Subscribe
    public void onEvent(UserInfoChangedEvent event) {
        if (event.getUserInfo() != null) {
            getView().onLoadUserInfo(event.getUserInfo());
        }
    }
}
