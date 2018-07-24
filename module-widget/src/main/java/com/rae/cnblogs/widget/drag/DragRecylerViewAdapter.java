package com.rae.cnblogs.widget.drag;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 带拖动排序的适配器
 * Created by rae on 2018/6/29.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public abstract class DragRecylerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements ItemTouchHelperAdapter {
    private final OnStartDragListener mDragStartListener;
    protected final List<T> mItems = new ArrayList<>();


    public DragRecylerViewAdapter(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
    }

    public void setDataList(List<T> data) {
        mItems.clear();
        mItems.addAll(data);
    }

    public void remove(int position) {
        Log.i("Rae", "移除：" + position);
        mItems.remove(position);
    }

    public void add(int position, T item) {
        mItems.add(position, item);
    }

    public T getDataItem(int position) {
        return mItems.get(position);
    }

    public List<T> getItems() {
        return mItems;
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, int position) {

        if (holder.itemView != null) {
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        swapCollections(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    // 重新排序
    private void swapCollections(List<T> items, int fromPosition, int toPosition) {
        T from = items.get(fromPosition);
        items.remove(fromPosition);
        items.add(toPosition, from);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }
}
