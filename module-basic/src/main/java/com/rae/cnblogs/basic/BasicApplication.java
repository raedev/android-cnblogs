package com.rae.cnblogs.basic;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.util.List;

/**
 * 应用程序
 */
public class BasicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || getPackageName().equalsIgnoreCase(processAppName)) {
            onFirstCreate();
        }
    }

    /**
     * 进初始化一次
     */
    protected void onFirstCreate() {

    }

    @Override
    protected void attachBaseContext(Context base) {
        // 解决Tinker存在的BUG，一定要在这之前初始化
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    private String getAppName(int pID) {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        if (am == null) return null;
        List l = am.getRunningAppProcesses();
        for (Object aL : l) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (aL);
            try {
                if (info.pid == pID) {
                    return info.processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
