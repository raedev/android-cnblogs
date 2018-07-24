package com.rae.cnblogs.blog.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.ArrayList;
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

    @BindView(R2.id.tv_message)
    TextView mMessageView;

    //  拖拽排序适配器
    CategoryDragAdapter mDragAdapter;

    // 推荐分类适配器
    CategoryDragAdapter mRecommendAdapter;

    @Nullable
    private CategorytemTouchHelperCallback mItemToucCallback;
    private boolean mEnableReload;

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
                mDragAdapter.notifyItemRemoved(position);

                // 添加到推荐分类
                int p = mRecommendAdapter.getItemCount();
                mRecommendAdapter.add(p, item);
                mRecommendAdapter.notifyItemInserted(p);
            }

            @Override
            public void onItemClick(int position, CategoryBean item) {
                // 处于编辑模式的时候不可用
                if (mEditView.isSelected()) return;

                // 跳转到分类
                FragmentActivity activity = getActivity();
                if (activity == null) return;
                Intent data = new Intent();
                data.putExtra("data", item);
                activity.setResult(Activity.RESULT_OK, data);
                makeResultOK(item, mEnableReload);
                activity.finish();
            }

            @Override
            public void onItemLongClick(int position, CategoryBean item) {
                // 不处理
            }
        };
        mDragAdapter = new CategoryDragAdapter(this, dragItemListener);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mDragAdapter);

        // 初始化Item拖拽
        mItemToucCallback = new CategorytemTouchHelperCallback(mDragAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemToucCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        // 推荐分类
        ICategoryItemListener recommendItemListener = new ICategoryItemListener() {

            private Runnable mSaveRunnable = new Runnable() {
                @Override
                public void run() {
                    mPresenter.save(mDragAdapter.getItems(), mRecommendAdapter.getItems());
                    mDragAdapter.notifyDataSetChanged();
                    makeResultOK();
                }
            };

            @Override
            public void onItemRemoveClick(int position, CategoryBean item) {
                // 不处理
            }

            @Override
            public void onItemClick(int position, CategoryBean item) {
                // 添加到我的分类中去
                int p = mDragAdapter.getItemCount();
                mDragAdapter.add(p, item);
                mDragAdapter.notifyItemInserted(p);
//                mDragAdapter.notifyDataSetChanged();

                // 从推荐分类中删除
                mRecommendAdapter.remove(position);
                mRecommendAdapter.notifyItemRemoved(position);

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
        mRecommendRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
            mMessageView.setText("点击进入分类");
            // 保存分类
            mPresenter.save(mDragAdapter.getItems(), mRecommendAdapter.getItems());
            // 标志结果状态
            makeResultOK();
        } else {
            // 点击编辑状态
            mEditView.setText(R.string.finish);
            mMessageView.setText("拖动排序");
        }

        mEditView.setSelected(!isEdit);
        mDragAdapter.setIsEditMode(mEditView.isSelected());
        mDragAdapter.notifyDataSetChanged();
        if (mItemToucCallback != null) {
            mItemToucCallback.setDragEnabled(mEditView.isSelected());
        }
    }

    /**
     * 标志结果
     */
    private void makeResultOK(CategoryBean item, boolean enableReload) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent();
            intent.putExtra("enableReload", enableReload);
            intent.putExtra("data", item);
            intent.putParcelableArrayListExtra("dataSet", new ArrayList<>(mDragAdapter.getItems()));
            activity.setResult(Activity.RESULT_OK, intent);
        }
    }

    private void makeResultOK() {
        mEnableReload = true;
        makeResultOK(null, true);
    }


    /**
     * 拖拽回调
     */
    private class CategorytemTouchHelperCallback extends SimpleItemTouchHelperCallback {

        /**
         * 是否可以拖动的
         */
        private boolean mDragEnabled = false;

        CategorytemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            super(adapter);
        }

        public void setDragEnabled(boolean dragEnabled) {
            mDragEnabled = dragEnabled;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return mDragEnabled;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return mDragEnabled;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            if (!mDragEnabled) return false;
            int position = target.getAdapterPosition();
            return position > 1 && super.onMove(recyclerView, source, target);
        }
    }
}
