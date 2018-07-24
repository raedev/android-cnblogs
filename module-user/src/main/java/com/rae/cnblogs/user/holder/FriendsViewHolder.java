package com.rae.cnblogs.user.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.user.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 粉丝和关注
 * Created by ChenRui on 2017/2/23 01:36.
 */
public class FriendsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.tv_user_name)
    public TextView nameView;
    @BindView(R2.id.tv_user_account)
    public TextView accountView;

    @BindView(R2.id.img_user_avatar)
    public ImageView avatarView;

    @BindView(R2.id.btn_action)
    Button actionView;

    public FriendsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setActionEnable(boolean actionEnable) {
        actionView.setEnabled(actionEnable);
    }

    public void setActionText(int resId) {
        this.actionView.setText(resId);
    }

    public void setActionClickListener(View.OnClickListener listener) {
        actionView.setOnClickListener(listener);
    }

    public void setActionSelected(boolean selected) {
        actionView.setSelected(selected);
    }
}
