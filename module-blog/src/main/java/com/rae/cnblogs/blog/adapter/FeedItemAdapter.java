package com.rae.cnblogs.blog.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.holder.FeedViewHolder;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.cnblogs.theme.ThemeCompat;

/**
 * 动态 - 适配器
 * Created by ChenRui on 2017/3/16 16:40.
 */
public class FeedItemAdapter extends BaseItemAdapter<UserFeedBean, FeedViewHolder> {

    @Override
    public FeedViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new FeedViewHolder(inflateView(parent, R.layout.item_blogger_feed));
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position, UserFeedBean m) {
        holder.authorTitleView.setText(m.getAuthor());
        holder.dateView.setText(m.getFeedDate());
        holder.contentView.setText(m.getContent());
//        holder.feedActionView.setText(m.getAction());
        holder.feedActionTitleView.setText(String.format("#%s# %s", m.getAction(), m.getTitle()));
        holder.itemView.setTag(m);
        if ("发表评论".equals(m.getAction())) {
            holder.feedLayout.setVisibility(View.GONE);
            holder.contentView.setText(String.format("#%s# %s", m.getAction(), m.getContent()));
        } else {
            holder.feedLayout.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(m.getAvatar())) {
            AppImageLoader.displayAvatar(m.getAvatar(), holder.avatarView);
        }

        holder.feedActionView.setImageResource(ThemeCompat.isNight() ? R.drawable.link_allshare_pressed : R.drawable.ic_share_link);
    }
}
