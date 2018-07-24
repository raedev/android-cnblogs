package com.rae.cnblogs.user.friends;

import android.text.TextUtils;

/**
 * Created by rae on 2018/7/23.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SearchFriendsPresenterImpl extends FriendsPresenterImpl {

    public SearchFriendsPresenterImpl(FriendsContract.View view) {
        super(view, "search");
    }

    @Override
    protected void onStart() {
        if (TextUtils.isEmpty(mKeyword)) return;
        super.onStart();
    }

    @Override
    public void onLoadMore() {
        if (TextUtils.isEmpty(mKeyword)) return;
        super.onLoadMore();
    }
}
