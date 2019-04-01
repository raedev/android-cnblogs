package com.rae.cnblogs.user.login;

import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.rae.cnblogs.sdk.event.LoginInfoEvent;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 登录协议
 * Created by rae on 2018/5/16.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class LoginPresenterImpl extends BasicPresenter<LoginContract.View> implements LoginContract.Presenter {

    private final IUserApi mUserApi;
    private CnblogAppConfig mConfig;
    private String mBlogApp;

    public LoginPresenterImpl(LoginContract.View view) {
        super(view);
        mConfig = CnblogAppConfig.getInstance(getContext());
        mUserApi = CnblogsApiFactory.getInstance(getContext()).getUserApi();
    }

    @Override
    protected void onStart() {
        // 清除COOKIE
        CookieSyncManager.createInstance(getContext());
        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void login() {
        if (mConfig.hasLoginGuide()) {
            getView().onUserLicense();
            return;
        }
        getView().onRouteToWebLogin();
    }

    @Override
    public void license() {
        mConfig.applyLoginGuide();
        getView().onRouteToWebLogin();
    }

    @Override
    public void loadUserInfo() {
        AndroidObservable
                // 2、获取blogApp
                .create(mUserApi.getUserBlogAppInfo())
                .with(this)
                .flatMap(new Function<UserInfoBean, ObservableSource<UserInfoBean>>() {
                    @Override
                    public ObservableSource<UserInfoBean> apply(UserInfoBean user) {
                        // 3、根据blogApp获取用户信息
                        mBlogApp = user.getBlogApp();
                        return AndroidObservable
                                .create(mUserApi.getUserInfo(user.getBlogApp()))
                                .with(LoginPresenterImpl.this);
                    }
                })
                .subscribe(new ApiDefaultObserver<UserInfoBean>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        // 报告错误信息
                        CrashReport.postCatchedException(new CnblogsApiException("登录失败", e));
                    }

                    @Override
                    protected void onError(String message) {
                        // 统计错误信息
                        AppMobclickAgent.onLoginEvent(getContext(), "WEB-ERROR", false, message);
                        getView().onLoginFailed(message);
                    }

                    @Override
                    protected void accept(final UserInfoBean data) {
                        if (TextUtils.isEmpty(data.getUserId())) {
                            onError("获取用户信息失败");
                            return;
                        }

                        // 如果blogApp为空则重新设置，这里的blogApp一定不为空了
                        if (TextUtils.isEmpty(data.getBlogApp())) {
                            data.setBlogApp(mBlogApp);
                        }

                        // 保存登录信息
                        UserProvider.getInstance().setLoginUserInfo(data);
                        // 统计登录事件
                        AppMobclickAgent.onLoginEvent(getContext(), data.getBlogApp(), true, "登录成功");
                        // 友盟统计用户
                        AppMobclickAgent.onProfileSignIn(data.getBlogApp());
                        // [重要] 同步Cookie登录信息
                        CnblogsApiFactory.getInstance(getContext()).javaCookie2WebCookie();
                        // 发送用户登录事件
                        EventBus.getDefault().post(new LoginInfoEvent(true, data));
                        EventBus.getDefault().post(new UserInfoChangedEvent(data));
                        getView().onLoginSuccess(data);
                    }
                });
    }
}
