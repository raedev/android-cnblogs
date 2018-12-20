package com.rae.cnblogs.blog.fragment;

import android.os.Bundle;

import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.blog.content.KbListPresenterImpl;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 知识库列表
 * Created by rae on 2018/5/28.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class KbListFragment extends MultipleTypeBlogListFragment {

    public static KbListFragment newInstance(CategoryBean category) {
        Bundle args = new Bundle();
        args.putParcelable("category", category);
        KbListFragment fragment = new KbListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ContentListContract.Presenter makePresenter() {
        return new KbListPresenterImpl(this);
    }
}
