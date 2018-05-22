package com.rae.cnblogs.moment.holder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
public class MomentHolder extends SimpleViewHolder {

//    @BindView(R2.id.ll_blog_author_layout)
//    public View authorLayout;

    @BindView(R2.id.img_blog_avatar)
    public ImageView avatarView;

    @BindView(R2.id.tv_blog_author)
    public TextView authorView;

    @BindView(R2.id.tv_blog_summary)
    public TextView summaryView;

    @BindView(R2.id.tv_blog_date)
    public TextView dateView;


    @Nullable
    @BindView(R2.id.tv_blog_comment)
    public TextView commentView;

    @BindView(R2.id.recycler_view)
    public RecyclerView mRecyclerView;

    @Nullable
    @BindView(R2.id.img_close)
    public View deleteView; // 删除

    @Nullable
    @BindView(R2.id.btn_blogger_follow)
    public Button followView; // 关注
    @Nullable
    @BindView(R2.id.img_thumb)
    public ImageView thumbView; // 单张图片
    @Nullable
    @BindView(R2.id.tv_android_tag)
    public View androidTagView; // 客户端标志

    public MomentHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
