package com.rae.cnblogs.sdk;

import android.text.TextUtils;

import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * 接口回调
 * Created by ChenRui on 2017/5/5 0005 16:48.
 */
public abstract class ApiDefaultObserver<T> extends DisposableObserver<T> {

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        if (t == null) {
            onEmpty(t);
            return;
        }
        accept(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

        if (e instanceof CnblogsApiException) {
            CnblogsApiException ex = (CnblogsApiException) e;
            if (ex.getCode() == ApiErrorCode.LOGIN_EXPIRED) {
                clearLoginToken();
                onLoginExpired();
                return;
            }
            onError(ex);
            return;
        }
        if (e instanceof HttpException) {
            HttpException ex = (HttpException) e;
            if (ex.code() == 401) {
                // 登录失效
                clearLoginToken();
                onLoginExpired();
                return;
            } else if (ex.code() == 404) {
                onError("没找到页面0x404");
                return;
            } else if (ex.code() == 503) {
                onError("服务器拒绝连接0x503");
                return;
            } else if (ex.code() >= 500 && ex.code() < 600) {
                onError("服务器发生错误0x" + ex.code());
                return;
            } else {
                onError("请求错误，状态码0x" + ex.code());
                return;
            }
        }
        if (e instanceof UnknownHostException) {
            onError("网络连接错误，请检查网络连接");
            return;
        }


        String message = BuildConfig.DEBUG ? e.getMessage() : "数据加载失败，请重试";


        if (message != null && (message.contains("登录过期") || message.contains("Authorization"))) {
            clearLoginToken();
            onLoginExpired();
            return;
        }

        if (TextUtils.isEmpty(message)) {
            message = "接口信息异常";
        }
        onError(message);
    }

    protected void clearLoginToken() {
        UserProvider.getInstance().logout(); // 退出登录
    }

    public void onError(CnblogsApiException e) {
        onError(e.getMessage());
    }

    /**
     * 登录过期
     */
    protected void onLoginExpired() {
        onError("登录失效，请重新登录");
    }

    protected abstract void onError(String message);

    protected abstract void accept(T t);

    protected void onEmpty(T t) {
        onError("没有数据");
    }
}
