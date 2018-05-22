package com.rae.cnblogs.home.main;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.VersionInfo;

/**
 * 首页协议
 */
public interface MainContract {

    interface Presenter extends IPresenter {

    }

    interface View extends IPresenterView {

        /**
         * 有新版本更新
         *
         * @param versionInfo 版本信息
         */
        void onNewVersion(VersionInfo versionInfo);
    }
}
