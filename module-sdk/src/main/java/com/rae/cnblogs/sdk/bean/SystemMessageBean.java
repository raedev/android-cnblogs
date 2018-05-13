package com.rae.cnblogs.sdk.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 系统消息
 * Created by ChenRui on 2017/9/5 0005 17:04.
 */
public class SystemMessageBean {

    private String summary;
    @SerializedName("thumb_url")
    private String mThumbUrl;
    private String url;
    @SerializedName("create_time")
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getThumbUrl() {
        return mThumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.mThumbUrl = thumbUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
