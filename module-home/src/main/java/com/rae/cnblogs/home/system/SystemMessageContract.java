package com.rae.cnblogs.home.system;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.SystemMessageBean;

import java.util.List;

/**
 * Created by rae on 2018/5/15.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface SystemMessageContract {
    interface Presenter extends IPresenter {

    }

    interface View extends IPresenterView {

        /**
         * 没有数据
         *
         * @param msg 错误消息
         */
        void onEmptyData(String msg);

        void onLoadData(List<SystemMessageBean> data);
    }
}
