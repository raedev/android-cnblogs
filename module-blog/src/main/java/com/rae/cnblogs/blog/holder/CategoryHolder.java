package com.rae.cnblogs.blog.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.blog.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rae on 2018/6/29.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class CategoryHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.tv_name)
    TextView mTitleView;

    @BindView(R2.id.img_remove)
    ImageView mRemoveView;

    public CategoryHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setIsEditMode(boolean isEditMode) {
        UICompat.setVisibility(mRemoveView, isEditMode);
    }

    public void setOnRemoveClickListener(View.OnClickListener onRemoveClickListener) {
        mRemoveView.setOnClickListener(onRemoveClickListener);
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }
}
