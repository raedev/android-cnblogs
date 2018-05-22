package com.rae.cnblogs.moment.holder;

import android.view.View;
import android.widget.ImageView;

import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.moment.R;

/**
 * Created by ChenRui on 2017/11/1 0001 0:51.
 */
public class MomentImageHolder extends SimpleViewHolder {
    ImageView mImageView;

    public ImageView getImageView() {
        return mImageView;
    }

    public MomentImageHolder(View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.img_thumb);
    }
}
