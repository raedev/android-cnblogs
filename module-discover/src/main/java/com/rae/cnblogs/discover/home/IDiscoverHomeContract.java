package com.rae.cnblogs.discover.home;

import com.antcode.sdk.model.AntAdInfo;
import com.antcode.sdk.model.AntColumnInfo;
import com.antcode.sdk.model.AntTabInfo;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

import java.util.List;

public interface IDiscoverHomeContract {

    interface Presenter extends IPresenter {

    }

    interface View extends IPresenterView {

        void onLoadAds(List<AntAdInfo> ads);

        void onLoadColumns(List<AntColumnInfo> columns);

        void onLoadCategories(List<AntTabInfo> homeTabs);

        /**
         * 加载错误
         */
        void onLoadColumnError(String message);

        /**
         * 加载错误
         */
        void onLoadAdError(String message);
    }
}
