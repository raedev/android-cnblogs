package com.rae.cnblogs.blog.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.blog.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 博客列表ITEM
 * Created by ChenRui on 2016/12/2 0002 19:43.
 */
public class ContentItemViewHolder extends SimpleViewHolder {

    @BindView(R2.id.ll_blog_author_layout)
    public View authorLayout;

    @BindView(R2.id.img_blog_avatar)
    public ImageView avatarView;

    @BindView(R2.id.tv_blog_author)
    public TextView authorView;

    @BindView(R2.id.tv_blog_title)
    public TextView titleView;

    @BindView(R2.id.tv_blog_summary)
    public TextView summaryView;

    @BindView(R2.id.tv_blog_date)
    public TextView dateView;

    @BindView(R2.id.tv_blog_view)
    public TextView readerView;

    @BindView(R2.id.tv_blog_like)
    public TextView likeView;

    @BindView(R2.id.tv_blog_comment)
    public TextView commentView;


    @BindView(R2.id.img_blog_list_large_thumb)
    public ImageView largeThumbView;

    @BindView(R2.id.layout_blog_list_thumb)
    public View thumbLayout;

    @BindView(R2.id.img_blog_list_thumb_one)
    public ImageView thumbOneView;

    @BindView(R2.id.img_blog_list_thumb_two)
    public ImageView thumbTwoView;

    @BindView(R2.id.img_blog_list_thumb_three)
    public ImageView thumbThreeView;

    @BindView(R2.id.ll_count_layout)
    @Nullable
    public ViewGroup countLayout;

    public ContentItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
