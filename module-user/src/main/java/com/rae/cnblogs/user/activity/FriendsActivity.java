package com.rae.cnblogs.user.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;
import com.rae.cnblogs.user.fragment.FansFragment;
import com.rae.cnblogs.user.fragment.FollowFragment;

import butterknife.OnClick;

/**
 * fans and follower
 * Created by rae on 2018/7/23.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_FRIENDS)
public class FriendsActivity extends SwipeBackBasicActivity {

    Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        String type = getIntent().getStringExtra("fromType");
        String blogName = getIntent().getStringExtra("bloggerName");
        String blogApp = getIntent().getStringExtra("blogApp");

        if (TextUtils.isEmpty(blogName)) {
            blogName = "我";
        }

        if (TextUtils.equals(AppRoute.ACTIVITY_FRIENDS_TYPE_FANS, type)) {
            setTitle(getString(R.string.title_fans, blogName));
            mFragment = new FansFragment();
        } else {
            setTitle(getString(R.string.title_follow, blogName));
            mFragment = new FollowFragment();
        }

        Bundle args = new Bundle();
        args.putString("blogApp", blogApp);
        mFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content, mFragment)
                .commitNowAllowingStateLoss();
    }

    @OnClick(R2.id.ll_search)
    public void onSearchClick() {
        AppRoute.routeToSearchFriends(getContext());
    }
}
