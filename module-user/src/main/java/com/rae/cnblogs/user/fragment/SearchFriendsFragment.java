package com.rae.cnblogs.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.user.friends.FriendsContract;
import com.rae.cnblogs.user.friends.ISearchListener;
import com.rae.cnblogs.user.friends.SearchFriendsPresenterImpl;

/**
 * Created by rae on 2018/7/24.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SearchFriendsFragment extends FriendsFragment implements ISearchListener {

    public static SearchFriendsFragment newInstance(String keyword) {

        Bundle args = new Bundle();
        args.putString(Intent.EXTRA_TEXT, keyword);
        SearchFriendsFragment fragment = new SearchFriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FriendsContract.Presenter onCreatePresenter() {
        return new SearchFriendsPresenterImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPlaceholderView.dismiss();
        mAppLayout.setEnabled(false);
        if (getArguments() != null) {
            String text = getArguments().getString(Intent.EXTRA_TEXT);
            onSearch(text);
        }
    }

    @Override
    public void onSearch(CharSequence text) {
        if (TextUtils.isEmpty(text)) return;
        mPlaceholderView.loading();
        mAppLayout.setEnabled(true);
        mPresenter.onSearch(text);
        // 键盘
        if (getActivity() != null)
            UICompat.hideSoftInputFromWindow(getActivity());
    }
}
