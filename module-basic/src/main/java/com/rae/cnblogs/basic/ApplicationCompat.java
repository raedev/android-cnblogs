package com.rae.cnblogs.basic;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.meituan.android.walle.WalleChannelReader;

/**
 * 应用程序扩展类
 */
public final class ApplicationCompat {

    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取版本名称
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    /**
     * 获取渠道包，如果没有定义渠道包默认返回official
     */
    public static String getChannel(Context context) {
        String channel = WalleChannelReader.getChannel(context);
        return TextUtils.isEmpty(channel) ? "official" : channel;
    }


    /**
     * 检查网络是否可用
     */
    public static boolean isNetworkError(Context context) {
        if (context == null) return true;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable();
    }


    public static boolean checkFragmentIsActive(Fragment fragment) {
        return fragment.isVisible() && !fragment.isDetached() && fragment.isAdded();
    }
}
