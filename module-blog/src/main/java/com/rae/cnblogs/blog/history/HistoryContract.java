package com.rae.cnblogs.blog.history;

import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.BlogBean;

/**
 * Created by rae on 2018/6/6.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface HistoryContract {
    interface Presenter extends IPresenter {
        void onLoadMore();

        void clear();
    }

    interface View extends IPresenterView, IPageView<BlogBean> {

    }
}
