package com.rae.cnblogs.user.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.holder.FriendsViewHolder;

/**
 * 朋友适配器
 * Created by ChenRui on 2017/2/23 01:36.
 */
public class FriendsAdapter extends BaseItemAdapter<UserInfoBean, FriendsViewHolder> {

    public interface OnFollowClickListener {
        void onFollowClick(TextView view, UserInfoBean m);
    }

    private OnFollowClickListener mOnFollowClickListener;

    public void setOnFollowClickListener(OnFollowClickListener onFollowClickListener) {
        mOnFollowClickListener = onFollowClickListener;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new FriendsViewHolder(inflateView(parent, R.layout.item_friends));
    }

    @Override
    public void onBindViewHolder(final FriendsViewHolder holder, int position, final UserInfoBean m) {
        AppImageLoader.displayAvatar(m.getAvatar(), holder.avatarView);
        holder.nameView.setText(TextUtils.isEmpty(m.getRemarkName()) ? m.getDisplayName() : m.getRemarkName());
        holder.accountView.setText(m.getBlogApp());
        // 已经关注，显示取消关注
        holder.setActionSelected(m.isHasFollow());
        holder.setActionEnable(true);
        if (m.isHasFollow()) {
            holder.setActionText(R.string.unfollow);
        } else {
            holder.setActionText(R.string.following);
        }
        holder.setActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFollowClickListener.onFollowClick((TextView) v, m);
            }
        });
    }
}
