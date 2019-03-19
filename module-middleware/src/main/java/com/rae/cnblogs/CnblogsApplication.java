package com.rae.cnblogs;

import android.content.Intent;
import android.util.Log;

import com.antcode.sdk.AntCodeSDK;
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
import com.rae.cnblogs.theme.ThemeCompat;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import skin.support.SkinCompatManager;
import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

public class CnblogsApplication extends BasicApplication implements SkinObserver {
    @Override
    protected void onFirstCreate() {
        super.onFirstCreate();

        startCnblogServices();

        QMUISwipeBackActivityManager.init(this);

        // 主题初始化
        AppThemeManager.init(this);
        SkinCompatManager.getInstance().addObserver(this);

        // 路由初始化
        AppRoute.init(this, BuildConfig.DEBUG);
        // 初始化数据库
        DbCnblogs.init(this);
        // 用户管理
        UserProvider.init(this);
        // 码蚁专栏SDK
        AntCodeSDK.init(this, "91f8cc0325f8d228d37bd1c9c4ef7e84");
//        AntCodeSDK.init(new AntCodeSDK.Builder(this).clientId("91f8cc0325f8d228d37bd1c9c4ef7e84").url("http://192.168.1.7:8081/antcode/index.php/"));
        // LeanCloud用户反馈初始化，要在主线程
        AVOSCloud.initialize(this,
                BuildConfig.LEAN_CLOUD_APP_ID,
                BuildConfig.LEAN_CLOUD_APP_KEY);
        FeedbackThread.getInstance();

        // Bugly
        CrashReport.initCrashReport(this, BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG);
        initUMConfig();

    }

    // 启动博客园服务
    private void startCnblogServices() {
        try {
            Intent serviceIntent = new Intent();
            serviceIntent.setClassName(getPackageName(), "com.rae.cnblogs.blog.CnblogsService");
            startService(serviceIntent);
        } catch (Exception ex) {
            Log.e("Rae", "启动博客园服务失败", ex);
        }
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
        Log.d("rae", "--- 初始化配置信息 ---");
        Log.d("rae", String.format("--- 包名：%s ---", getPackageName()));
        Log.d("rae", String.format("--- 版本号：%s ---", ApplicationCompat.getVersionCode(this)));
        Log.d("rae", String.format("--- 版本名：%s ---", ApplicationCompat.getVersionName(this)));
        Log.d("rae", String.format("--- 渠道名：%s ---", CnblogAppConfig.APP_CHANNEL));
    }

    @Override
    public void updateSkin(SkinObservable observable, Object o) {
        EventBus.getDefault().post(new AppThemeManager.ThemeEvent(ThemeCompat.isNight()));
    }
}
