package com.rae.cnblogs.blog.fragment;

import android.os.Bundle;

import com.rae.cnblogs.blog.adapter.ContentItemAdapter;
import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.blog.content.NewsListPresenterImpl;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 新闻
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class NewsListFragment extends MultipleTypeBlogListFragment {

    public static NewsListFragment newInstance(CategoryBean category) {
        Bundle args = new Bundle();
        args.putParcelable("category", category);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ContentListContract.Presenter makePresenter() {
        return new NewsListPresenterImpl(this);
    }

    @Override
    protected ContentItemAdapter makeItemAdapter() {
        return new ContentItemAdapter(ContentItemAdapter.VIEW_TYPE_NEWS);
    }
}
