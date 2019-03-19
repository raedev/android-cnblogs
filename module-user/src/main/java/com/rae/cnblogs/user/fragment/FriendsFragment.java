package com.rae.cnblogs.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;
import com.rae.cnblogs.user.adapter.FriendsAdapter;
import com.rae.cnblogs.user.friends.FriendsContract;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by rae on 2018/7/23.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public abstract class FriendsFragment extends BasicFragment implements FriendsContract.View {

    @BindView(R2.id.recycler_view)
    RaeRecyclerView mRecyclerView;

    @BindView(R2.id.placeholder)
    PlaceholderView mPlaceholderView;

    @BindView(R2.id.ptr_content)
    AppLayout mAppLayout;

    String mBlogApp;

    private FriendsAdapter mAdapter;

    protected FriendsContract.Presenter mPresenter;

    @Override
    public String getBlogApp() {
        return mBlogApp;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.app_layout_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = onCreatePresenter();
        if (getArguments() != null)
            mBlogApp = getArguments().getString("blogApp");
    }

    protected abstract FriendsContract.Presenter onCreatePresenter();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int padding = getResources().getDimensionPixelOffset(R.dimen.friends_foot_padding);
        mRecyclerView.getFootView().setPadding(0, padding, 0, padding);
        mAdapter = new FriendsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<UserInfoBean>() {
            @Override
            public void onItemClick(Context context, UserInfoBean item) {
                AppRoute.routeToBlogger(getActivity(), item.getBlogApp());
            }
        });
        mAdapter.setOnFollowClickListener(new FriendsAdapter.OnFollowClickListener() {
            @Override
            public void onFollowClick(TextView view, UserInfoBean m) {
                view.setEnabled(false);
                view.setText(R.string.loading);
                if (m.isHasFollow()) {
                    // 取消关注
                    mPresenter.unFollow(m);
                } else {
                    // 加关注
                    mPresenter.follow(m);
                }
            }
        });
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                mPresenter.onLoadMore();
            }
        });


        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                mPresenter.start();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mRecyclerView.isOnTop();
            }
        });

        mPresenter.start();
    }


    @Override
    public void onNoMoreData() {
        mRecyclerView.setNoMore(true);
    }

    @Override
    public void onEmptyData(String msg) {
        mPlaceholderView.empty(msg);
        mAppLayout.refreshComplete();
    }

    @Override
    public void onLoadData(List<UserInfoBean> data) {
        mRecyclerView.setNoMore(false);
        mPlaceholderView.dismiss();
        mAppLayout.refreshComplete();
        mAdapter.setDataList(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoginExpired() {
        UICompat.failed(getContext(), getString(R.string.login_expired));

//        new DefaultDialogFragment.Builder()
//                .message(getString(R.string.login_expired))
//                .confirmText(getString(R.string.go_login))
//                .confirm(new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        AppRoute.routeToLogin(getContext());
//                        getActivity().finish();
//                    }
//                })
//                .build()
//                .show(getChildFragmentManager(), "dialog");
    }

    @Override
    public void onFollowError(String message) {
        new DefaultDialogFragment.Builder().message(message).show(getChildFragmentManager(), "alert");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFollowSuccess() {
        // 刷新状态
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUnFollowSuccess() {
        // 刷新状态
        mAdapter.notifyDataSetChanged();
    }
}
