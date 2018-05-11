package com.rae.cnblogs.basic;

/**
 * 公共的Presenter接口
 * Created by ChenRui on 2018/4/18.
 */
public interface IPresenter {

    /**
     * 开始加载数据
     */
    void start();

    /**
     * 释放数据
     */
    void destroy();
}
