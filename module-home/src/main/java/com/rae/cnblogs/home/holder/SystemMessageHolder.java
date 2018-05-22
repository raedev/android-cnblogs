package com.rae.cnblogs.home.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.home.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SystemMessageHolder extends SimpleViewHolder {
    @BindView(R.id.img_thumb)
    ImageView mThumbImageView;
    @BindView(R.id.tv_title)
    TextView mTitleView;
    @BindView(R.id.tv_date)
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
