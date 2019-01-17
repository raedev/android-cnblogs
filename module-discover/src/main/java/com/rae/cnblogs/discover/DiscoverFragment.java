package com.rae.cnblogs.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.antcode.sdk.model.AntAdInfo;
import com.antcode.sdk.model.AntColumnInfo;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.discover.home.DiscoverHomeAdapter;
import com.rae.cnblogs.discover.home.DiscoverHomePresenterImpl;
import com.rae.cnblogs.discover.home.IDiscoverHomeContract;
import com.youth.banner.Banner;

import java.util.List;

import butterknife.BindView;

/**
 * 发现
 * Created by ChenRui on 2018/6/13 10:22.
 */
@Route(path = AppRoute.PATH_FRAGMENT_DISCOVER)
public class DiscoverFragment extends BasicFragment implements IDiscoverHomeContract.View {

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R2.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    IDiscoverHomeContract.Presenter mPresenter;
    private final DiscoverHomeAdapter mAdapter = new DiscoverHomeAdapter();

    @Override
    protected int getLayoutId() {
        return R.layout.fm_discover;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DiscoverHomePresenterImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // init views
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.start();
            }
        });


        mPresenter.start();
    }

    @Override
    public void onLoadAds(List<AntAdInfo> ads) {
        mAdapter.removeAllHeaderView();
        Banner banner = (Banner) getLayoutInflater().inflate(R.layout.view_discover_banner, (ViewGroup) getView(), false);
        banner.setImageLoader(new BannerImageLoader());
        banner.setIndicatorGravity(Gravity.CENTER);
        banner.setImages(ads);
        banner.start();
        mAdapter.addHeaderView(banner);
    }

    @Override
    public void onLoadColumns(List<AntColumnInfo> columns) {

    }
}
