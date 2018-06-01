package com.rae.cnblogs.blog.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.blog.fragment.FavoritesFragment;
import com.rae.cnblogs.sdk.bean.TagBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏列表Fragment适配器
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class FavoriteFragmentAdapter extends FragmentPagerAdapter {

    private final FragmentManager mFragmentManager;

    private List<TagBean> mDataList = new ArrayList<>();

    public FavoriteFragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        TagBean m = mDataList.get(position);
        return FavoritesFragment.newInstance(m.getName());
    }

    @Override
    public int getCount() {
        return Rx.getCount(mDataList);
    }

    /**
     * TAB的标题
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList == null ? "" : mDataList.get(position).getName();
    }

    @Nullable
    public Fragment getFragment(int index, int viewPagerId) {
        return mFragmentManager.findFragmentByTag("android:switcher:" + viewPagerId + ":" + index);
    }

    public void setDataList(@Nullable List<TagBean> dataList) {
        mDataList = dataList;
    }
}
