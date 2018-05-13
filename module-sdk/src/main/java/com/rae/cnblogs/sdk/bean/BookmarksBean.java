package com.rae.cnblogs.sdk.bean;

import java.util.List;

/**
 * 收藏夹
 * Created by ChenRui on 2017/1/14 13:55.
 */
public class BookmarksBean {
    /**
     * WzLinkId : 1
     * Title : sample string 2
     * LinkUrl : sample string 3
     * Summary : sample string 4
     * Tags : ["sample string 1","sample string 2"]
     * DateAdded : 2017-01-14T13:54:49.5577061+08:00
     * FromCNBlogs : true
     */

    private int WzLinkId;
    private String Title;
    private String LinkUrl;
    private String Summary;
    private String DateAdded;
    private boolean FromCNBlogs;
    private List<String> Tags;

    public BookmarksBean() {
    }

    public BookmarksBean(String title, String summary, String linkUrl) {
        Title = title;
        LinkUrl = linkUrl;
        Summary = summary;
        FromCNBlogs = true;
    }

    public int getWzLinkId() {
        return WzLinkId;
    }

    public void setWzLinkId(int WzLinkId) {
        this.WzLinkId = WzLinkId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getLinkUrl() {
        return LinkUrl;
    }

    public void setLinkUrl(String LinkUrl) {
        this.LinkUrl = LinkUrl;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String Summary) {
        this.Summary = Summary;
    }

    public String getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(String DateAdded) {
        this.DateAdded = DateAdded;
    }

    public boolean isFromCNBlogs() {
        return FromCNBlogs;
    }

    public void setFromCNBlogs(boolean FromCNBlogs) {
        this.FromCNBlogs = FromCNBlogs;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> Tags) {
        this.Tags = Tags;
    }
}
