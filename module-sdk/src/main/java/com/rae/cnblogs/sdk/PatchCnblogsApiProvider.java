package com.rae.cnblogs.sdk;

import android.content.Context;

/**
 * 用于动态下发的接口工厂提供者
 * Created by ChenRui on 2018/1/26 0026 14:28.
 */
public class PatchCnblogsApiProvider extends DefaultCnblogsApiProvider {

    public PatchCnblogsApiProvider(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public int getApiVersion() {
        return 3;
    }

    @Override
    public String toString() {
        return "this is cnblogs patch api provider! version is " + getApiVersion();
    }
}
