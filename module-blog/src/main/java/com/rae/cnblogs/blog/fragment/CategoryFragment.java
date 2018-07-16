package com.rae.cnblogs.blog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Button;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.R2;
import com.rae.cnblogs.blog.adapter.CategoryDragAdapter;
import com.rae.cnblogs.blog.adapter.ICategoryItemListener;
import com.rae.cnblogs.blog.category.CategoryContract;
import com.rae.cnblogs.blog.category.CategoryPresenterImpl;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.widget.drag.ItemTouchHelperAdapter;
import com.rae.cnblogs.widget.drag.OnStartDragListener;
import com.rae.cnblogs.widget.drag.SimpleItemTouchHelperCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 博客分类管理
 * Created by rae on 2018/6/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class CategoryFragment extends BasicFragment implements CategoryContract.View, OnStartDragListener {

    private CategoryContract.Presenter mPresenter;

    private ItemTouchHelper mItemTouchHelper;

    //  我的分类
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    // 推荐的分类
    @BindView(R2.id.recycler_view_unused)
    RecyclerView mRecommendRecyclerView;

    @BindView(R2.id.btn_edit)
    Button mEditView;

    //  拖拽排序适配器
    CategoryDragAdapter mDragAdapter;

    // 推荐分类适配器
    CategoryDragAdapter mRecommendAdapter;

    public static CategoryFragment newInstance(@Nullable CategoryBean selectedItem) {
        Bundle args = new Bundle();
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_category;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CategoryPresenterImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        mPresenter.start();
    }

    private void initViews() {
        // 我的分类
        ICategoryItemListener dragItemListener = new ICategoryItemListener() {

            @Override
            public void onItemRemoveClick(int position, CategoryBean item) {
                // 从我的分类中删除
                mDragAdapter.remove(position);
                mDragAdapter.notifyDataSetChanged();
                // 添加到推荐分类
                mRecommendAdapter.add(item);
                mRecommendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemClick(int position, CategoryBean item) {
                // TODO 跳转到分类
            }

            @Override
            public void onItemLongClick(int position, CategoryBean item) {
                UICompat.toastInCenter(getContext(), item.getName());
            }
        };
        mDragAdapter = new CategoryDragAdapter(this, dragItemListener);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mDragAdapter);

        // 初始化Item拖拽
        ItemTouchHelper.Callback callback = new CategorytemTouchHelperCallback(mDragAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        // 推荐分类
        ICategoryItemListener recommendItemListener = new ICategoryItemListener() {

            private Runnable mSaveRunnable = new Runnable() {
                @Override
                public void run() {
                    Log.w("rae", "保存分类");
                    mPresenter.save(mDragAdapter.getItems(), mRecommendAdapter.getItems());
                }
            };

            @Override
            public void onItemRemoveClick(int position, CategoryBean item) {
                // 不处理
            }

            @Override
            public void onItemClick(int position, CategoryBean item) {
                // 添加到我的分类中去
                mDragAdapter.add(item);
                mDragAdapter.notifyDataSetChanged();

                // 从推荐分类中删除
                mRecommendAdapter.remove(position);
                mRecommendAdapter.notifyDataSetChanged();

                // 保存分类
                mEditView.removeCallbacks(mSaveRunnable);
                mEditView.postDelayed(mSaveRunnable, 1000);
            }

            @Override
            public void onItemLongClick(int position, CategoryBean item) {
                UICompat.toastInCenter(getContext(), item.getName());
            }
        };
        mRecommendAdapter = new CategoryDragAdapter(this, recommendItemListener);
        mRecommendRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecommendRecyclerView.setHasFixedSize(true);
        mRecommendRecyclerView.setNestedScrollingEnabled(false);
        mRecommendRecyclerView.setAdapter(mRecommendAdapter);


    }

    @Override
    public void onLoadCategoryFailed(String message) {
        UICompat.failed(getContext(), message);
        if (getActivity() != null)
            getActivity().finish();
    }

    @Override
    public void onLoadCategory(List<CategoryBean> myCategories, List<CategoryBean> recommendCategories) {
        mDragAdapter.setDataList(myCategories);
        mDragAdapter.notifyDataSetChanged();

        mRecommendAdapter.setDataList(recommendCategories);
        mRecommendAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        // 非编辑模式
        boolean isEdit = mEditView.isSelected();
        if (!isEdit) {
            return;
        }
        // 开始拖拽
        int position = viewHolder.getAdapterPosition();
        if (position > 1)
            mItemTouchHelper.startDrag(viewHolder);
    }

    @OnClick(R2.id.btn_edit)
    public void onEditClick() {
        boolean isEdit = mEditView.isSelected();

        if (isEdit) {
            // 点击完成状态
            mEditView.setText(R.string.edit);
            // 保存分类
            mPresenter.save(mDragAdapter.getItems(), mRecommendAdapter.getItems());
        } else {
            // 点击编辑状态
            mEditView.setText(R.string.finish);
        }

        mEditView.setSelected(!isEdit);
        mDragAdapter.setIsEditMode(mEditView.isSelected());
        mDragAdapter.notifyDataSetChanged();

    }


    /**
     * 拖拽回调
     */
    private class CategorytemTouchHelperCallback extends SimpleItemTouchHelperCallback {

        CategorytemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            super(adapter);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            int position = target.getAdapterPosition();
            return position > 1 && super.onMove(recyclerView, source, target);
        }
    }
}
