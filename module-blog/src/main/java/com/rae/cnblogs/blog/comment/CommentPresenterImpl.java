package com.rae.cnblogs.blog.comment;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * 评论
 * Created by rae on 2018/5/30.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class CommentPresenterImpl extends BasicPresenter<CommentContract.View> implements CommentContract.Presenter {

    private PageObservable<BlogCommentBean> mPageObservable;
    private IBlogApi mBlogApi;
    private INewsApi mNewsApi;

    public CommentPresenterImpl(CommentContract.View view) {
        super(view);
        mBlogApi = CnblogsApiFactory.getInstance(getContext()).getBlogApi();
        mNewsApi = CnblogsApiFactory.getInstance(getContext()).getNewsApi();

        mPageObservable = new PageObservable<BlogCommentBean>(getView(), this) {
            @Override
            protected Observable<List<BlogCommentBean>> onCreateObserver(int page) {
                ContentEntity entity = getView().getContentEntity();
                BlogType type = BlogType.typeOf(entity.getType());
                if (type == BlogType.NEWS) {
                    return mNewsApi.getNewsComment(entity.getId(), page);
                }
                return mBlogApi.getBlogComments(page, entity.getId(), entity.getAuthorId());
            }
        };
    }

    @Override
    protected void onStart() {
        mPageObservable.start();
    }

    @Override
    public void onDeleteComment(final BlogCommentBean comment) {
        ContentEntity entity = getView().getContentEntity();
        Observable<Empty> observable;
        if (BlogType.typeOf(entity.getType()) == BlogType.NEWS) {
            observable = mNewsApi.deleteNewsComment(comment.getId());
        } else {
            observable = mBlogApi.deleteBlogComment(comment.getId());
        }

        AndroidObservable
                .create(observable)
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onDeleteCommentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        getView().onDeleteCommentSuccess(comment);
                    }
                });
    }

    @Override
    public void onLoadMore() {
        mPageObservable.loadMore();
    }

    @Override
    public void onPostComment(String content, BlogCommentBean entity, boolean isReference) {
        if (!UserProvider.getInstance().isLogin()) {
            getView().onLoginExpired();
            return;
        }
        // 根据博客、新闻、知识库的类型
        ContentEntity m = getView().getContentEntity();
        BlogType type = BlogType.typeOf(m.getType());
        Observable<Empty> observable;
        if (type == BlogType.NEWS) {
            observable = makeNewCommentObservable(content, entity, isReference, m);
            AppMobclickAgent.onClickEvent(getContext(), "NEWS_COMMENT");
        } else {
            AppMobclickAgent.onClickEvent(getContext(), "BLOG_COMMENT");
            observable = makeBlogCommentObservable(content, entity, isReference, m);
        }

        AndroidObservable
                .create(observable)
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onCommentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        getView().onCommentSuccess();
                    }
                });

    }

    private Observable<Empty> makeNewCommentObservable(String content, BlogCommentBean entity, boolean isReference, ContentEntity m) {
        if (mNewsApi == null)
            mNewsApi = CnblogsApiFactory.getInstance(getContext()).getNewsApi();
        // 引用评论处理
        String parentId = "0";
        if (entity != null) {
            content = isReference ? ApiUtils.getCommentContent(entity, content) : ApiUtils.getAtCommentContent(entity, content);
            parentId = entity.getId();
        }
        return mNewsApi.addNewsComment(m.getId(), parentId, content);
    }

    private Observable<Empty> makeBlogCommentObservable(String content, BlogCommentBean entity, boolean isReference, ContentEntity m) {
        // 引用评论处理
        String parentId = "0";
        if (entity != null) {
            content = isReference ? ApiUtils.getCommentContent(entity, content) : ApiUtils.getAtCommentContent(entity, content);
            parentId = entity.getId();
        }
        return mBlogApi.addBlogComment(m.getId(), m.getAuthorId(), parentId, content);
    }
}
