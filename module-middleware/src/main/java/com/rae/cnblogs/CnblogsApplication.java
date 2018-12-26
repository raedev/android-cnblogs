package com.rae.cnblogs;

import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.rae.cnblogs.basic.AppDataManager;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.ApplicationCompat;
import com.rae.cnblogs.basic.BasicApplication;
import com.rae.cnblogs.resource.BuildConfig;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.theme.AppThemeManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

public class CnblogsApplication extends BasicApplication {
    @Override
    protected void onFirstCreate() {
        super.onFirstCreate();
        QMUISwipeBackActivityManager.init(this);
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
        initUMConfig();
    }

    public void clearCache() {
        // 清除图片缓存
        AppImageLoader.clearCache(getApplicationContext());
        // 清除数据库
        DbFactory.getInstance().clearCache();
        new AppDataManager(this).clearCache();
    }


    /**
     * 初始化友盟配置
     */
    private void initUMConfig() {
        // 初始化友盟
        CnblogAppConfig.APP_CHANNEL = ApplicationCompat.getChannel(this); // 渠道
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.init(this, BuildConfig.UMENG_APPKEY, CnblogAppConfig.APP_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMShareAPI.get(this);
        PlatformConfig.setWeixin(BuildConfig.WECHAT_APP_ID, BuildConfig.WECHAT_APP_SECRET);
        PlatformConfig.setSinaWeibo(BuildConfig.WEIBO_APP_ID, BuildConfig.WEIBO_APP_SECRET, "http://www.raeblog.com/cnblogs/index.php/share/weibo/redirect");
        PlatformConfig.setQQZone(BuildConfig.QQ_APP_ID, BuildConfig.QQ_APP_SECRET);
        Log.i("rae", "--- 初始化配置信息 ---");
        Log.i("rae", String.format("--- 包名：%s ---", getPackageName()));
        Log.i("rae", String.format("--- 版本号：%s ---", ApplicationCompat.getVersionCode(this)));
        Log.i("rae", String.format("--- 版本名：%s ---", ApplicationCompat.getVersionName(this)));
        Log.i("rae", String.format("--- 渠道名：%s ---", CnblogAppConfig.APP_CHANNEL));

    }
}
