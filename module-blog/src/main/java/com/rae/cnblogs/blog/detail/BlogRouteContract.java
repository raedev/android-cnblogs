package com.rae.cnblogs.blog.detail;

import android.support.annotation.NonNull;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

/**
 * 博客路由协议
 * Created by rae on 2018/6/4.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface BlogRouteContract {
    interface Presenter extends IPresenter {

    }

    interface View extends IPresenterView {

        @NonNull
        String getUrl();

        /**
         * 加载实体对象
         */
        void onLoadData(ContentEntity entity);

        /**
         * 加载数据失败
         */
        void onLoadDataFailed(String message);
    }
}
