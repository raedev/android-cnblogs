package com.rae.cnblogs.blog.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.R2;
import com.rae.cnblogs.blog.adapter.ContentItemAdapter;
import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.blog.content.BlogListPresenterImpl;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.ITopScrollable;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 博客列表
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class MultipleTypeBlogListFragment extends BasicFragment implements ContentListContract.View, ITopScrollable {

    public static MultipleTypeBlogListFragment newInstance(CategoryBean category) {
        Bundle args = new Bundle();
        args.putParcelable("category", category);
        MultipleTypeBlogListFragment fragment = new MultipleTypeBlogListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R2.id.content)
    AppLayout mAppLayout;

    @BindView(R2.id.rec_blog_list)
    RaeRecyclerView mRecyclerView;

    @BindView(R2.id.blog_list_placeholder)
    PlaceholderView mPlaceholderView;

    private CategoryBean mCategory;
    private ContentItemAdapter mAdapter;
    private ContentListContract.Presenter mPresenter;


    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_list;
    }

    /**
     * [继承类需要重写] 创建Presenter
     */
    protected ContentListContract.Presenter makePresenter() {
        return new BlogListPresenterImpl(this);
    }

    /**
     * [继承类需要重写] 创建适配器
     */
    protected ContentItemAdapter makeItemAdapter() {
        return new ContentItemAdapter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = makePresenter();
        mAdapter = makeItemAdapter();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCategory = arguments.getParcelable("category");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPlaceholderView.dismiss();
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setAdapter(mAdapter);

        // 下拉刷新
        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.start();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mRecyclerView.isOnTop();
            }
        });

        // 上拉加载
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });

        // 重试
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mPresenter.start();
            }
        });

        // 注册数据监听
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (mAdapter.getItemCount() < 0) {
                    mPlaceholderView.empty();
                } else {
                    mPlaceholderView.dismiss();
                }
            }
        });

        // 点击跳转都详情
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<ContentEntity>() {
            @Override
            public void onItemClick(ContentEntity item) {
                AppRoute.routeToContentDetail(getContext(), item);
            }
        });


        mPresenter.start();
    }

    @NonNull
    @Override
    public CategoryBean getCategory() {
        return mCategory;
    }

    @Override
    public void onNoMoreData() {
        mAppLayout.refreshComplete();
        mRecyclerView.setNoMore(true);
    }

    @Override
    public void onEmptyData(String msg) {
        mAppLayout.refreshComplete();
        mRecyclerView.setNoMore(false);
        mPlaceholderView.empty(msg);
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadData(List<ContentEntity> data) {
        mAppLayout.refreshComplete();
        mRecyclerView.setNoMore(false);
        mRecyclerView.setLoadingMoreEnabled(true);
        mAdapter.setDataList(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoginExpired() {
        // 不用实现
    }

    @Override
    public void scrollToTop() {
        if (mRecyclerView.isOnTop()) {
            mAppLayout.autoRefresh();
        } else {
            UICompat.scrollToTop(mRecyclerView);
        }
    }
}
