package com.rae.cnblogs.blog.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.holder.HistoryHolder;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;

import java.util.List;

/**
 * Created by rae on 2018/6/6.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class HistoryAdapter extends BaseItemAdapter<BlogBean, HistoryHolder> {

    @Override
    public HistoryHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new HistoryHolder(inflateView(parent, R.layout.item_history));
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position, BlogBean m) {
        holder.authorView.setText(m.getAuthor());
        holder.titleView.setText(m.getTitle());
        holder.summaryView.setText(m.getSummary());
        holder.dateView.setText(m.getPostDate());
        holder.readerView.setText(m.getViews());
        holder.likeView.setText(m.getLikes());
        holder.commentView.setText(m.getComment());

        BlogType type = BlogType.typeOf(m.getBlogType());
        holder.blogTypeView.setText(type.getDisplayName());

        // 显示图片
        List<String> thumbs = m.getThumbs();
        boolean hasImage = thumbs != null && Rx.getCount(thumbs) > 0;
        UICompat.setVisibility(holder.largeThumbView, hasImage);
        if (hasImage) {
            AppImageLoader.display(thumbs.get(0), holder.largeThumbView);
        }
    }
}
