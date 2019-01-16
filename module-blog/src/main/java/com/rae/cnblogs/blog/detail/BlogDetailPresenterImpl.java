package com.rae.cnblogs.blog.detail;

import android.support.annotation.Nullable;
import android.util.Log;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IBlogApi;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 博客内容详情
 * Created by rae on 2018/5/28.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class BlogDetailPresenterImpl extends ContentDetailPresenterImpl {
    private IBlogApi mBlogApi;

    public BlogDetailPresenterImpl(ContentDetailContract.View view) {
        super(view);
        mBlogApi = CnblogsApiFactory.getInstance(getContext()).getBlogApi();
    }

    @Override
    protected Observable<String> onCreateContentObservable(String id) {
        return mBlogApi.getBlogContent(id);
    }

    @Override
    protected Observable<String> onFetchContentSource(Observable<String> local, Observable<String> network) {
        // 数据源3： 从原文地址获取
        Observable<String> source = mBlogApi
                .getBlogContentSource(getView().getContentEntity().getUrl())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String content) {
                        updateContent(content);
                        Log.i("rae","读取原文地址内容！");
                        return content;
                    }
                });
        return Observable.concat(local, network, source).take(1);
    }

    @Nullable
    @Override
    protected Observable<Empty> onCreateLikeObservable(ContentEntity entity, boolean liked) {
        if (liked) {
            return mBlogApi.unLikeBlog(entity.getId(), entity.getAuthorId());
        } else {
            return mBlogApi.likeBlog(entity.getId(), entity.getAuthorId());
        }
    }

    @Override
    protected Observable<Empty> onCreateCommentObservable(ContentEntity m, String content) {
        return mBlogApi.addBlogComment(m.getId(), m.getAuthorId(), "0", content);
    }
}
