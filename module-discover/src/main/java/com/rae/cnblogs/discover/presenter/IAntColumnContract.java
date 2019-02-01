package com.rae.cnblogs.discover.presenter;

import com.antcode.sdk.model.AntColumnInfo;
import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

public interface IAntColumnContract {

    int TYPE_RECOMMEND = 0;
    int TYPE_MY = 1;

    interface Presenter extends IPresenter {
        void loadMore();
    }

    interface View extends IPresenterView, IPageView<AntColumnInfo> {

        int getType();
    }
}
