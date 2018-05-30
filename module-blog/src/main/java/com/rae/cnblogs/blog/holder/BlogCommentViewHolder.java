package com.rae.cnblogs.blog.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.blog.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 博客评论View
 * Created by ChenRui on 2016/12/15 22:49.
 */
public class BlogCommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.tv_blog_author)
    public TextView authorTitleView;

    @BindView(R2.id.img_blog_avatar)
    public ImageView avatarView;

    @BindView(R2.id.tv_blog_date)
    public TextView dateView;

    @BindView(R2.id.tv_blog_summary)
    public TextView contentView;

    // 下面的要复用，不能用BindView
    @BindView(R2.id.ll_quote_comment)
    public View quoteLayout;
    @BindView(R2.id.ll_blog_author_layout)
    public View authorLayout;
    @BindView(R2.id.tv_quote_blog_app)
    public TextView quoteBlogAppView;
    @BindView(R2.id.tv_quote_content)
    public TextView quoteContentView;

    public BlogCommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
