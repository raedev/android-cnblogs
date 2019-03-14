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
    @Nullable
    public View authorLayout;

    @BindView(R2.id.img_blog_avatar)
    @Nullable
    public ImageView avatarView;

    @BindView(R2.id.tv_blog_author)
    @Nullable
    public TextView authorView;

    @BindView(R2.id.tv_blog_title)
    @Nullable
    public TextView titleView;

    @BindView(R2.id.tv_blog_summary)
    @Nullable
    public TextView summaryView;

    @BindView(R2.id.tv_blog_date)
    @Nullable
    public TextView dateView;

    @BindView(R2.id.tv_blog_view)
    @Nullable
    public TextView readerView;

    @BindView(R2.id.tv_blog_like)
    @Nullable
    public TextView likeView;

    @BindView(R2.id.tv_blog_comment)
    @Nullable
    public TextView commentView;


    @BindView(R2.id.img_blog_list_large_thumb)
    @Nullable
    public ImageView largeThumbView;

    @BindView(R2.id.layout_blog_list_thumb)
    @Nullable
    public View thumbLayout;

    @BindView(R2.id.img_blog_list_thumb_one)
    @Nullable
    public ImageView thumbOneView;

    @BindView(R2.id.img_blog_list_thumb_two)
    @Nullable
    public ImageView thumbTwoView;

    @BindView(R2.id.img_blog_list_thumb_three)
    @Nullable
    public ImageView thumbThreeView;

    @BindView(R2.id.ll_count_layout)
    @Nullable
    public ViewGroup countLayout;

    public ContentItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setText(@Nullable TextView view, String text) {
        if (view != null) view.setText(text);
    }

    public void setTextColor(@Nullable TextView view, int color) {
        if (view != null) view.setTextColor(color);
    }

    public void setVisibility(@Nullable View view, int visibility) {
        if (view != null) view.setVisibility(visibility);
    }

    public void setVisibility(@Nullable View view, boolean visibility) {
        if (view != null) view.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}
