package com.rae.cnblogs.sdk.bean;

import com.google.gson.annotations.SerializedName;

public class VersionInfo {
    @SerializedName("appdesc")
    private String appDesc;
    @SerializedName("download_url")
    private String downloadUrl;
    @SerializedName("version_name")
    private String versionName;
    @SerializedName("version_code")
    private String versionCode;

    public String getAppDesc() {
        return this.appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
