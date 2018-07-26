package com.rae.cnblogs.blog.fragment;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.blog.content.SearchBlogListPresenterImpl;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 搜索
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_FRAGMENT_BLOG_SEARCH)
public class SearchBlogListFragment extends MultipleTypeBlogListFragment {

    public static SearchBlogListFragment newInstance(String keyword, BlogType type) {
        Bundle args = new Bundle();
        CategoryBean category = new CategoryBean();
        category.setName(keyword);
        category.setType(type.getTypeName());
        args.putParcelable("category", category);
        args.putString("type", type.getTypeName());
        SearchBlogListFragment fragment = new SearchBlogListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ContentListContract.Presenter makePresenter() {
        BlogType blogType;
        if (getArguments() != null) {
            String type = getArguments().getString("type", BlogType.BLOG.getTypeName());
            blogType = BlogType.typeOf(type);
        } else {
            blogType = BlogType.BLOG;
        }
        return new SearchBlogListPresenterImpl(this, blogType);
    }
}
