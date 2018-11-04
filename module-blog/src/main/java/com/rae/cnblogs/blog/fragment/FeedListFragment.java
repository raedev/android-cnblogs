package com.rae.cnblogs.blog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.R2;
import com.rae.cnblogs.blog.adapter.FeedItemAdapter;
import com.rae.cnblogs.blog.feed.FeedContract;
import com.rae.cnblogs.blog.feed.FeedPresenterImpl;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.swift.Rx;

import java.util.List;

import butterknife.BindView;

/**
 * 作者的动态列表
 * Created by ChenRui on 2017/3/16 16:08.
 */
public class FeedListFragment extends BasicFragment implements FeedContract.View {

    private FeedItemAdapter mAdapter;

    public static FeedListFragment newInstance(String blogApp) {
        Bundle args = new Bundle();
        args.putString("blogApp", blogApp);
        FeedListFragment fragment = new FeedListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FeedContract.Presenter mFeedPresenter;

    private String mBlogApp;


    @BindView(R2.id.content)
    AppLayout mAppLayout;

    @BindView(R2.id.rec_blog_list)
    RaeRecyclerView mRecyclerView;

    @BindView(R2.id.blog_list_placeholder)
    PlaceholderView mPlaceholderView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBlogApp = getArguments().getString("blogApp");
            mFeedPresenter = new FeedPresenterImpl(this);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_list;
    }

    @Override
    protected void onLoadData() {
        mPlaceholderView.dismiss();
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeedPresenter.start();
            }
        });
        mAppLayout.setEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        mAdapter = new FeedItemAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<UserFeedBean>() {
            @Override
            public void onItemClick(Context context, UserFeedBean item) {
                if ("发表评论".equals(item.getAction())) {
                    return;
                }
                AppRoute.routeToWeb(getContext(), item.getUrl());
            }

        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mFeedPresenter.loadMore();
            }
        });
        mFeedPresenter.start();
    }

    @Override
    public String getBlogApp() {
        return mBlogApp;
    }

    @Override
    public void onLoadFeedFailed(String msg) {
        mPlaceholderView.retry(msg);
    }

    @Override
    public void onLoadMoreFeedFailed(String msg) {
        mPlaceholderView.dismiss();
        mRecyclerView.loadMoreComplete();
    }

    @Override
    public void onLoadFeedSuccess(List<UserFeedBean> dataList) {
        mRecyclerView.loadMoreComplete();
        if (Rx.isEmpty(dataList)) {
            mPlaceholderView.empty();
        } else {
            mPlaceholderView.dismiss();
            mAdapter.invalidate(dataList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMoreFinish() {
        mRecyclerView.setNoMore(true);
    }


    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
//        RaeViewCompat.scrollToTop(mRecyclerView);
    }

    @Override
    public void onNoMoreData() {

    }

    @Override
    public void onEmptyData(String msg) {

    }

    @Override
    public void onLoadData(List<BlogCommentBean> data) {

    }

    @Override
    public void onLoginExpired() {

    }
}