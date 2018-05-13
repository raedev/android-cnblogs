package com.rae.cnblogs.basic;

import com.rae.cnblogs.basic.rx.LifecycleObserver;

/**
 * 公共的Presenter接口
 * Created by ChenRui on 2018/4/18.
 */
public interface IPresenter extends LifecycleObserver {

    /**
     * 开始加载数据
     */
    void start();

    /**
     * 释放数据
     */
    void destroy();


}
