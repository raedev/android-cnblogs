package com.rae.cnblogs.blog.content;

import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * 新闻列表
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class NewsListPresenterImpl extends BasicBlogPresenterImpl {

    private INewsApi mNewsApi;

    public NewsListPresenterImpl(ContentListContract.View view) {
        super(view, BlogType.NEWS);
        mNewsApi = CnblogsApiFactory.getInstance(getContext()).getNewsApi();
    }

    @Override
    protected Observable<List<BlogBean>> onCreateObserver(CategoryBean category, int page) {
        return mNewsApi.getNews(page);
    }
}
