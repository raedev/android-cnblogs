package com.rae.cnblogs.user.fragment;

import com.rae.cnblogs.user.friends.FollowPresenterImpl;
import com.rae.cnblogs.user.friends.FriendsContract;

/**
 * Created by rae on 2018/7/23.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class FollowFragment extends FriendsFragment {
    @Override
    protected FriendsContract.Presenter onCreatePresenter() {
        return new FollowPresenterImpl(this);
    }
}
