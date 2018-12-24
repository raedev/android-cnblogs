package com.rae.cnblogs.blog.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.RaeTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.R2;
import com.rae.cnblogs.blog.adapter.BlogHomeFragmentAdapter;
import com.rae.cnblogs.blog.home.BlogHomeContract;
import com.rae.cnblogs.blog.home.BlogHomePresenterImpl;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.widget.ITopScrollable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页博客
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_FRAGMENT_HOME)
public class BlogHomeFragment extends BasicFragment implements BlogHomeContract.View, RaeTabLayout.OnTabSelectedListener, ITopScrollable {

    public static BlogHomeFragment newInstance() {
        return new BlogHomeFragment();
    }


    private BlogHomeFragmentAdapter mAdapter;

    // 当前用户查看的分类Tab
    @Nullable
    private CharSequence mCurrentCategoryName;

    @BindView(R2.id.tab_category)
    RaeTabLayout mTabLayout;

    @BindView(R2.id.vp_blog_list)
    ViewPager mViewPager;

    @BindView(R2.id.tv_search)
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
    public void onLoadCategory(final List<CategoryBean> data) {
        mAdapter.clear();
        mAdapter.setDataList(data);
        mAdapter.notifyDataSetChanged();
        relocation(data);
    }

    /**
     * 重新找索引
     */
    private void relocation(List<CategoryBean> data) {
        // 找到所在的索引
        int index = 0;
        if (mCurrentCategoryName != null) {
            int size = data.size();
            for (int i = 0; i < size; i++) {
                CategoryBean item = data.get(i);
                if (TextUtils.equals(item.getName(), mCurrentCategoryName)) {
                    index = i;
                    break;
                }
            }
        }

        mViewPager.setCurrentItem(index); // 选择
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
    @Override
    public void scrollToTop() {
        Fragment fragment = mAdapter.getCurrent(mViewPager.getId(), mViewPager.getCurrentItem());
        if (fragment instanceof ITopScrollable) {
            ((ITopScrollable) fragment).scrollToTop();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppRoute.REQ_CODE_CATEGORY && resultCode == Activity.RESULT_OK && data != null) {
            CategoryBean category = data.getParcelableExtra("data");
            ArrayList<CategoryBean> dataSet = data.getParcelableArrayListExtra("dataSet");
            mPresenter.reorganizeData(dataSet); // 重新整理数据
            reloadTab(dataSet, category, data.getBooleanExtra("enableReload", false));
        }
    }


    /**
     * 重新加载TAB选项卡
     *
     * @param category             分类
     * @param enableClearFragments 是否重新加载
     */
    private void reloadTab(List<CategoryBean> data, @Nullable CategoryBean category, boolean enableClearFragments) {
        if (category == null) {
            // 保存当前用户查看的Tab索引
            int position = mTabLayout.getSelectedTabPosition();
            mCurrentCategoryName = mAdapter.getPageTitle(Math.max(0, position));

        } else {
            mCurrentCategoryName = category.getName();
        }

        if (enableClearFragments)
            clearFragments();


        onLoadCategory(data);
    }

    private void clearFragments() {
        // 销毁当前的
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment.isAdded()) {
                fragmentTransaction.detach(fragment);
            }
            fragmentTransaction.remove(fragment);
        }

        fragmentTransaction.commitNow();

        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R2.id.fl_search)
    public void onSearchClick() {
        if (UserProvider.getInstance().isLogin()) {
            AppRoute.routeToSearch(getContext());
        } else {
            AppRoute.routeToLogin(getContext());
        }
    }
}
