package com.rae.cnblogs.blog.comm;

import android.support.annotation.Nullable;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 内容列表协议，可以兼容：博客列表、新闻、知识库
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface ContentListContract {

    interface Presenter extends IPresenter {

        /**
         * 加载更多数据
         */
        void loadMore();
    }

    interface View extends IPresenterView, IPageView<ContentEntity> {

        /**
         * 获取当前所属分类
         */
        @Nullable
        CategoryBean getCategory();
    }
}
