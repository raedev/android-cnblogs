package com.rae.cnblogs.sdk.bean;


import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

//@Table(name = "ads")
@Entity(nameInDb = "ads")
public class AdvertBean {

    @Id
    @SerializedName("ad_id")
    private String mAdId;

    @SerializedName("ad_end_date")
    private String mAdEndDate;

    @SerializedName("create_time")
    private String mCreateTime;

    @SerializedName("ad_url")
    private String mAdUrl;

    @SerializedName("image_url")
    private String mImageUrl;

    @SerializedName("ad_name")
    private String mAdName;

    @SerializedName("ad_type")
    private String mAdType;

    @SerializedName("jump_type")
    private String mJumpType;


    @Generated(hash = 1203995417)
    public AdvertBean(String mAdId, String mAdEndDate, String mCreateTime,
                      String mAdUrl, String mImageUrl, String mAdName, String mAdType,
                      String mJumpType) {
        this.mAdId = mAdId;
        this.mAdEndDate = mAdEndDate;
        this.mCreateTime = mCreateTime;
        this.mAdUrl = mAdUrl;
        this.mImageUrl = mImageUrl;
        this.mAdName = mAdName;
        this.mAdType = mAdType;
        this.mJumpType = mJumpType;
    }

    @Generated(hash = 108258830)
    public AdvertBean() {
    }


    public String getAdEndDate() {
        return this.mAdEndDate;
    }

    public void setAdEndDate(String adEndDate) {
        this.mAdEndDate = adEndDate;
    }

    public String getAdId() {
        return this.mAdId;
    }

    public void setAdId(String adId) {
        this.mAdId = adId;
    }

    public String getCreateTime() {
        return this.mCreateTime;
    }

    public void setCreateTime(String createTime) {
        this.mCreateTime = createTime;
    }

    public String getAdUrl() {
        return this.mAdUrl;
    }

    public void setAdUrl(String adUrl) {
        this.mAdUrl = adUrl;
    }


    public String getAdName() {
        return this.mAdName;
    }

    public void setAdName(String adName) {
        this.mAdName = adName;
    }

    public String getJumpType() {
        return this.mJumpType;
    }

    public void setJumpType(String jumpType) {
        this.mJumpType = jumpType;
    }

    public String getAdType() {
        return mAdType;
    }

    public void setAdType(String adType) {
        this.mAdType = adType;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getMAdId() {
        return this.mAdId;
    }

    public void setMAdId(String mAdId) {
        this.mAdId = mAdId;
    }

    public String getMAdEndDate() {
        return this.mAdEndDate;
    }

    public void setMAdEndDate(String mAdEndDate) {
        this.mAdEndDate = mAdEndDate;
    }

    public String getMCreateTime() {
        return this.mCreateTime;
    }

    public void setMCreateTime(String mCreateTime) {
        this.mCreateTime = mCreateTime;
    }

    public String getMAdUrl() {
        return this.mAdUrl;
    }

    public void setMAdUrl(String mAdUrl) {
        this.mAdUrl = mAdUrl;
    }

    public String getMImageUrl() {
        return this.mImageUrl;
    }

    public void setMImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getMAdName() {
        return this.mAdName;
    }

    public void setMAdName(String mAdName) {
        this.mAdName = mAdName;
    }

    public String getMAdType() {
        return this.mAdType;
    }

    public void setMAdType(String mAdType) {
        this.mAdType = mAdType;
    }

    public String getMJumpType() {
        return this.mJumpType;
    }

    public void setMJumpType(String mJumpType) {
        this.mJumpType = mJumpType;
    }
}
