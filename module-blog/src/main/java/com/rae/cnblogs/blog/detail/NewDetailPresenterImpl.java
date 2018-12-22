package com.rae.cnblogs.blog.detail;

import android.support.annotation.Nullable;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.INewsApi;

import io.reactivex.Observable;

/**
 * Created by rae on 2018/12/21.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class NewDetailPresenterImpl extends ContentDetailPresenterImpl {

    private INewsApi mNewsApi;


    public NewDetailPresenterImpl(ContentDetailContract.View view) {
        super(view);
        mNewsApi = CnblogsApiFactory.getInstance(getContext()).getNewsApi();

    }

    @Override
    protected Observable<String> onCreateContentObservable(String id) {
        return mNewsApi.getNewsContent(id);
    }

    @Nullable
    @Override
    protected Observable<Empty> onCreateLikeObservable(ContentEntity entity, boolean liked) {
        if (liked) {
            getView().onLikeSuccess();
            return null;
        } else {
            return mNewsApi.like(entity.getId());
        }
    }

    @Override
    protected Observable<Empty> onCreateCommentObservable(ContentEntity m, String content) {
        return mNewsApi.addNewsComment(m.getId(), "0", content);
    }
}
