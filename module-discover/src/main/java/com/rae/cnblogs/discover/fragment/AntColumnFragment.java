package com.rae.cnblogs.discover.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.antcode.sdk.model.AntColumnInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.discover.RaeBaseQuickAdapter;
import com.rae.cnblogs.discover.holder.AntColumnHolder;
import com.rae.cnblogs.discover.presenter.AntColumnPresenterImpl;
import com.rae.cnblogs.discover.presenter.IAntColumnContract;

import java.util.List;

import butterknife.BindView;

public class AntColumnFragment extends BasicFragment implements IAntColumnContract.View {

    private int mType;

    public static AntColumnFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        AntColumnFragment fragment = new AntColumnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R2.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    AntColumnAdapter mAdapter;

    IAntColumnContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_discover;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AntColumnPresenterImpl(this);
    }

    @Override
    protected void onReceiveArguments(@NonNull Bundle arguments) {
        super.onReceiveArguments(arguments);
        mType = arguments.getInt("type");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new AntColumnAdapter(getContext(), R.layout.item_discover_home_column);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 加载数据
                mPresenter.start();
            }
        });
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore();
            }
        }, mRecyclerView);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public void onNoMoreData() {
        mAdapter.loadMoreEnd();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onEmptyData(String msg) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.showEmpty(msg);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void onLoadData(List<AntColumnInfo> data) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.loadMoreComplete();
        mAdapter.replaceData(data);
    }

    @Override
    public void onLoginExpired() {
        // 显示登录提示
    }


    class AntColumnAdapter extends RaeBaseQuickAdapter<AntColumnInfo, BaseViewHolder> {

        AntColumnAdapter(Context context, int layoutId) {
            super(context, layoutId, null);
        }

        @Override
        protected void convert(BaseViewHolder helper, AntColumnInfo item) {
            AntColumnHolder holder = new AntColumnHolder(helper);
            holder.bindData(item);
        }
    }
}
