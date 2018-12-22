package com.rae.cnblogs.sdk.db.model;

import com.rae.cnblogs.sdk.bean.BlogType;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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
     * {@link BlogType#getTypeName()}
     */

    private String blogType; // 博客类型，如：博客、新闻、知识库


    @Generated(hash = 1553554788)
    public UserBlogInfo(Long id, String blogId, boolean isRead, boolean isLiked,
            boolean isBookmarks, String blogType) {
        this.id = id;
        this.blogId = blogId;
        this.isRead = isRead;
        this.isLiked = isLiked;
        this.isBookmarks = isBookmarks;
        this.blogType = blogType;
    }

    @Generated(hash = 20220642)
    public UserBlogInfo() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isBookmarks() {
        return isBookmarks;
    }

    public void setBookmarks(boolean bookmarks) {
        isBookmarks = bookmarks;
    }

    public String getBlogType() {
        return blogType;
    }

    public void setBlogType(String blogType) {
        this.blogType = blogType;
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
