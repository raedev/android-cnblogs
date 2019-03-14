package com.rae.cnblogs.discover;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.widget.PlaceholderView;

import java.util.List;

public abstract class RaeBaseQuickAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    @NonNull
    protected PlaceholderView mPlaceholderView;

    public RaeBaseQuickAdapter(Context context, int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        initView(context);
    }

    public RaeBaseQuickAdapter(Context context, @Nullable List<T> data) {
        super(data);
        initView(context);
    }

    public RaeBaseQuickAdapter(Context context, int layoutResId) {
        super(layoutResId);
        initView(context);
    }

    @NonNull
    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {

        return super.onCreateViewHolder(parent, viewType);
    }

    protected void initView(Context context) {
        mPlaceholderView = new PlaceholderView(context);
        mPlaceholderView.setBackgroundColor(ContextCompat.getColor(context, R.color.background_divider));
        mPlaceholderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setEmptyView(mPlaceholderView);
    }

    @Override
    public void setNewData(@Nullable List<T> data) {
        super.setNewData(data);
        dismissLoading();
    }

    public void dismissLoading() {
        mPlaceholderView.dismiss();
    }

    public void showLoading() {
        mPlaceholderView.loading();
    }

    public void showEmpty(String message) {
        if (mData != null) mData.clear();
        mPlaceholderView.empty(message);
        notifyDataSetChanged();
    }


}
