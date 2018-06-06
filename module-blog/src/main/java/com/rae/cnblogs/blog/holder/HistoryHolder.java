package com.rae.cnblogs.blog.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.blog.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rae on 2018/6/6.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class HistoryHolder extends SimpleViewHolder {

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

    @BindView(R2.id.tv_blog_type)
    public TextView blogTypeView;


    @BindView(R2.id.img_blog_avatar)
    public ImageView largeThumbView;

    public HistoryHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
