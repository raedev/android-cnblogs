package com.rae.cnblogs.basic;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * 应用程序
 */
public class BasicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        // 解决Tinker存在的BUG，一定要在这之前初始化
        MultiDex.install(base);
        super.attachBaseContext(base);
    }
}
