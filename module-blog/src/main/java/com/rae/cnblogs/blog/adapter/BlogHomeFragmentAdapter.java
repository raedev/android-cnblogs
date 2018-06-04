package com.rae.cnblogs.blog.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.blog.fragment.MultipleTypeBlogListFragment;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

/**
 * 首页分类博客
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class BlogHomeFragmentAdapter extends FragmentPagerAdapter {

    private final FragmentManager mFragmentManager;
    private List<CategoryBean> mDataList;

    public BlogHomeFragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // 每次都重新加载
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        CategoryBean m = mDataList.get(position);
        return MultipleTypeBlogListFragment.newInstance(m);
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
        return mDataList.get(position).getName();
    }

    @Nullable
    public Fragment getCurrent(int viewId, int position) {
        return mFragmentManager.findFragmentByTag("android:switcher:" + viewId + ":" + position);
    }

    public void setDataList(List<CategoryBean> dataList) {
        mDataList = dataList;
    }

    public void clear() {
        if (mDataList != null)
            mDataList.clear();
    }
}
