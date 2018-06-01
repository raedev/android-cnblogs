package com.rae.cnblogs.blog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.blog.content.BookmarkListPresenterImpl;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 我的收藏
 * Created by rae on 2018/6/1.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class FavoritesFragment extends MultipleTypeBlogListFragment {
    /**
     * @param tag 标签，如果为空则是所有
     */
    public static FavoritesFragment newInstance(String tag) {
        Bundle args = new Bundle();
        if (!TextUtils.equals("全部", tag)) {
            CategoryBean category = new CategoryBean();
            category.setName(tag);
            args.putParcelable("category", category);
        }
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ContentListContract.Presenter makePresenter() {
        return new BookmarkListPresenterImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getAdapter().setOnItemClickListener(new BaseItemAdapter.onItemClickListener<ContentEntity>() {
            @Override
            public void onItemClick(ContentEntity item) {
                // 跳转网页
                AppRoute.routeToWeb(getContext(), item.getUrl());
            }
        });
    }
}
