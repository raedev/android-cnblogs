package com.rae.cnblogs.moment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.moment.R;
import com.rae.cnblogs.moment.R2;
import com.rae.cnblogs.moment.adapter.MomentAdapter;
import com.rae.cnblogs.moment.main.IMomentContract;
import com.rae.cnblogs.moment.main.MomentPresenterImpl;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.ITopScrollable;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.cnblogs.widget.ToolbarToastView;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 闪存列表
 * Created by ChenRui on 2017/10/27 0027 10:41.
 */
public class MomentFragment extends BasicFragment implements IMomentContract.View, ITopScrollable {


    /**
     * 实例化
     *
     * @param type 参考：{@link com.rae.cnblogs.sdk.api.IMomentApi#MOMENT_TYPE_ALL}
     */
    public static MomentFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        MomentFragment fragment = new MomentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String mType;

    @BindView(R2.id.recycler_view)
    RaeRecyclerView mRecyclerView;
    @BindView(R2.id.placeholder)
    PlaceholderView mPlaceholderView;
    @BindView(R2.id.ptr_content)
    AppLayout mAppLayout;

    MomentAdapter mAdapter;
    IMomentContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_moment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MomentPresenterImpl(this);
        if (getArguments() != null) {
            mType = getArguments().getString("type", IMomentApi.MOMENT_TYPE_ALL);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        mAdapter.setOnItemClickListener(null);
        mAdapter.setOnBloggerClickListener(null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MomentAdapter();
        mAdapter.setOnBloggerClickListener(new MomentAdapter.OnBloggerClickListener() {
            @Override
            public void onBloggerClick(String blogApp) {
                AppRoute.routeToBlogger(getContext(), blogApp);
            }
        });
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<MomentBean>() {
            @Override
            public void onItemClick(Context context, MomentBean item) {
                if (item != null)
                    AppRoute.routeToMomentDetail(getContext(), item);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                start();
                // 统计闪存
                AppMobclickAgent.onClickEvent(frame.getContext(), "Moment_" + mType);

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
                mPresenter.loadMore();
            }
        });
    }

    /**
     * 开始加载数据
     */
    protected void start() {
        mPresenter.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start();
    }

    @Override
    public void onNoMoreData() {
        mRecyclerView.setNoMore(true);
    }

    @Override
    public void onEmptyData(String msg) {
        mAppLayout.refreshComplete();
        mPlaceholderView.retry(msg);
    }

    @Override
    public void onLoadData(List<MomentBean> data) {
        mRecyclerView.setNoMore(false);
        mPlaceholderView.dismiss();
        mAppLayout.refreshComplete();
        mRecyclerView.loadMoreComplete();
        mAdapter.invalidate(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoginExpired() {
        mAppLayout.refreshComplete();
        mPlaceholderView.showLogin();
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public void onMessageCountChanged(int replyMeCount, int atMeCount) {
        if (replyMeCount > 0) {
            showToast(ToolbarToastView.TYPE_REPLY_ME, replyMeCount + "条回复我的消息");
        } else if (atMeCount > 0) {
            showToast(ToolbarToastView.TYPE_AT_ME, atMeCount + "条提到我的消息");
        } else {
            dismissToast();
        }
    }

    private void dismissToast() {
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment.isAdded() && fragment.isVisible()) {
            MomentHomeFragment momentHomeFragment = (MomentHomeFragment) fragment;
            momentHomeFragment.dismissToast();
        }
    }

    private void showToast(int type, String msg) {
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment.isAdded() && fragment.isVisible()) {
            MomentHomeFragment momentHomeFragment = (MomentHomeFragment) fragment;
            momentHomeFragment.showToast(type, msg);
        }
    }

    /**
     * 滚动到顶部
     */
    @Override
    public void scrollToTop() {
        UICompat.scrollToTop(mRecyclerView);
        if (mRecyclerView != null && mRecyclerView.isOnTop()) {
            mAppLayout.post(new Runnable() {
                @Override
                public void run() {
                    mAppLayout.autoRefresh();
                }
            });
        }
    }

}
