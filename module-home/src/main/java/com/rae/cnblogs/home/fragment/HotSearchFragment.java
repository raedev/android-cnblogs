package com.rae.cnblogs.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.home.R;
import com.rae.cnblogs.home.R2;
import com.rae.cnblogs.home.adapter.SearchAdapter;
import com.rae.cnblogs.home.search.HotSearchContract;
import com.rae.cnblogs.home.search.HotSearchPresenterImpl;
import com.rae.cnblogs.sdk.event.SearchEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 热门搜索
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class HotSearchFragment extends BasicFragment implements HotSearchContract.View, BaseItemAdapter.onItemClickListener<String> {


    @BindView(R2.id.tv_hot_search)
    TextView mHotSearchView;
    @BindView(R2.id.recycler_view_hot_search)
    RecyclerView mHotSearchRecyclerView;

    @BindView(R2.id.ll_search_history)
    LinearLayout mSearchHistoryLayout;
    @BindView(R2.id.recycler_view_search_history)
    RecyclerView mSearchHistoryRecyclerView;

    private SearchAdapter mHotSearchAdapter; // 热门搜索适配器
    private SearchAdapter mSearchHistoryAdapter; // 历史记录适配器

    private HotSearchContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_hot_search;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new HotSearchPresenterImpl(this);
        mHotSearchAdapter = new SearchAdapter(true);
        mSearchHistoryAdapter = new SearchAdapter(false);
        mHotSearchAdapter.setOnItemClickListener(this);
        mSearchHistoryAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHotSearchAdapter.setOnItemClickListener(null);
        mSearchHistoryAdapter.setOnItemClickListener(null);
        mPresenter.destroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHotSearchRecyclerView.setHasFixedSize(true);
        mHotSearchRecyclerView.setNestedScrollingEnabled(false);
        mSearchHistoryRecyclerView.setHasFixedSize(true);
        mSearchHistoryRecyclerView.setNestedScrollingEnabled(false);

        mHotSearchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mSearchHistoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mHotSearchRecyclerView.setAdapter(mHotSearchAdapter);
        mSearchHistoryRecyclerView.setAdapter(mSearchHistoryAdapter);
        setHotSearchLayoutVisibility(View.GONE);
        setSearchHistoryLayoutVisibility(View.GONE);
        mPresenter.start();
    }

    @Override
    public void onItemClick(Context context, String item) {
        // 通知搜索事件
        EventBus.getDefault().post(new SearchEvent(item, true));
    }

    private void setHotSearchLayoutVisibility(int visibility) {
        mHotSearchView.setVisibility(visibility);
        mHotSearchRecyclerView.setVisibility(visibility);
    }

    private void setSearchHistoryLayoutVisibility(int visibility) {
        mSearchHistoryLayout.setVisibility(visibility);
    }

    @Override
    public void onEmptyHotSearchData() {
        mHotSearchAdapter.clear();
        mHotSearchAdapter.notifyDataSetChanged();
        setHotSearchLayoutVisibility(View.GONE);
    }

    @Override
    public void onLoadHotSearchData(List<String> data) {
        setHotSearchLayoutVisibility(View.VISIBLE);
        mHotSearchAdapter.setDataList(data);
        mHotSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEmptySearchHistoryData() {
        mSearchHistoryAdapter.clear();
        mSearchHistoryAdapter.notifyDataSetChanged();
        setSearchHistoryLayoutVisibility(View.GONE);
    }

    @Override
    public void onLoadSearchHistoryData(List<String> data) {
        setSearchHistoryLayoutVisibility(View.VISIBLE);
        mSearchHistoryAdapter.setDataList(data);
        mSearchHistoryAdapter.notifyDataSetChanged();
    }

    @OnClick(R2.id.img_delete)
    public void onClearHistoryClick() {
        mPresenter.clearSearchHistory();
    }
}
