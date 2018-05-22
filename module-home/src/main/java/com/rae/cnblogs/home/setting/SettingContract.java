package com.rae.cnblogs.home.setting;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.VersionInfo;

/**
 * 设置
 * Created by rae on 2018/5/14.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface SettingContract {
    /**
     *
     */
    interface Presenter extends IPresenter {

        /**
         * 退出登录
         */
        void logout();

        /**
         * 检查更新
         */
        void checkUpdate();
    }

    interface View extends IPresenterView {

        /**
         * 没有登录状态
         */
        void onNotLogin();

        /**
         * 加载版本信息
         *
         * @param versionName 版本号
         */
        void onLoadVersionInfo(String versionName);

        /**
         * 退出登录
         */
        void onLogout();

        /**
         * 没有版本更新
         */
        void onNoVersion();

        /**
         * 发现有版本更新
         *
         * @param versionInfo 版本信息
         */
        void onNewVersion(VersionInfo versionInfo);
    }
}
