package com.rae.cnblogs.home.mine;

import android.support.annotation.NonNull;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

/**
 * 我的
 */
public interface MineContract {
    interface Presenter extends IPresenter {
        /**
         * 加载用户信息
         */
        void loadUserInfo();

        /**
         * 是否已经登录
         */
        boolean isLogin();
    }

    interface View extends IPresenterView {

        /**
         * 没有登录状态
         */
        void onNotLogin();

        /**
         * 有新的反馈消息
         *
         * @param hasNewMessage 是否有新的消息
         */
        void onLoadFeedbackMessage(boolean hasNewMessage);

        /**
         * 有新的系统消息
         *
         * @param count  消息数量
         * @param hasNew 是否为新的消息
         */
        void onLoadSystemMessage(int count, boolean hasNew);

        /**
         * 加载粉丝关注数量
         *
         * @param fans    粉丝数
         * @param follows 关注数
         */
        void onLoadFansCount(String fans, String follows);

        /**
         * 加载用户信息
         *
         * @param userInfo 用户信息
         */
        void onLoadUserInfo(@NonNull UserInfoBean userInfo);

        /**
         * 登录过期
         */
        void onLoginExpired();
    }
}
