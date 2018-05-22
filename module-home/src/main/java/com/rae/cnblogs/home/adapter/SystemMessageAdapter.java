package com.rae.cnblogs.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.home.R;
import com.rae.cnblogs.home.holder.SystemMessageHolder;
import com.rae.cnblogs.sdk.bean.SystemMessageBean;
import com.rae.cnblogs.theme.ThemeCompat;

/**
 * 系统消息
 * Created by ChenRui on 2017/9/5 0005 17:23.
 */
public class SystemMessageAdapter extends BaseItemAdapter<SystemMessageBean, SystemMessageHolder> {

    @Override
    public SystemMessageHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new SystemMessageHolder(inflateView(parent, R.layout.item_system_message));
    }

    @Override
    public void onBindViewHolder(SystemMessageHolder holder, int position, SystemMessageBean m) {
        holder.getTitleView().setText(m.getSummary());
        holder.getDateView().setText(m.getCreateTime());
        AppImageLoader.display(m.getThumbUrl(), holder.getThumbImageView());
        if (ThemeCompat.isNight()) {
            holder.getThumbImageView().setAlpha(0.4f);
        }
    }

}
