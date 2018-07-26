package com.rae.cnblogs.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.home.R;
import com.rae.cnblogs.home.holder.SearchHolder;

/**
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SearchAdapter extends BaseItemAdapter<String, SearchHolder> {

    private boolean isHotSearchMode; // 是否为热门搜索模式

    public SearchAdapter(boolean isHotSearchMode) {
        this.isHotSearchMode = isHotSearchMode;
    }

    @Override
    public SearchHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new SearchHolder(inflateView(parent, isHotSearchMode ? R.layout.item_hot_search : R.layout.item_search));
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position, String m) {
        holder.setTitle(m);
        if (isHotSearchMode) {
            holder.setRank(position + 1);
        }
    }
}
