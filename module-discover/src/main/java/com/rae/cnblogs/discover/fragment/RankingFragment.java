package com.rae.cnblogs.discover.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.discover.RaeBaseQuickAdapter;
import com.rae.cnblogs.discover.presenter.IRankingContract;
import com.rae.cnblogs.discover.presenter.RankingPresenterImpl;
import com.rae.cnblogs.sdk.bean.HotSearchBean;
import com.rae.cnblogs.widget.ITopScrollable;

import java.util.List;

import butterknife.BindView;

public class RankingFragment extends BasicFragment implements IRankingContract.View, BaseQuickAdapter.OnItemClickListener, ITopScrollable {


    /**
     * @param type {@link com.rae.cnblogs.discover.presenter.IRankingContract#TYPE_HOT_SEARCH}
     */
    public static RankingFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        RankingFragment fragment = new RankingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    private RankingAdapter mAdapter;
    private int mType;
    private IRankingContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_ranking;
    }

    @Override
    protected void onReceiveArguments(@NonNull Bundle arguments) {
        super.onReceiveArguments(arguments);
        mType = arguments.getInt("type");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new RankingPresenterImpl(this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化View
        mAdapter = new RankingAdapter(view.getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation();
        mAdapter.isFirstOnly(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore();
            }
        }, mRecyclerView);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setShowHotIcon(mType != IRankingContract.TYPE_TOP_AUTHOR);
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
    }

    @Override
    public void onEmptyData(String msg) {
        mAdapter.setNewData(null);
        onNoMoreData();
    }

    @Override
    public void onLoadData(List<HotSearchBean> data) {
        mAdapter.replaceData(data);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void onLoginExpired() {
        // 不用实现
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        // ITEM 点击事件
        int type = mType;
        HotSearchBean item = mAdapter.getItem(position);
        if (item == null) return;

        // 热搜点击
        if (type == IRankingContract.TYPE_HOT_SEARCH) {
            AppRoute.routeToSearch(getContext(), 0, item.getName());
        }
        // 大神点击
        else if (type == IRankingContract.TYPE_TOP_AUTHOR) {
            AppRoute.routeToBlogger(getContext(), item.getId());
        } else {
            // 调整博客详情
            AppRoute.routeToContentDetail(getContext(), item.getId());
        }


    }

    @Override
    public void scrollToTop() {
        UICompat.scrollToTop(mRecyclerView);
    }

    private class RankingAdapter extends RaeBaseQuickAdapter<HotSearchBean, BaseViewHolder> {

        private boolean showHotIcon = true;

        RankingAdapter(Context context) {
            super(context, R.layout.item_ranking, null);
        }

        public void setShowHotIcon(boolean showHotIcon) {
            this.showHotIcon = showHotIcon;
        }

        @Override
        protected void convert(BaseViewHolder helper, HotSearchBean item) {
            int position = helper.getLayoutPosition();
            TextView positionView = helper.getView(R.id.tv_position);
            TextView titleView = helper.getView(R.id.tv_title);
            TextView rankingView = helper.getView(R.id.tv_sub_num);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
            params.topMargin = position == 0 ? QMUIDisplayHelper.dp2px(helper.itemView.getContext(), 12) : 0;
            positionView.setSelected(position < 3);
            UICompat.setVisibility(rankingView, showHotIcon);

            positionView.setText(String.valueOf(position + 1));
            titleView.setText(item.getName());
            rankingView.setText(item.getRanking());
        }
    }
}
