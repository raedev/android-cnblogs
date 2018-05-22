package com.rae.cnblogs.moment.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.moment.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 闪存
 * Created by ChenRui on 2017/10/27 0027 10:50.
 */
public class MomentCommentHolder extends SimpleViewHolder {
    @BindView(R2.id.tv_blog_author)
    public TextView authorView;

    @BindView(R2.id.tv_blog_summary)
    public TextView summaryView;

    @BindView(R2.id.img_blog_avatar)
    public ImageView avatarView;

    @BindView(R2.id.tv_blog_date)
    public TextView dateView;

    @BindView(R2.id.ll_title)
    public View titleLayout;

    @BindView(R2.id.view_divider)
    public View dividerView;

    @BindView(R2.id.tv_title)
    public TextView commentTextView;

    public MomentCommentHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
