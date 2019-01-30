package com.rae.cnblogs.discover.presenter;

import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.BlogQuestionBean;

public interface IBlogQuestionContract {

    // 热搜
    int TYPE_UNSOLVED = 0;
    // 阅读
    int TYPE_HIGH_SCORE = 1;
    // 收藏
    int TYPE_MY = 2;

    interface Presenter extends IPresenter {

        void loadMore();

    }

    interface View extends IPresenterView, IPageView<BlogQuestionBean> {

        /**
         * 参考 静态变量
         * {@link #TYPE_UNSOLVED}
         */
        int getType();
    }
}
