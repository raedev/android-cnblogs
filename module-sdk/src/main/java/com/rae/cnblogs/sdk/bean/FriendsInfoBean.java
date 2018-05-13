package com.rae.cnblogs.sdk.bean;

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


    /**
     * 园龄
     */
    private String snsAge;

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

    public String getSnsAge() {
        return snsAge;
    }

    public void setSnsAge(String snsAge) {
        this.snsAge = snsAge;
    }
}
