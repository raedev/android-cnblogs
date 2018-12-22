package com.rae.cnblogs.blog.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.holder.ItemLoadingViewHolder;
import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.holder.ContentItemViewHolder;
import com.rae.cnblogs.theme.ThemeCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class ContentItemAdapter extends BaseItemAdapter<ContentEntity, SimpleViewHolder> {

    /* 正常类型 */
    public static final int VIEW_TYPE_NORMAL = 0;

    /*新闻类型*/
    public static final int VIEW_TYPE_NEWS = 1;

    private final int mViewType;

    private boolean mEnableCountLayout = true; // 显示评论 喜欢 阅读数的布局

    public ContentItemAdapter() {
        this(VIEW_TYPE_NORMAL);
    }

    public ContentItemAdapter(int viewType) {
        mViewType = viewType;
        List<ContentEntity> loadData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            loadData.add(new ContentEntity());
        }
        setDataList(loadData);
    }

    @Override
    public int getItemViewType(int position) {
        // 没有设置数据，处于加载状态
        ContentEntity dataItem = getDataItem(position);
        if (dataItem == null || dataItem.getId() == null) {
            return VIEW_TYPE_LOADING;
        }
        return mViewType;
    }

    public void setEnableCountLayout(boolean enableCountLayout) {
        mEnableCountLayout = enableCountLayout;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_LOADING)
            // 加载中视图
            return new ItemLoadingViewHolder(inflateView(parent, R.layout.item_list_loading));
        else if (viewType == VIEW_TYPE_NEWS)
            // 新闻视图
            return new ContentItemViewHolder(inflateView(parent, R.layout.item_news_list));
        else
            // 内容项目视图
            return new ContentItemViewHolder(inflateView(parent, R.layout.item_blog_list));
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position, ContentEntity m) {
        if (holder instanceof ContentItemViewHolder) {
            onBindContentItemViewHolder((ContentItemViewHolder) holder, m);
        }
    }

    /**
     * 绑定内容项目
     */
    private void onBindContentItemViewHolder(ContentItemViewHolder holder, ContentEntity m) {
        holder.authorView.setText(m.getAuthor());
        holder.titleView.setText(m.getTitle());
        holder.summaryView.setText(m.getSummary());
        holder.dateView.setText(m.getDate());
        holder.readerView.setText(m.getViewCount());
        holder.likeView.setText(m.getLikeCount());
        holder.commentView.setText(m.getCommentCount());
        UICompat.setVisibility(holder.countLayout, mEnableCountLayout);

        // 显示头像
        AppImageLoader.displayAvatar(m.getAvatar(), holder.avatarView);
        // 隐藏没有头像地址
        UICompat.setVisibility(holder.authorLayout, !TextUtils.isEmpty(m.getAvatar()));

        // 显示预览图
        showThumbImages(m.getThumbs(), holder);

        int titleColor = m.isRead() ? getColor("ph4") : getColor("ph1");
        int summaryColor = m.isRead() ? getColor("ph4") : getColor("ph2");

        holder.titleView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), titleColor));
        holder.summaryView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), summaryColor));
        holder.itemView.setTag(m);

    }

    private int getColor(String name) {
        return ThemeCompat.getColor(getContext(), name);
    }

    /**
     * 预览图处理
     */
    private void showThumbImages(List<String> thumbs, ContentItemViewHolder holder) {
        if (Rx.isEmpty(thumbs)) {
            //  没有预览图
            holder.largeThumbView.setVisibility(View.GONE);
            holder.thumbLayout.setVisibility(View.GONE);
        } else if (thumbs.size() < 3 && thumbs.size() > 0) {
            // 一张预览图
            holder.largeThumbView.setVisibility(View.VISIBLE);
            holder.thumbLayout.setVisibility(View.GONE);
            AppImageLoader.display(thumbs.get(0), holder.largeThumbView);
        } else if (thumbs.size() >= 3) {
            holder.largeThumbView.setVisibility(View.GONE);
            holder.thumbLayout.setVisibility(View.VISIBLE);
            // 取三张预览图
            AppImageLoader.display(thumbs.get(0), holder.thumbOneView);
            AppImageLoader.display(thumbs.get(1), holder.thumbTwoView);
            AppImageLoader.display(thumbs.get(2), holder.thumbThreeView);
        } else {
            holder.largeThumbView.setVisibility(View.GONE);
            holder.thumbLayout.setVisibility(View.GONE);
        }
    }
}
