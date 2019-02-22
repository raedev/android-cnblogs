package com.rae.cnblogs.discover.presenter;

import com.antcode.sdk.model.AntArticleInfo;
import com.antcode.sdk.model.AntColumnInfo;
import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

public interface IAntUserColumnDetailContract {

    interface Presenter extends IPresenter {
        void loadData();
        void loadMore();

        void unsubscribe();
    }

    interface View extends IPageView<AntArticleInfo>, IPresenterView {

        // 获取专栏ID
        String getColumnId();

        // 加载专栏详情
        void onLoadColumnDetail(AntColumnInfo columnInfo);

        // 加载数据失败
        void onLoadDataError(String message);

        void onLoginExpired();

        void onUnsubscribeSuccess();

        void onUnsubscribeError(String message);
    }
}
