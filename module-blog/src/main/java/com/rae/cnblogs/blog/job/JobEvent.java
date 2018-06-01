package com.rae.cnblogs.blog.job;

/**
 * JOB event
 * Created by ChenRui on 2017/7/28 0028 18:33.
 */
public final class JobEvent {
    /**
     * 启动博客内容下载Action
     */
    public final static int ACTION_JOB_BLOG_CONTENT = 1;

    private int action;


    public JobEvent(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
