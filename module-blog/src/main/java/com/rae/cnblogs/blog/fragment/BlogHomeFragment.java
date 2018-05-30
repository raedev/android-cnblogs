package com.rae.cnblogs.blog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.RaeTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.R2;
import com.rae.cnblogs.blog.adapter.BlogHomeFragmentAdapter;
import com.rae.cnblogs.blog.home.BlogHomeContract;
import com.rae.cnblogs.blog.home.BlogHomePresenterImpl;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.widget.ITopScrollable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页博客
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class BlogHomeFragment extends BasicFragment implements BlogHomeContract.View, RaeTabLayout.OnTabSelectedListener {

    public static BlogHomeFragment newInstance() {
        return new BlogHomeFragment();
    }

    private BlogHomeFragmentAdapter mAdapter;

    @BindView(R2.id.tab_category)
    RaeTabLayout mTabLayout;

    @BindView(R2.id.vp_blog_list)
    ViewPager mViewPager;

    @BindView(R.id.tv_search)
    TextView mSearchView;

    private BlogHomeContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new BlogHomePresenterImpl(this);
        mAdapter = new BlogHomeFragmentAdapter(getChildFragmentManager());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3); // 缓存3页，太多吃内存
        mTabLayout.setupWithViewPager(mViewPager);
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        mTabLayout.removeOnTabSelectedListener(this);
        super.onDestroy();
    }

    /**
     * 加载分类
     */
    @Override
    public void onLoadCategory(List<CategoryBean> data) {
        mAdapter.clear();
        mAdapter.setDataList(data);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 加载热搜
     */
    @Override
    public void onLoadHotSearchData(String keyword) {
        Animation anim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        anim.setDuration(800);
        mSearchView.startAnimation(anim);
        mSearchView.setText(keyword);
    }


    /**
     * 分类点击跳转到分类编辑
     */
    @OnClick(R2.id.img_edit_category)
    public void onCategoryClick(View view) {
        AppRoute.routeToCategoryForResult(getActivity());
    }

    /**
     * LOGO点击返回顶部
     */
    @OnClick(R2.id.img_actionbar_logo)
    public void onLogoClick() {
        scrollToTop();
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
        Fragment fragment = mAdapter.getFragment(currentItem, R.id.vp_blog_list);
        if (fragment instanceof ITopScrollable) {
            ((ITopScrollable) fragment).scrollToTop();
        }
    }
}
