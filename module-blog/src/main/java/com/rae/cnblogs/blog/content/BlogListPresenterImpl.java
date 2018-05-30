package com.rae.cnblogs.blog.content;

import android.support.annotation.Nullable;

import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * 博客列表
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class BlogListPresenterImpl extends BasicBlogPresenterImpl {

    private IBlogApi mBlogApi;

    public BlogListPresenterImpl(ContentListContract.View view) {
        super(view, BlogType.BLOG);
        mBlogApi = CnblogsApiFactory.getInstance(getContext()).getBlogApi();
    }

    @Override
    protected Observable<List<BlogBean>> onCreateObserver(@Nullable CategoryBean category, int page) {
        if (category == null) {
            return Observable.error(new NullPointerException("the category is null."));
        }
        // 博客列表接口
        return mBlogApi.getBlogList(page, category.getType(), category.getParentId(), category.getCategoryId());
    }
}
