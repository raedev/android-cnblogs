package com.rae.cnblogs.sdk.db.model;

import com.rae.cnblogs.sdk.bean.BlogType;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 用户的博客信息
 * Created by ChenRui on 2017/1/25 0025 17:04.
 */
@Entity(nameInDb = "blog_info")
public class UserBlogInfo {

    /**
     * 自增ID
     */
    @Id(autoincrement = true)
    private Long id;

    private String blogId;

    private boolean isRead;

    private boolean isLiked;

    private boolean isBookmarks;

    /**
     * 内容
     */
    private String content;

    /**
     * {@link BlogType#getTypeName()}
     */

    private String blogType; // 博客类型，如：博客、新闻、知识库


    @Generated(hash = 20220642)
    public UserBlogInfo() {
    }

    @Generated(hash = 869507996)
    public UserBlogInfo(Long id, String blogId, boolean isRead, boolean isLiked,
                        boolean isBookmarks, String content, String blogType) {
        this.id = id;
        this.blogId = blogId;
        this.isRead = isRead;
        this.isLiked = isLiked;
        this.isBookmarks = isBookmarks;
        this.content = content;
        this.blogType = blogType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean getIsLiked() {
        return this.isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean getIsBookmarks() {
        return this.isBookmarks;
    }

    public void setIsBookmarks(boolean isBookmarks) {
        this.isBookmarks = isBookmarks;
    }
}
