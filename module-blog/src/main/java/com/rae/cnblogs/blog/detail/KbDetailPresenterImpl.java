package com.rae.cnblogs.blog.detail;

import android.support.annotation.Nullable;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IBlogApi;

import io.reactivex.Observable;

/**
 * Created by rae on 2018/12/21.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class KbDetailPresenterImpl extends ContentDetailPresenterImpl {

    private IBlogApi mBlogApi;


    public KbDetailPresenterImpl(ContentDetailContract.View view) {
        super(view);
        mBlogApi = CnblogsApiFactory.getInstance(getContext()).getBlogApi();
    }

    @Override
    protected Observable<String> onCreateContentObservable(String id) {
        return mBlogApi.getKbContent(id);
    }

    @Nullable
    @Override
    protected Observable<Empty> onCreateLikeObservable(ContentEntity entity, boolean liked) {
        if (liked) {
            getView().onLikeSuccess();
            return null;
        } else {
            return mBlogApi.likeKb(entity.getId());
        }
    }

    @Override
    protected Observable<Empty> onCreateCommentObservable(ContentEntity m, String content) {
        return null;
    }
}
