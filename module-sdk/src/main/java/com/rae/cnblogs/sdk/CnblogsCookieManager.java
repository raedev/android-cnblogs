package com.rae.cnblogs.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

/**
 * 博客园COOKIE管理器
 * 用于同步WebCookie和JavaCookie，保持接口的登录状态
 */
public final class CnblogsCookieManager {


    private final Context mContext;
    private final JavaNetCookieJar mCookieJar;

    public CnblogsCookieManager(Context context, JavaNetCookieJar cookieJar) {
        mCookieJar = cookieJar;
        mContext = context;
    }

    public CnblogsCookieManager(Context context, OkHttpClient client) {
        mContext = context;
        mCookieJar = (JavaNetCookieJar) client.cookieJar();
    }


    /**
     * 同步网页的cookie到HTTP请求cookie中去
     */
    public void webCookie2JavaCookie() {
        // 同步接口的cookie达到同步web登陆
        CookieManager cookieManager = CookieManager.getInstance();
        String webCookies = cookieManager.getCookie("http://cnblogs.com");
        String sslCookies = cookieManager.getCookie("https://cnblogs.com");
        if (TextUtils.isEmpty(webCookies)) webCookies = sslCookies;
        saveCookie2JavaCookie(webCookies);
    }

    /**
     * 同步JAVA的cookie到网页的Cookie
     */
    public void javaCookie2WebCookie() {
        String url = "http://cnblogs.com";
        HttpUrl uri = HttpUrl.parse(url);
        if (uri == null) return;
        List<Cookie> cookies = mCookieJar.loadForRequest(uri);
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
     * 保存cookie到HTTP请求cookie中去
     *
     * @param webCookies 需要保存的COOKIE
     */
    public void saveCookie2JavaCookie(String webCookies) {
        if (TextUtils.isEmpty(webCookies)) return;
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
        mCookieJar.saveFromResponse(httpUrl, cookies);
    }
}
