package com.rae.cnblogs.blog.comment;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;

/**
 * 评论
 * Created by rae on 2018/5/30.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface CommentContract {
    interface Presenter extends IPresenter {
        /**
         * 删除评论
         *
         * @param comment 评论
         */
        void onDeleteComment(BlogCommentBean comment);

        /**
         * 加载更多
         */
        void onLoadMore();

        /**
         * 发表评论
         *
         * @param content     发表内容
         * @param entity      引用的评论
         * @param isReference 是否引用评论
         */
        void onPostComment(String content, BlogCommentBean entity, boolean isReference);
    }

    interface View extends IPresenterView, IPageView<BlogCommentBean> {

        ContentEntity getContentEntity();


        /**
         * 评论失败
         */
        void onCommentFailed(String message);

        /**
         * 评论成功
         */
        void onCommentSuccess();

        /**
         * 删除评论失败
         *
         * @param message 错误消息
         */
        void onDeleteCommentFailed(String message);

        /**
         * 删除评论成功
         *
         * @param comment 已删除的评论
         */
        void onDeleteCommentSuccess(BlogCommentBean comment);
    }
}
