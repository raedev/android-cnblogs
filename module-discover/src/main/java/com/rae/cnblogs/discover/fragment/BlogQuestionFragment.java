package com.rae.cnblogs.discover.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.discover.RaeBaseQuickAdapter;
import com.rae.cnblogs.discover.presenter.BlogQuestionPresenterImpl;
import com.rae.cnblogs.discover.presenter.IBlogQuestionContract;
import com.rae.cnblogs.sdk.bean.BlogQuestionBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.ITopScrollable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class BlogQuestionFragment extends BasicFragment implements ITopScrollable, IBlogQuestionContract.View {

    @BindView(R2.id.refresh_layout)
    AppLayout mAppLayout;

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private BlogQuestionAdapter mAdapter;
    IBlogQuestionContract.Presenter mPresenter;
    private int mType;

    public static BlogQuestionFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        BlogQuestionFragment fragment = new BlogQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_question;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new BlogQuestionPresenterImpl(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new BlogQuestionAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore();
            }
        }, mRecyclerView);
        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.start();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BlogQuestionBean item = (BlogQuestionBean) adapter.getItem(position);
                if (item != null)
                    AppRoute.routeToQuestionDetail(view.getContext(), item.getUrl());
            }
        });
    }

    @Override
    protected void onReceiveArguments(@NonNull Bundle arguments) {
        super.onReceiveArguments(arguments);
        mType = arguments.getInt("type");
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
        mAppLayout.refreshComplete();
        mAdapter.loadMoreComplete();
        mAdapter.showEmpty(msg);
        onNoMoreData();
    }

    @Override
    public void onLoadData(List<BlogQuestionBean> data) {
        mAppLayout.refreshComplete();
        mAdapter.loadMoreComplete();
        mAdapter.replaceData(data);
    }

    @Override
    public void onLoginExpired() {
        // 不处理
    }

    @Override
    public void scrollToTop() {
        if (!mRecyclerView.canScrollVertically(-1)) {
            mAppLayout.autoRefresh();
            return;
        }
        UICompat.scrollToTop(mRecyclerView);
    }

    class BlogQuestionViewHolder extends BaseViewHolder {

        private final LayoutInflater mLayoutInflater;
        @BindView(R2.id.tv_title)
        TextView titleView;
        @BindView(R2.id.tv_sub_num)
        TextView diagNumView;
        @BindView(R2.id.tv_gold)
        TextView goldView;
        @BindView(R2.id.tv_author)
        TextView authorView;
        @BindView(R2.id.tv_read_count)
        TextView readCountView;
        @BindView(R2.id.tv_date)
        TextView dateView;
        @BindView(R2.id.tv_summary)
        TextView summaryView;
        @BindView(R2.id.ll_tags)
        ViewGroup tagsLayout;

        BlogQuestionViewHolder(View view) {
            super(view);
            mLayoutInflater = LayoutInflater.from(itemView.getContext());
            ButterKnife.bind(this, view);
        }

        void setTags(List<String> tags) {
            if (tags == null || tags.size() <= 0) {
                tagsLayout.setVisibility(View.GONE);
                tagsLayout.removeAllViews();
                return;
            }
            tagsLayout.removeAllViews();
            tagsLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < tags.size(); i++) {
                TextView view = (TextView) mLayoutInflater.inflate(R.layout.item_question_tag, (ViewGroup) itemView, false);
                view.setText(tags.get(i));
                tagsLayout.addView(view);
            }
        }
    }

    class BlogQuestionAdapter extends RaeBaseQuickAdapter<BlogQuestionBean, BaseViewHolder> {

        BlogQuestionAdapter(Context context) {
            super(context, R.layout.item_blog_question);
        }


        @Override
        protected void convert(BaseViewHolder helper, BlogQuestionBean item) {
            BlogQuestionViewHolder holder = new BlogQuestionViewHolder(helper.itemView);

            holder.diagNumView.setText(item.getDiggNumber());
            holder.titleView.setText(item.getTitle());
            holder.authorView.setText(item.getAuthor());
            holder.readCountView.setText(item.getReadView() + "次浏览");
            holder.dateView.setText(item.getCreatedAt());
            holder.summaryView.setText(TextUtils.isEmpty(item.getSummary()) ? item.getTitle() : item.getSummary());
            holder.setTags(item.getTags());

            if (TextUtils.isEmpty(item.getGold())) {
                UICompat.setVisibility(holder.goldView, false);
            } else {
                UICompat.setVisibility(holder.goldView, true);
                holder.goldView.setText(item.getGold() + "园豆");
            }
        }
    }
}
