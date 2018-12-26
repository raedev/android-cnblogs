package com.rae.cnblogs.blog.holder;

import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.blog.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteItemHolder extends SimpleViewHolder {
    @BindView(R2.id.tv_blog_title)
    public TextView titleView;

    @BindView(R2.id.tv_blog_summary)
    public TextView summaryView;

    @BindView(R2.id.tv_blog_date)
    public TextView dateView;

    public FavoriteItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
