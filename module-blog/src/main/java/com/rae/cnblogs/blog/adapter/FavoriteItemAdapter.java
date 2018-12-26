package com.rae.cnblogs.blog.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.holder.ItemLoadingViewHolder;
import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.holder.FavoriteItemHolder;

public class FavoriteItemAdapter extends ContentItemAdapter {

    @Override
    public SimpleViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING)
            // 加载中视图
            return new ItemLoadingViewHolder(inflateView(parent, R.layout.item_list_loading));

        return new FavoriteItemHolder(inflateView(parent, R.layout.item_favorite));
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position, ContentEntity m) {
        if (holder instanceof FavoriteItemHolder) {
            onBindItemViewHolder((FavoriteItemHolder) holder, m);
        }
    }

    private void onBindItemViewHolder(FavoriteItemHolder holder, ContentEntity m) {
        holder.titleView.setText(m.getTitle());
        holder.summaryView.setText(m.getSummary());
        holder.dateView.setText(m.getDate());
    }
}
