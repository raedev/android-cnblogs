package com.rae.cnblogs.sdk;

import android.app.Application;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.session.SessionManager;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;

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
//    private UserInfoBean mUserInfo;
//    private CnblogAppConfig mConfig;

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
        if (cookieHandler != null && cookieHandler instanceof java.net.CookieManager) {
            ((java.net.CookieManager) cookieHandler).getCookieStore().removeAll();
        }
        mSessionManager.clear();
    }


    /**
     * 同步Cookie：JavaNetCookieJar covert to WebKit CookieManger
     */
    public void cookieJar2CookieManager() {

        if (java.net.CookieManager.getDefault() == null) {
            return;
        }

        String url = "http://cnblogs.com";
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(java.net.CookieManager.getDefault());
        List<Cookie> cookies = cookieJar.loadForRequest(HttpUrl.parse(url));
        if (cookies != null) {
            // 同步接口的cookie达到同步web登陆
            CookieSyncManager.createInstance(mContext);
            CookieManager cookieManager = CookieManager.getInstance();
            for (Cookie cookie : cookies) {
                cookieManager.setCookie(url, cookie.toString());
            }
            CookieSyncManager.getInstance().sync();
        }

    }


    /**
     * 同步Cookie：WebKit CookieManger covert to JavaNetCookieJar
     */
    public void cookieManager2CookieJar() {
        // 同步接口的cookie达到同步web登陆
        CookieManager cookieManager = CookieManager.getInstance();
        String webCookies = cookieManager.getCookie("http://cnblogs.com");
        cookieManager2CookieJar(webCookies);
    }

    public void cookieManager2CookieJar(String webCookies) {
        if (java.net.CookieManager.getDefault() == null || TextUtils.isEmpty(webCookies)) {
            return;
        }
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(java.net.CookieManager.getDefault());
        String url = "http://cnblogs.com";
        List<Cookie> cookies = new ArrayList<>();
        String[] texts = webCookies.split(";");
        HttpUrl httpUrl = HttpUrl.parse(url);
        // 解析字符串
        for (String text : texts) {
            if (TextUtils.isEmpty(text)) continue;
            text = text.trim(); // 去掉多余的空格
            if (!text.endsWith(";")) {
                text += ";";
            }
            text += " domain=.cnblogs.com; path=/; HttpOnly";
            Cookie cookie = Cookie.parse(httpUrl, text);
            cookies.add(cookie);
        }

        // 保存cookie
        cookieJar.saveFromResponse(httpUrl, cookies);
    }

    /**
     * 校验是否为当前登录用户
     */
    public boolean checkIsMe(String blogApp) {
        return isLogin() && TextUtils.equals(blogApp, getLoginUserInfo().getBlogApp());
    }
}
