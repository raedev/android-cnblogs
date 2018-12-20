package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;

/**
 * 粉丝以及关注个数
 * Created by ChenRui on 2017/2/7 0007 15:25.
 */
public class FriendsInfoBean extends UserInfoBean {

    /**
     * 粉丝数
     */
    private String fans;
    /**
     * 关注数
     */
    private String follows;

    /**
     * 是否已经关注
     */
    private boolean isFollowed;


    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }


    public FriendsInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.fans);
        dest.writeString(this.follows);
        dest.writeByte(this.isFollowed ? (byte) 1 : (byte) 0);
    }

    protected FriendsInfoBean(Parcel in) {
        super(in);
        this.fans = in.readString();
        this.follows = in.readString();
        this.isFollowed = in.readByte() != 0;
    }

    public static final Creator<FriendsInfoBean> CREATOR = new Creator<FriendsInfoBean>() {
        @Override
        public FriendsInfoBean createFromParcel(Parcel source) {
            return new FriendsInfoBean(source);
        }

        @Override
        public FriendsInfoBean[] newArray(int size) {
            return new FriendsInfoBean[size];
        }
    };
}
