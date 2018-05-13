package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 闪存评论
 * Created by ChenRui on 2017/11/2 0002 15:10.
 */
public class MomentCommentBean implements Parcelable {
    /**
     * 主键，如果值为more的时候为浏览更多
     */
    private String id;
    // 闪存ID
    private String ingId;

    // 作者昵称
    private String authorName;
    private String blogApp;
    private String content;
    private String userAlias;
    // 作者头像
    private String avatar;
    // 发布时间
    private String postTime;

    // @用户昵称
    private String atAuthorName;
    // @用户userAlias
    private String atUserAlias;

    // 引用的原文
    private String referenceContent;


    public String getIngId() {
        return ingId;
    }

    public void setIngId(String ingId) {
        this.ingId = ingId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBlogApp() {
        return blogApp;
    }

    public void setBlogApp(String blogApp) {
        this.blogApp = blogApp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public String getAtAuthorName() {
        return atAuthorName;
    }

    public void setAtAuthorName(String atAuthorName) {
        this.atAuthorName = atAuthorName;
    }

    public String getAtUserAlias() {
        return atUserAlias;
    }

    public void setAtUserAlias(String atUserAlias) {
        this.atUserAlias = atUserAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getReferenceContent() {
        return referenceContent;
    }

    public void setReferenceContent(String referenceContent) {
        this.referenceContent = referenceContent;
    }

    public MomentCommentBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.ingId);
        dest.writeString(this.authorName);
        dest.writeString(this.blogApp);
        dest.writeString(this.content);
        dest.writeString(this.userAlias);
        dest.writeString(this.avatar);
        dest.writeString(this.postTime);
        dest.writeString(this.atAuthorName);
        dest.writeString(this.atUserAlias);
        dest.writeString(this.referenceContent);
    }

    protected MomentCommentBean(Parcel in) {
        this.id = in.readString();
        this.ingId = in.readString();
        this.authorName = in.readString();
        this.blogApp = in.readString();
        this.content = in.readString();
        this.userAlias = in.readString();
        this.avatar = in.readString();
        this.postTime = in.readString();
        this.atAuthorName = in.readString();
        this.atUserAlias = in.readString();
        this.referenceContent = in.readString();
    }

    public static final Creator<MomentCommentBean> CREATOR = new Creator<MomentCommentBean>() {
        @Override
        public MomentCommentBean createFromParcel(Parcel source) {
            return new MomentCommentBean(source);
        }

        @Override
        public MomentCommentBean[] newArray(int size) {
            return new MomentCommentBean[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        // 比较
        if (obj instanceof MomentCommentBean) {
            return TextUtils.equals(this.id, ((MomentCommentBean) obj).getId());
        }
        return super.equals(obj);
    }
}
