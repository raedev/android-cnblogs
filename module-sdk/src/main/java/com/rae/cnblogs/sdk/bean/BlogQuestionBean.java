package com.rae.cnblogs.sdk.bean;

import java.util.List;

public class BlogQuestionBean {
    private String id;
    private String title;
    private String diggNumber;
    private String gold;
    private String summary;
    private String author;
    private String authorAvatar;
    private String blogApp;
    private String readView;
    private String url;
    private String createdAt;
    private List<String> tags;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDiggNumber(String diggNumber) {
        this.diggNumber = diggNumber;
    }

    public String getDiggNumber() {
        return diggNumber;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getGold() {
        return gold;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setBlogApp(String blogApp) {
        this.blogApp = blogApp;
    }

    public String getBlogApp() {
        return blogApp;
    }

    public void setReadView(String readView) {
        this.readView = readView;
    }

    public String getReadView() {
        return readView;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
