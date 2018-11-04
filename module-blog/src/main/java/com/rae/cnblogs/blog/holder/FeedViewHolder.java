package com.rae.cnblogs.blog.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.blog.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 动态
 * Created by ChenRui on 2017/3/16 15:58.
 */
public class FeedViewHolder extends RecyclerView.ViewHolder {
    @BindView(R2.id.tv_blog_author)
    public TextView authorTitleView;

    @BindView(R2.id.img_blog_avatar)
    public ImageView avatarView;

    @BindView(R2.id.tv_blog_date)
    public TextView dateView;
    @BindView(R2.id.tv_blog_summary)
    public TextView contentView;

    @BindView(R2.id.img_blog_action)
    public ImageView feedActionView;

    @BindView(R2.id.ll_feed)
    public View feedLayout;

    @BindView(R2.id.tv_blog_action_title)
    public TextView feedActionTitleView;

    public FeedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
