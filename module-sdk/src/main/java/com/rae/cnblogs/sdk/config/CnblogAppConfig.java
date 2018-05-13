package com.rae.cnblogs.sdk.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.rae.cnblogs.sdk.AppGson;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

/**
 * 接口配置项
 * Created by ChenRui on 2017/1/14 01:53.
 */
public class CnblogAppConfig {
    /**
     * 渠道
     */
    public static String APP_CHANNEL = "official";
    private static CnblogAppConfig sInstance;

    public static CnblogAppConfig getsInstance(Context applicationContext) {
        if (sInstance == null) {
            sInstance = new CnblogAppConfig(applicationContext.getApplicationContext());
        }
        return sInstance;
    }

    private SharedPreferences mConfig;

    private CnblogAppConfig(Context context) {
        mConfig = context.getApplicationContext().getSharedPreferences("CNBLOG_SDK_CONFIG", Context.MODE_PRIVATE);
    }

    /**
     * 清除所有的配置项目
     */
    public void clear() {
        mConfig.edit().clear().apply();
    }

    /**
     * 清除所有的配置项目
     */
    public void clearUserInfo() {
        mConfig.edit()
                .remove("UserInfo")
                .apply();
    }

    /**
     * 保存用户信息
     *
     * @param userInfo 用户信息
     */
    public void saveUserInfo(UserInfoBean userInfo) {
        if (userInfo != null)
            mConfig.edit().putString("UserInfo", AppGson.get().toJson(userInfo)).apply();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public UserInfoBean getUserInfo() {
        String json = mConfig.getString("UserInfo", null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return AppGson.get().fromJson(json, UserInfoBean.class);
    }


    /**
     * 指引-评论缓存提示
     */
    public boolean hasCommentGuide() {
        return mConfig.getBoolean("hasCommentGuide", false);
    }

    /**
     * 指引-评论缓存提示
     */
    public void commentGuide() {
        mConfig.edit().putBoolean("hasCommentGuide", true).apply();
    }

    /**
     * 指引-点赞缓存提示
     */
    public boolean hasLikeGuide() {
        return mConfig.getBoolean("hasLikeGuide", false);
    }

    /**
     * 指引-点赞缓存提示
     */
    public void likeGuide() {
        mConfig.edit().putBoolean("hasLikeGuide", true).apply();
    }

    /**
     * 指引-登录提示
     */
    public boolean hasLoginGuide() {
        return mConfig.getBoolean("hasLoginGuide", false);
    }

    /**
     * 指引-登录提示
     */
    public void loginGuide() {
        mConfig.edit().putBoolean("hasLoginGuide", true).apply();
    }


    /**
     * 记录首页退出时间
     */
    public long getMainExitTimeMillis() {
        return mConfig.getLong("MainExitTimeMillis", 0);
    }

    public void setMainExitTimeMillis(long value) {
        mConfig.edit().putLong("MainExitTimeMillis", value).apply();
    }

    /**
     * 设置消息数量
     */
    public void setMessageCount(int count) {
        mConfig.edit().putInt("messageCount", count).apply();
    }

    /**
     * 获取消息数量
     */
    public int getMessageCount() {
        return mConfig.getInt("messageCount", 0);
    }

    /**
     * 获取字体大小，注意判断是否为0，单位：PX
     */
    public int getPageTextSize() {
        return mConfig.getInt("PageTextSize", 0);
    }

    /**
     * 设置字体大小，单位：PX
     */
    public void setPageTextSize(int size) {
        mConfig.edit().putInt("PageTextSize", size).apply();
    }

    /**
     * 发布动态提示信息
     */
    public boolean getPostMomentInProgressTips() {
        return mConfig.getBoolean("PostMomentInProgressTips", false);
    }

    /**
     * 发布动态提示信息
     */
    public void setPostMomentInProgressTips(boolean value) {
        mConfig.edit().putBoolean("PostMomentInProgressTips", value).apply();
    }
}
