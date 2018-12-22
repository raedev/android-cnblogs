package com.rae.cnblogs.basic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * APP 埋点统计
 * Created by ChenRui on 2017/2/8 0008 11:44.
 */
public final class AppMobclickAgent {

    /**
     * 统计打开时间
     */
    public static void onAppOpenEvent(Context context, @Nullable String blogApp) {
        @SuppressLint("SimpleDateFormat")
        String openDate = new SimpleDateFormat("HH").format(new Date()) + ":00";
        MobclickAgent.onEvent(context, "APP_OPEN_EVENT", openDate);

        // 统计活跃用户
        if (TextUtils.isEmpty(blogApp))
            blogApp = "Guest";

        MobclickAgent.onEvent(context, "APP_USER_OPEN_EVENT", blogApp);
    }

    /**
     * 统计栏目点击
     *
     * @param category 栏目
     */
    public static void onCategoryEvent(Context context, String category) {
        MobclickAgent.onEvent(context, "APP_CATEGORY", category);
    }

    /**
     * 搜索统计
     *
     * @param keyword 关键字
     */
    public static void onSearchEvent(Context context, String keyword) {
        MobclickAgent.onEvent(context, "APP_SEARCH", keyword);
    }

    /**
     * 广告统计
     */
    private static void onAdEvent(Context context, Map<String, String> map) {
        MobclickAgent.onEvent(context, "APP_AD_EVENT", map);
    }

    /**
     * 启动页广告曝光次数
     *
     * @param id 广告ID
     */
    public static void onLaunchAdExposureEvent(Context context, String id, String name) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "Exposure");
        map.put("ad", withString(id, "广告ID为空"));
        map.put("title", withString(name, "广告标题为空"));
        onAdEvent(context, map);
    }

    /**
     * 启动页广告点击次数
     *
     * @param id 广告ID
     */
    public static void onLaunchAdClickEvent(Context context, String id, String name) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "Click");
        map.put("id", withString(id, "广告ID为空"));
        map.put("name", withString(name, "广告标题为空"));
        onAdEvent(context, map);
    }


    /**
     * 用户登录事件，为了保护用户的隐私，这里只统计登录的blogApp，不记录帐号信息。
     * 登录失败只统计错误信息
     *
     * @param blogApp   登录成功的blogApp
     * @param isSuccess 是否成功
     * @param msg       失败的错误信息
     */
    public static void onLoginEvent(Context context, String blogApp, boolean isSuccess, String msg) {

        Map<String, String> map = new HashMap<>();
        map.put("success", String.valueOf(isSuccess));
        map.put("blogApp", blogApp);
        if (!TextUtils.isEmpty(msg)) {
            map.put("message", msg);
        }
        MobclickAgent.onEvent(context, "APP_LOGIN_EVENT", map);
    }


    /**
     * 点击事件
     *
     * @param name 事件名称
     */
    public static void onClickEvent(Context context, String name) {
        MobclickAgent.onEvent(context, "APP_CLICK_EVENT", name);
    }

    /**
     * 使用默认值的字符串
     *
     * @param text     源
     * @param defValue 默认值
     */
    private static String withString(String text, String defValue) {
        return TextUtils.isEmpty(text) ? defValue : text;
    }

    /**
     * 点击版本更新事件
     */
    public static void onUpdateEvent(Context context) {
        String versionName = "1.0.0";
        try {
            PackageInfo am = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versionName = am.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        MobclickAgent.onEvent(context, "APP_UPDATE", versionName);
    }


    /**
     * 退出登录事件
     */
    public static void onProfileSignOff() {
        MobclickAgent.onProfileSignOff();
    }

    /**
     * 登录事件
     *
     * @param blogApp 用户
     */
    public static void onProfileSignIn(String blogApp) {
        MobclickAgent.onProfileSignIn(blogApp);
    }
}
