package com.rae.cnblogs.sdk;

import android.app.Application;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.session.SessionManager;

import java.net.CookieHandler;

import io.reactivex.annotations.Nullable;

/**
 * 用户提供者，保存用户信息，对已登录的用户进行操作。
 * <pre>
 * 使用前请先调用{@link #init(Application)}}方法进行初始化
 * </pre>
 * Created by ChenRui on 2017/2/3 0003 15:58.
 */
public final class UserProvider {

    private static UserProvider sUserProvider;
    private final Application mContext;

    private SessionManager mSessionManager;

    public static UserProvider getInstance() {
        if (sUserProvider == null) {
            throw new NullPointerException("用户提供程序没有初始化！");
        }
        return sUserProvider;
    }

    public static void init(Application applicationContext) {
        if (sUserProvider == null) {

            SessionManager.init(new SessionManager
                    .Builder()
                    .withContext(applicationContext)
                    .userClass(UserInfoBean.class)
                    .build());

            sUserProvider = new UserProvider(applicationContext);
        }
    }

    private UserProvider(Application context) {
        mContext = context;
        mSessionManager = SessionManager.getDefault();
    }


    /**
     * 保存当前用户信息，一般在登录的时候调用该方法
     *
     * @param userInfo 用户信息
     */
    public void setLoginUserInfo(UserInfoBean userInfo) {
        mSessionManager.setUser(userInfo);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息视图
     */
    @Nullable
    public UserInfoBean getLoginUserInfo() {
        return mSessionManager.getUser();
    }

    /**
     * 是否登录
     */
    public boolean isLogin() {
        return mSessionManager.isLogin();
    }

    public boolean isNotLogin() {
        return !isLogin();
    }

    /**
     * 退出登录，释放资源
     */
    public void logout() {
        // 移除所有的cookie
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
        CookieHandler cookieHandler = java.net.CookieManager.getDefault();
        if (cookieHandler instanceof java.net.CookieManager) {
            ((java.net.CookieManager) cookieHandler).getCookieStore().removeAll();
        }
        mSessionManager.clear();
    }
}
