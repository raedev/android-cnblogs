package com.rae.cnblogs.discover;

import com.antcode.sdk.AntSessionManager;
import com.rae.cnblogs.sdk.ApiDefaultObserver;

/**
 * ant sdk default observer
 *
 * @param <T>
 */
public abstract class AntSdkDefaultObserver<T> extends ApiDefaultObserver<T> {

    @Override
    public void onError(Throwable e) {
        if (e.getMessage().contains("登录失效")) {
            clearLoginToken();
            onLoginExpired();
            return;
        }
        super.onError(e);
    }

    @Override
    protected void clearLoginToken() {
        AntSessionManager.getDefault().clear();
    }
}
