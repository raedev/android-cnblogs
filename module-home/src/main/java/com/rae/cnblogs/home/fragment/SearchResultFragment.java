package com.rae.cnblogs.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.home.R;
import com.rae.cnblogs.home.R2;
import com.rae.cnblogs.home.adapter.SearchResultFragmentAdapter;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.db.DbSearch;
import com.rae.cnblogs.user.fragment.SearchFriendsFragment;
import com.rae.cnblogs.widget.RaeAppTabLayout;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索结果
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SearchResultFragment extends BasicFragment {

    @BindView(R2.id.tab_category)
    RaeAppTabLayout mTabLayout;

    @BindView(R2.id.vp_search)
    ViewPager mViewPager;
    private SearchResultFragmentAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_search_result;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String text = arguments.getString(Intent.EXTRA_TEXT);
            saveHistory(text);
            // 加载数据
            onLoadData(text);
        }
    }

    public void saveHistory(String keyword) {
        // 保存历史记录
        final DbSearch mDbSearch = DbFactory.getInstance().getSearch();
        Observable.just(keyword)
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        // 清除相同的记录
                        mDbSearch.deleteSearchHistory(s);
                        // 添加到历史中
                        mDbSearch.addSearchHistory(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onLoadData(String text) {
        mAdapter = new SearchResultFragmentAdapter(getChildFragmentManager());
        mAdapter.add("博客", AppRoute.newSearchFragment(text, BlogType.BLOG));
        mAdapter.add("新闻", AppRoute.newSearchFragment(text, BlogType.NEWS));
        mAdapter.add("知识库", AppRoute.newSearchFragment(text, BlogType.KB));
        mAdapter.add("园友", SearchFriendsFragment.newInstance(text));
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.clear();
        }
    }
}
