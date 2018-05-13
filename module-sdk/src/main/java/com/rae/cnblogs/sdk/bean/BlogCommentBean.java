package com.rae.cnblogs.sdk.bean;

import android.text.TextUtils;

/**
 * 博客评论实体
 * Created by ChenRui on 2016/12/10 18:00.
 */
public class BlogCommentBean {

    private String id;
    private String authorName;
    private String blogApp;
    private String date;
    private String body;
    private String quote; // 引用
    private String quoteBlogApp; // 引用者
    private String like;
    private String unlike;
    private String mAvatar;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getQuoteBlogApp() {
        return quoteBlogApp;
    }

    public void setQuoteBlogApp(String quoteBlogApp) {
        this.quoteBlogApp = quoteBlogApp;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getUnlike() {
        return unlike;
    }

    public void setUnlike(String unlike) {
        this.unlike = unlike;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlogCommentBean) {
            BlogCommentBean comment = (BlogCommentBean) obj;
            return TextUtils.equals(comment.getId(), this.getId());
        }
        return super.equals(obj);
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getAvatar() {
        return mAvatar;
    }
}
