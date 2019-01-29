package com.rae.cnblogs.discover.presenter;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

public interface IAntUserAuthContract {

    interface Presenter extends IPresenter {
        void send();
    }

    interface View extends IPresenterView {

        /**
         * 获取手机号码
         */
        String getPhoneNumber();

        /**
         * 发送成功
         */
        void onSendSuccess();

        /**
         * 发送失败
         *
         * @param message 错误消息
         */
        void onSendError(String message);
    }
}
