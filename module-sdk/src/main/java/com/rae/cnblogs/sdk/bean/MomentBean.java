package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 闪存
 * Created by ChenRui on 2017/9/25 0025 17:03.
 */
public class MomentBean implements Parcelable {

    // 标志
    private String id;

    // 作者昵称
    private String authorName;

    // 作者头像
    private String avatar;

    private String blogApp;

    // 发布时间
    private String postTime;

    // 评论数量
    private String commentCount;

    private String content;

    // 用户，评论用到
    private String userAlias;

    // 源地址
    private String sourceUrl;

    // 是否发自于Android客户端
    private boolean isAndroidClient;

    // 评论列表
    private List<MomentCommentBean> commentList;

    // 图片列表
    private List<String> imageList;

    public List<MomentCommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<MomentCommentBean> commentList) {
        this.commentList = commentList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isAndroidClient() {
        return isAndroidClient;
    }

    public void setAndroidClient(boolean androidClient) {
        isAndroidClient = androidClient;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBlogApp() {
        return blogApp;
    }

    public void setBlogApp(String blogApp) {
        this.blogApp = blogApp;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }


    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public MomentBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.authorName);
        dest.writeString(this.avatar);
        dest.writeString(this.blogApp);
        dest.writeString(this.postTime);
        dest.writeString(this.commentCount);
        dest.writeString(this.content);
        dest.writeString(this.userAlias);
        dest.writeString(this.sourceUrl);
        dest.writeByte(this.isAndroidClient ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.commentList);
        dest.writeStringList(this.imageList);
    }

    protected MomentBean(Parcel in) {
        this.id = in.readString();
        this.authorName = in.readString();
        this.avatar = in.readString();
        this.blogApp = in.readString();
        this.postTime = in.readString();
        this.commentCount = in.readString();
        this.content = in.readString();
        this.userAlias = in.readString();
        this.sourceUrl = in.readString();
        this.isAndroidClient = in.readByte() != 0;
        this.commentList = in.createTypedArrayList(MomentCommentBean.CREATOR);
        this.imageList = in.createStringArrayList();
    }

    public static final Creator<MomentBean> CREATOR = new Creator<MomentBean>() {
        @Override
        public MomentBean createFromParcel(Parcel source) {
            return new MomentBean(source);
        }

        @Override
        public MomentBean[] newArray(int size) {
            return new MomentBean[size];
        }
    };
}
