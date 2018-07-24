package com.rae.cnblogs.user.friends;

import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

/**
 * Created by rae on 2018/7/23.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface FriendsContract {
    interface Presenter extends IPresenter {
        void onLoadMore();

        /**
         * 搜索
         *
         * @param text 搜索文本
         */
        void onSearch(CharSequence text);

        /**
         * 取消关注
         */
        void unFollow(UserInfoBean m);

        /**
         * 加关注
         */
        void follow(UserInfoBean m);
    }

    interface View extends IPageView<UserInfoBean>, IPresenterView {
        String getBlogApp();

        /**
         * 加关注/取消关注 发生错误
         *
         * @param message 错误信息
         */
        void onFollowError(String message);

        /**
         * 加关注成功
         */
        void onFollowSuccess();

        /**
         * 取消关注成功
         */
        void onUnFollowSuccess();
    }
}
