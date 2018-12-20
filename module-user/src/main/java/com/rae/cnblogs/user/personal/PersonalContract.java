package com.rae.cnblogs.user.personal;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface PersonalContract {
    interface Presenter extends IPresenter {

    }

    interface View extends IPresenterView {

        /**
         * 加载用户信息
         *
         * @param user
         */
        void onLoadUserInfo(UserInfoBean user);

        /**
         * 登录过期
         */
        void onLoginExpired();
    }
}
