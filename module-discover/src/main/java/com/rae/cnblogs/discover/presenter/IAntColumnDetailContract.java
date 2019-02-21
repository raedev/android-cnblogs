package com.rae.cnblogs.discover.presenter;

import com.antcode.sdk.model.AntColumnInfo;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

public interface IAntColumnDetailContract {

    interface Presenter extends IPresenter {

        /**
         * 订阅
         */
        void subscribe();
    }

    interface View extends IPresenterView {

        // 获取专栏ID
        String getColumnId();

        // 加载专栏详情
        void onLoadColumnDetail(AntColumnInfo columnInfo);

        // 加载数据失败
        void onLoadDataError(String message);

        // 订阅失败
        void onSubscribeError(String message);

        // 订阅成功
        void onSubscribeSuccess();

        // 专栏是否订阅
        void onColumnSubscribe(boolean subscribe);
    }
}
