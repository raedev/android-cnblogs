package com.rae.cnblogs.discover.home;

import com.antcode.sdk.model.AntAdInfo;
import com.antcode.sdk.model.AntColumnInfo;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

import java.util.List;

public interface IDiscoverHomeContract {

    interface Presenter extends IPresenter {

    }

    interface View extends IPresenterView {

        void onLoadAds(List<AntAdInfo> ads);

        void onLoadColumns(List<AntColumnInfo> columns);
    }
}
