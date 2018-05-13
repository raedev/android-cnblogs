package com.rae.cnblogs.sdk.db.model;

import com.rae.cnblogs.sdk.bean.BlogType;

/**
 * 用户的博客信息
 * Created by ChenRui on 2017/1/25 0025 17:04.
 */
//@Table(name = "blog_info")
public class UserBlogInfo {


    private String blogId;


    private boolean isRead;

    private boolean isLiked;

    private boolean isBookmarks;

    private String content;

    /**
     * {@link BlogType#getTypeName()}
     */

    private String blogType; // 博客类型，如：博客、新闻、知识库

    public String getBlogType() {
        return blogType;
    }

    public void setBlogType(String blogType) {
        this.blogType = blogType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public boolean isBookmarks() {
        return isBookmarks;
    }

    public void setBookmarks(boolean bookmarks) {
        isBookmarks = bookmarks;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
