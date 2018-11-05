package com.rae.cnblogs.blog.content;

import android.support.annotation.Nullable;

import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.IFriendsApi;
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
    private IFriendsApi mFriendsApi;

    public BlogListPresenterImpl(ContentListContract.View view, BlogType type) {
        super(view, type);
        if (type == BlogType.BLOGGER)
            mFriendsApi = CnblogsApiFactory.getInstance(getContext()).getFriendApi();
        mBlogApi = CnblogsApiFactory.getInstance(getContext()).getBlogApi();
    }

    public BlogListPresenterImpl(ContentListContract.View view) {
        this(view, BlogType.BLOG);
    }

    @Override
    protected Observable<List<BlogBean>> onCreateObserver(@Nullable CategoryBean category, int page) {
        if (category == null) {
            return Observable.error(new NullPointerException("the category is null."));
        }
        if (getBlogType() == BlogType.BLOGGER) {

            // 博主的博客列表
            return mFriendsApi.getBlogList(page, category.getCategoryId());
        }
        // 博客列表接口
        return mBlogApi.getBlogList(page, category.getType(), category.getParentId(), category.getCategoryId());
    }
}
