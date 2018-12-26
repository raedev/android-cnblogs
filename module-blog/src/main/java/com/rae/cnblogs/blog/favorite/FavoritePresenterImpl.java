package com.rae.cnblogs.blog.favorite;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.bean.TagBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏列表
 * Created by rae on 2018/6/1.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class FavoritePresenterImpl extends BasicPresenter<FavoriteContract.View> implements FavoriteContract.Presenter {
    private IBookmarksApi mBookmarksApi;

    public FavoritePresenterImpl(FavoriteContract.View view) {
        super(view);
        mBookmarksApi = CnblogsApiFactory.getInstance(getContext()).getBookmarksApi();
    }

    @Override
    protected void onStart() {
        AndroidObservable.create(mBookmarksApi.getTags())
                .with(this)
                .subscribe(new ApiDefaultObserver<List<TagBean>>() {
                    @Override
                    protected void onError(String message) {
                        getView().onLoadFailed(message);
                    }

                    @Override
                    protected void accept(List<TagBean> tagBeans) {
                        tagBeans.add(0, new TagBean("全部"));
                        getView().onLoadTags(tagBeans);
                    }
                });
    }
}
