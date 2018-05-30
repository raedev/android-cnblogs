package com.rae.cnblogs.blog.detail;

import android.support.annotation.NonNull;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

/**
 * 内容详情
 * Created by rae on 2018/5/28.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface ContentDetailContract {
    interface Presenter extends IPresenter {

        /**
         * 点赞
         *
         * @param selected 当前状态
         */
        void onLike(boolean selected);

        /**
         * 收藏
         *
         * @param selected 当前状态
         */
        void onCollect(boolean selected);

        /**
         * 发表评论
         *
         * @param content 评论内容
         */
        void onComment(String content);
    }

    interface View extends IPresenterView {

        /**
         * 获取内容
         */
        @NonNull
        ContentEntity getContentEntity();

        /**
         * 收藏失败
         */
        void onCollectFailed(String message);

        /**
         * 收藏成功
         */
        void onCollectSuccess();

        /**
         * 评论失败
         */
        void onCommentFailed(String message);

        /**
         * 评论成功
         */
        void onCommentSuccess();

        /**
         * 点赞失败
         */
        void onLikeError(String msg);

        /**
         * 点赞成功
         */
        void onLikeSuccess();

        /**
         * 加载数据失败
         *
         * @param message 错误性
         */
        void onLoadDataFailed(String message);

        /**
         * 加载数据成功
         *
         * @param data     内容
         * @param jsonData 格式化JSON的对象
         */
        void onLoadDataSuccess(BlogBean data, String jsonData);

        /**
         * 加载用户博客信息
         */
        void onLoadUserBlogInfo(@NonNull UserBlogInfo m);

        /**
         * 操作需要登录
         */
        void onNeedLogin();

        /**
         * 不支持取消收藏的时候
         */
        void onNotSupportCollecte();
    }
}
