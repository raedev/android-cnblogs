package com.rae.cnblogs.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.home.adapter.SystemMessageAdapter;
import com.rae.cnblogs.home.system.SystemMessageContract;
import com.rae.cnblogs.home.system.SystemMessagePresenterImpl;
import com.rae.cnblogs.sdk.bean.SystemMessageBean;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.swift.Rx;

import java.util.List;

import butterknife.BindView;

/**
 * 系统消息
 * Created by ChenRui on 2017/9/5 0005 15:39.
 */
@Route(path = AppRoute.PATH_SYSTEM_MESSAGE)
public class SystemMessageActivity extends SwipeBackBasicActivity implements SystemMessageContract.View {

    @BindView(R2.id.recycler_view)
    RaeRecyclerView mRecyclerView;
    @BindView(R2.id.placeholder)
    PlaceholderView mPlaceholderView;

    SystemMessageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        mAdapter = new SystemMessageAdapter();
        SystemMessageContract.Presenter presenter = new SystemMessagePresenterImpl(this);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<SystemMessageBean>() {
            @Override
            public void onItemClick(SystemMessageBean item) {
                AppRoute.routeToWeb(getContext(), item.getUrl());
            }
        });

        presenter.start();

    }

    @Override
    public void onEmptyData(String msg) {
        mPlaceholderView.empty(msg);
    }

    @Override
    public void onLoadData(List<SystemMessageBean> data) {
        if (Rx.isEmpty(data)) {
            mPlaceholderView.empty();
            return;
        }
        mPlaceholderView.dismiss();
        mAdapter.invalidate(data);
        mAdapter.notifyDataSetChanged();
    }
}
