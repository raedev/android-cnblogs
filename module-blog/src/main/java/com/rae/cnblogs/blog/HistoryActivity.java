package com.rae.cnblogs.blog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.ContentEntityConverter;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.blog.adapter.HistoryAdapter;
import com.rae.cnblogs.blog.history.HistoryContract;
import com.rae.cnblogs.blog.history.HistoryPresenterImpl;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 浏览记录
 * Created by rae on 2018/6/5.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_BLOG_HISTORY)
public class HistoryActivity extends SwipeBackBasicActivity implements HistoryContract.View {

    @BindView(R2.id.recycler_view)
    RaeRecyclerView mRecyclerView;

    @BindView(R2.id.app_layout)
    AppLayout mAppLayout;

    @BindView(R2.id.placeholder)
    PlaceholderView mPlaceholderView;


    @BindView(R2.id.img_edit_delete)
    ImageView mDeleteView;

    HistoryContract.Presenter mPresenter;
    private HistoryAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mPresenter = new HistoryPresenterImpl(this);
        mAdapter = new HistoryAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.start();

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

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPresenter.onLoadMore();
            }
        });

        mPlaceholderView.registerAdapterDataObserver(mAdapter);

        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<BlogBean>() {
            @Override
            public void onItemClick(Context context, BlogBean item) {
                AppRoute.routeToContentDetail(context, ContentEntityConverter.convert(item));
            }
        });
    }

    @Override
    public void onNoMoreData() {
        mRecyclerView.setNoMore(true);
    }

    @Override
    public void onEmptyData(String msg) {
        mDeleteView.setVisibility(View.GONE);
        mPlaceholderView.empty(msg);
        mRecyclerView.setNoMore(true);
        mAppLayout.refreshComplete();
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadData(List<BlogBean> data) {
        if (Rx.getCount(data) <= 0) {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            return;
        }
        UICompat.setVisibility(mDeleteView, Rx.getCount(data) > 0);
        mRecyclerView.setNoMore(false);
        mAppLayout.refreshComplete();
        mAdapter.setDataList(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoginExpired() {

    }

    /**
     * 滚动到顶部
     */
    @OnClick(R2.id.tool_bar)
    public void onToolbarClick() {
        UICompat.scrollToTop(mRecyclerView);
    }


    /**
     * 清空浏览记录
     */
    @OnClick(R2.id.img_edit_delete)
    public void onDeleteClick() {
        new DefaultDialogFragment.Builder()
                .message("是否清空浏览记录？")
                .confirmText("立即清除")
                .cancelable(true)
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.clear();
                    }
                }).show(getSupportFragmentManager());
    }
}