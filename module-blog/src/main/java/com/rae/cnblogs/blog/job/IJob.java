package com.rae.cnblogs.blog.job;

/**
 * 工作
 * Created by ChenRui on 2017/7/27 0027 15:36.
 */
public interface IJob {

    /**
     * 运行
     */
    void run();

    /**
     * 取消
     */
    void cancel();
}
