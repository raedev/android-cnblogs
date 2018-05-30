package com.rae.cnblogs.basic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;

/**
 * 内容实体
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class ContentEntity implements Parcelable {

    // 关联ID
    private String mId;
    // 作者
    private String mAuthor;
    // 作者唯一识别号，对应blogApp
    private String mAuthorId;
    // 头像地址
    private String mAvatar;
    // 标题
    private String mTitle;
    // 摘要
    private String mSummary;
    // 发表日期
    private String mDate;
    // 浏览量
    private String mViewCount;
    // 评论数量
    private String mCommentCount;
    // 点赞数量
    private String mLikeCount;
    // 是否已读
    private boolean isRead;
    // 路径
    private String url;
    // 缩略图
    @Nullable
    private ArrayList<String> mThumbs;
    private String mType;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getViewCount() {
        return mViewCount == null ? "0" : mViewCount;
    }

    public void setViewCount(String viewCount) {
        mViewCount = viewCount;
    }

    public String getCommentCount() {
        return mCommentCount == null ? "0" : mCommentCount;
    }

    public void setCommentCount(String commentCount) {
        mCommentCount = commentCount;
    }

    public String getLikeCount() {
        return mLikeCount == null ? "0" : mLikeCount;
    }

    public void setLikeCount(String likeCount) {
        mLikeCount = likeCount;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Nullable
    public ArrayList<String> getThumbs() {
        return mThumbs;
    }

    public void setThumbs(ArrayList<String> thumbs) {
        mThumbs = thumbs;
    }

    public void setType(String type) {
        mType = type;
    }

    public ContentEntity() {
    }

    public String getType() {
        return mType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mAuthor);
        dest.writeString(this.mAuthorId);
        dest.writeString(this.mAvatar);
        dest.writeString(this.mTitle);
        dest.writeString(this.mSummary);
        dest.writeString(this.mDate);
        dest.writeString(this.mViewCount);
        dest.writeString(this.mCommentCount);
        dest.writeString(this.mLikeCount);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
        dest.writeStringList(this.mThumbs);
        dest.writeString(this.mType);
    }

    protected ContentEntity(Parcel in) {
        this.mId = in.readString();
        this.mAuthor = in.readString();
        this.mAuthorId = in.readString();
        this.mAvatar = in.readString();
        this.mTitle = in.readString();
        this.mSummary = in.readString();
        this.mDate = in.readString();
        this.mViewCount = in.readString();
        this.mCommentCount = in.readString();
        this.mLikeCount = in.readString();
        this.isRead = in.readByte() != 0;
        this.url = in.readString();
        this.mThumbs = in.createStringArrayList();
        this.mType = in.readString();
    }

    public static final Creator<ContentEntity> CREATOR = new Creator<ContentEntity>() {
        @Override
        public ContentEntity createFromParcel(Parcel source) {
            return new ContentEntity(source);
        }

        @Override
        public ContentEntity[] newArray(int size) {
            return new ContentEntity[size];
        }
    };
}
