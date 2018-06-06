package com.rae.cnblogs.blog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.RaeTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.blog.adapter.FavoriteFragmentAdapter;
import com.rae.cnblogs.blog.favorite.FavoriteContract;
import com.rae.cnblogs.blog.favorite.FavoritePresenterImpl;
import com.rae.cnblogs.sdk.bean.TagBean;
import com.rae.cnblogs.widget.ITopScrollable;

import java.util.List;

import butterknife.BindView;

/**
 * 首页博客
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_FAVORITE)
public class FavoritesActivity extends SwipeBackBasicActivity implements RaeTabLayout.OnTabSelectedListener, FavoriteContract.View {

    private FavoriteFragmentAdapter mAdapter;

    @BindView(R2.id.tab_category)
    RaeTabLayout mTabLayout;

    @BindView(R2.id.view_pager)
    ViewPager mViewPager;

    FavoriteContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        mPresenter = new FavoritePresenterImpl(this);
        mAdapter = new FavoriteFragmentAdapter(getSupportFragmentManager());

        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.setOffscreenPageLimit(3); // 缓存3页，太多吃内存

        // 获取标签
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        mTabLayout.removeOnTabSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public void onLoadTags(List<TagBean> data) {
        // 没有任何标签
        if (Rx.getCount(data) <= 0) {
            mTabLayout.setVisibility(View.GONE);
        } else {
            mTabLayout.setVisibility(View.VISIBLE);
        }
        // 添加一个全部
        data.add(0, new TagBean("全部"));
        mAdapter.setDataList(data);
        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onTabSelected(RaeTabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(RaeTabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(RaeTabLayout.Tab tab) {
        // 再次点击返回顶部
        scrollToTop();
    }

    /**
     * 滚动到顶部
     */
    private void scrollToTop() {
        int currentItem = mViewPager.getCurrentItem();
        Fragment fragment = mAdapter.getFragment(currentItem, R.id.view_pager);
        if (fragment instanceof ITopScrollable) {
            ((ITopScrollable) fragment).scrollToTop();
        }
    }
}
