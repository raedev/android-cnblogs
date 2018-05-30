package com.rae.cnblogs.blog.fragment;

import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.blog.content.KbListPresenterImpl;

/**
 * 知识库列表
 * Created by rae on 2018/5/28.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class KbListFragment extends MultipleTypeBlogListFragment {

    public static KbListFragment newInstance() {
        return new KbListFragment();
    }

    @Override
    protected ContentListContract.Presenter makePresenter() {
        return new KbListPresenterImpl(this);
    }
}
