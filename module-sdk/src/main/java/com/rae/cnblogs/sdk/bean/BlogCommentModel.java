package com.rae.cnblogs.sdk.bean;

/**
 * 实体
 * Created by ChenRui on 2016/12/10 18:28.
 */
public class BlogCommentModel {

    private int commentCount;
    private String commentsHtml;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentsHtml() {
        return commentsHtml;
    }

    public void setCommentsHtml(String commentsHtml) {
        this.commentsHtml = commentsHtml;
    }
}
