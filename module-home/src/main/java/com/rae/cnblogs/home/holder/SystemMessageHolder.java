package com.rae.cnblogs.home.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.home.R;
import com.rae.cnblogs.home.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SystemMessageHolder extends SimpleViewHolder {
    @BindView(R2.id.img_thumb)
    ImageView mThumbImageView;
    @BindView(R2.id.tv_title)
    TextView mTitleView;
    @BindView(R2.id.tv_date)
    TextView mDateView;

    public SystemMessageHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getThumbImageView() {
        return mThumbImageView;
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    public TextView getDateView() {
        return mDateView;
    }
}
