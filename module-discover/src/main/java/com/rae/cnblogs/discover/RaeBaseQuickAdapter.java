package com.rae.cnblogs.discover;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.rae.cnblogs.widget.PlaceholderView;

import java.util.List;

public abstract class RaeBaseQuickAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    @NonNull
    private PlaceholderView mPlaceholderView;

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
        mPlaceholderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setEmptyView(mPlaceholderView);

    }

    @Override
    protected K createBaseViewHolder(View view) {
        if (view == getEmptyView()) {
            int padding = QMUIDisplayHelper.dp2px(view.getContext(), 20);
            view.setPadding(padding, padding, padding, padding);
        }
        return super.createBaseViewHolder(view);
    }

    @Override
    public void setNewData(@Nullable List<T> data) {
        super.setNewData(data);
        dismissLoading();
    }

    private void dismissLoading() {
        mPlaceholderView.empty();
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
