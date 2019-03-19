package com.rae.cnblogs.discover.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.antcode.sdk.model.AntColumnInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
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
    @Nullable
    private AntLoginPlaceHolderFragment mLoginFragment;

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

    @BindView(R2.id.placeholder)
    FrameLayout mPlaceholderLayout;

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
        int itemLayoutId = mType == IAntColumnContract.TYPE_MY ? R.layout.item_discover_mine_column : R.layout.item_discover_home_column;
        mAdapter = new AntColumnAdapter(getContext(), itemLayoutId, mType);
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

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AntColumnInfo item = mAdapter.getItem(position);
                if (item == null) return;
                if (getType() == IAntColumnContract.TYPE_MY) {
                    AppRoute.routeToAntUserColumnDetail(view.getContext(), item.getId());
                } else {
                    AppRoute.routeToAntColumnDetail(view.getContext(), item.getId());
                }
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
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
        dismissLogin();
        mRefreshLayout.setRefreshing(false);
        mAdapter.showEmpty(msg);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void onLoadData(List<AntColumnInfo> data) {
        dismissLogin();
        mRefreshLayout.setRefreshing(false);
        mAdapter.loadMoreComplete();
        mAdapter.replaceData(data);
    }

    private void dismissLogin() {
        UICompat.setVisibility(mPlaceholderLayout, false);
    }

    @Override
    public void onLoginExpired() {
        UICompat.setVisibility(mPlaceholderLayout, true);
        mRefreshLayout.setRefreshing(false);
        mAdapter.dismissLoading();

        // 登录失败，切换Fragment显示
        if (mLoginFragment == null) {
            mLoginFragment = AntLoginPlaceHolderFragment.newInstance();
        }

        if (mLoginFragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.placeholder, mLoginFragment)
                    .commitNow();
        }

    }


    class AntColumnAdapter extends RaeBaseQuickAdapter<AntColumnInfo, BaseViewHolder> {

        private final int mItemType;

        AntColumnAdapter(Context context, int layoutId, int itemType) {
            super(context, layoutId, null);
            this.mItemType = itemType;
        }

        @Override
        protected void initView(Context context) {
            super.initView(context);
            mPlaceholderView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        @Override
        protected void convert(BaseViewHolder helper, AntColumnInfo item) {
            AntColumnHolder holder = new AntColumnHolder(helper, mItemType);
            holder.bindData(item);
        }
    }
}
