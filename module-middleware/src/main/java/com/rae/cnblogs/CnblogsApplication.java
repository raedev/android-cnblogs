package com.rae.cnblogs;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.rae.cnblogs.basic.AppDataManager;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BasicApplication;
import com.rae.cnblogs.resource.BuildConfig;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.theme.AppThemeManager;
import com.tencent.bugly.crashreport.CrashReport;

public class CnblogsApplication extends BasicApplication {
    @Override
    protected void onFirstCreate() {
        super.onFirstCreate();
        // 主题初始化
        AppThemeManager.init(this);
        // 路由初始化
        AppRoute.init(this, BuildConfig.DEBUG);
        // 初始化数据库
        DbCnblogs.init(this);
        // 用户管理
        UserProvider.init(this);
        // LeanCloud用户反馈初始化，要在主线程
        AVOSCloud.initialize(this,
                BuildConfig.LEAN_CLOUD_APP_ID,
                BuildConfig.LEAN_CLOUD_APP_KEY);
        FeedbackThread.getInstance();
        // Bugly
        CrashReport.initCrashReport(this, BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG);
    }

    public void clearCache() {
        // 清除图片缓存
        AppImageLoader.clearCache(getApplicationContext());
        // 清除数据库
        DbFactory.getInstance().clearCache();
        new AppDataManager(this).clearCache();
    }
}
