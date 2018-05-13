package com.rae.cnblogs;

import com.rae.cnblogs.basic.BasicApplication;
import com.rae.cnblogs.basic.BuildConfig;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.rae.cnblogs.theme.AppThemeManager;

public class CnblogsApplication extends BasicApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // 主题初始化
        AppThemeManager.init(this);
        // 路由初始化
        AppRoute.init(this, BuildConfig.DEBUG);
        // 初始化数据库
        DbCnblogs.init(this);
    }
}
