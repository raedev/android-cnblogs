package com.rae.cnblogs.home.setting;

import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.ApplicationCompat;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.BuildConfig;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IRaeServerApi;
import com.rae.cnblogs.sdk.bean.VersionInfo;
import com.rae.cnblogs.sdk.event.LoginInfoEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 设置
 * Created by rae on 2018/5/14.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SettingPresenterImpl extends BasicPresenter<SettingContract.View> implements SettingContract.Presenter {

    private final IRaeServerApi mRaeServerApi;

    public SettingPresenterImpl(SettingContract.View view) {
        super(view);
        mRaeServerApi = CnblogsApiFactory.getInstance(getContext()).getRaeServerApi();
    }

    @Override
    protected void onStart() {
        if (!UserProvider.getInstance().isLogin()) {
            getView().onNotLogin();
        }
        getView().onLoadVersionInfo("v" + ApplicationCompat.getVersionName(getContext()));
    }

    @Override
    public void logout() {
        getView().onNotLogin();
        AppMobclickAgent.onProfileSignOff();
        UserProvider.getInstance().logout();
        EventBus.getDefault().post(new LoginInfoEvent());
        getView().onLogout();
    }

    @Override
    public void checkUpdate() {
        final int versionCode = ApplicationCompat.getVersionCode(getContext());
        String versionName = ApplicationCompat.getVersionName(getContext());
        String channel = ApplicationCompat.getChannel(getContext());
        AndroidObservable
                .create(mRaeServerApi.versionInfo(versionCode, versionName, channel, BuildConfig.BUILD_TYPE))
                .with(this)
                .subscribe(new ApiDefaultObserver<VersionInfo>() {
                    @Override
                    protected void onError(String message) {
                        // 更新出错不提示
                        getView().onNoVersion();
                    }

                    @Override
                    protected void accept(VersionInfo versionInfo) {
                        getView().onNewVersion(versionInfo);
                    }
                });
    }
}
