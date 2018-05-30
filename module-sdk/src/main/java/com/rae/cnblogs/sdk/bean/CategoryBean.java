package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 分类
 * Created by ChenRui on 2016/11/30 0030 17:20.
 */
@Entity(nameInDb = "categories")
public class CategoryBean implements Parcelable {

    @Id
    private String categoryId;

    private String parentId;

    private String name;

    private String type;

    private int orderNo; // 排序


    private boolean isHide; // 是否隐藏

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public CategoryBean() {
        super();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.parentId);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeInt(this.orderNo);
        dest.writeByte(this.isHide ? (byte) 1 : (byte) 0);
    }

    public boolean getIsHide() {
        return this.isHide;
    }

    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }

    protected CategoryBean(Parcel in) {
        this.categoryId = in.readString();
        this.parentId = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.orderNo = in.readInt();
        this.isHide = in.readByte() != 0;
    }

    @Generated(hash = 1895999032)
    public CategoryBean(String categoryId, String parentId, String name, String type,
                        int orderNo, boolean isHide) {
        this.categoryId = categoryId;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.orderNo = orderNo;
        this.isHide = isHide;
    }

    public static final Creator<CategoryBean> CREATOR = new Creator<CategoryBean>() {
        @Override
        public CategoryBean createFromParcel(Parcel source) {
            return new CategoryBean(source);
        }

        @Override
        public CategoryBean[] newArray(int size) {
            return new CategoryBean[size];
        }
    };
}
