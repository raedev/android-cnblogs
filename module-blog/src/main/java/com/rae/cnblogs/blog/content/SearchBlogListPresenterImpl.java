package com.rae.cnblogs.blog.content;

import android.support.annotation.Nullable;

import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SearchBlogListPresenterImpl extends BasicBlogPresenterImpl {

    private ISearchApi mSearchApi;

    /**
     * @param view
     * @param blogType 博客类型
     */
    public SearchBlogListPresenterImpl(ContentListContract.View view, BlogType blogType) {
        super(view, blogType);
        mSearchApi = CnblogsApiFactory.getInstance(getContext()).getSearchApi();
    }

    @Override
    protected Observable<List<BlogBean>> onCreateObserver(@Nullable CategoryBean category, int page) {
        if (category == null)
            throw new NullPointerException("搜索需要传递category");
        BlogType blogType = getBlogType();
        String name = category.getName();
        switch (blogType) {
            case NEWS:
                return mSearchApi.searchNewsList(name, page);
            case KB:
                return mSearchApi.searchKbList(name, page);
            case BLOGGER:
                return mSearchApi.searchBlogAppList(name, page);
            default:
                return mSearchApi.searchBlogList(name, page);
        }
    }
}
