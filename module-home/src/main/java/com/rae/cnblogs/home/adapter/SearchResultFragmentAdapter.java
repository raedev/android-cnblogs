package com.rae.cnblogs.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果适配器
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SearchResultFragmentAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments;
    private final List<String> mTitles;

    public SearchResultFragmentAdapter(FragmentManager fm) {
        this(fm, new ArrayList<Fragment>());
    }

    public SearchResultFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
        mTitles = new ArrayList<>();
    }

    public void add(String title, Fragment fm) {
        mTitles.add(title);
        add(fm);
    }

    public void add(Fragment fm) {
        mFragments.add(fm);
    }

    public void clear() {
        mFragments.clear();
        mTitles.clear();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments == null ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.size() <= 0 ? null : mTitles.get(position % mTitles.size());
    }
}
