package com.rae.cnblogs.discover.presenter;

import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.HotSearchBean;

public interface IRankingContract {

    // 热搜
    int TYPE_HOT_SEARCH = 0;
    // 阅读
    int TYPE_TOP_READ = 1;
    // 收藏
    int TYPE_TOP_FAVORITE = 2;
    // 大神
    int TYPE_TOP_AUTHOR = 3;

    interface Presenter extends IPresenter {

        void loadMore();

    }

    interface View extends IPresenterView, IPageView<HotSearchBean> {

        /**
         * 参考 静态变量
         * {@link #TYPE_HOT_SEARCH}
         */
        int getType();
    }
}
