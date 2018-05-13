package com.rae.cnblogs.sdk.config;

/**
 * 离线下载配置
 * Created by ChenRui on 2017/2/1 20:39.
 */
public class OfflineConfig {
    private int mPageSize = 1; // 离线分页多少页
    private boolean mEnableOffline = true; // 是否允许离线
    private int mStartHour = 0; // 离线开始时间 - 小时
    private int mStartMinute = 0; // 离线开始时间 - 分钟

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    public boolean isEnableOffline() {
        return mEnableOffline;
    }

    public void setEnableOffline(boolean enableOffline) {
        mEnableOffline = enableOffline;
    }

    public int getStartHour() {
        return mStartHour;
    }

    public void setStartHour(int startHour) {
        mStartHour = startHour;
    }

    public int getStartMinute() {
        return mStartMinute;
    }

    public void setStartMinute(int startMinute) {
        mStartMinute = startMinute;
    }
}
