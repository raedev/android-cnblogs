package com.rae.cnblogs.moment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.dialog.EditCommentDialogFragment;
import com.rae.cnblogs.moment.R;
import com.rae.cnblogs.moment.R2;
import com.rae.cnblogs.moment.adapter.MomentMessageAdapter;
import com.rae.cnblogs.moment.message.IMomentMessageContract;
import com.rae.cnblogs.moment.message.MomentMessagePresenterImpl;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.ITopScrollable;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 回复我的消息
 * Created by ChenRui on 2017/11/6 0006 14:21.
 */
public class MomentMessageFragment extends BasicFragment implements ITopScrollable, IMomentMessageContract.View, EditCommentDialogFragment.OnEditCommentListener {

    @BindView(R2.id.recycler_view)
    RaeRecyclerView mRecyclerView;
    @BindView(R2.id.placeholder)
    PlaceholderView mPlaceholderView;
    @BindView(R2.id.ptr_content)
    AppLayout mAppLayout;

    MomentMessageAdapter mAdapter;
    IMomentMessageContract.Presenter mPresenter;
    private EditCommentDialogFragment mEditCommentDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_moment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MomentMessagePresenterImpl(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPresenter.destroy();
        mAdapter.setOnItemClickListener(null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MomentMessageAdapter();
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<MomentCommentBean>() {
            @Override
            public void onItemClick(Context context, MomentCommentBean item) {
                // 弹出回复对话框
                EditCommentDialogFragment.Entry<MomentCommentBean> entry = new EditCommentDialogFragment.Entry<>();
                entry.setAuthorName(item.getAuthorName());
                entry.setContent(item.getContent());
                entry.setSource(item);
                mEditCommentDialog = EditCommentDialogFragment.newInstance(EditCommentDialogFragment.FROM_TYPE_MOMENT, entry);
                mEditCommentDialog.show(getChildFragmentManager(), "shareDialog");
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mPlaceholderView.dismiss();
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
    public void onLoadData(List<MomentCommentBean> data) {
        mRecyclerView.setNoMore(false);
        mPlaceholderView.dismiss();
        mAppLayout.refreshComplete();
        mRecyclerView.loadMoreComplete();
        mAdapter.invalidate(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoginExpired() {
        mPlaceholderView.retry(getString(R.string.login_expired));
    }


    @Override
    public void onPostCommentFailed(String message) {
        mEditCommentDialog.dismissLoading();
        UICompat.failed(getContext(), message);
    }

    @Override
    public void onPostCommentSuccess() {
        mEditCommentDialog.dismiss();
        UICompat.toastInCenter(getContext(), getString(R.string.tips_comment_success));
    }

    /**
     * 滚动到顶部
     */
    @Override
    public void scrollToTop() {
        UICompat.scrollToTop(mRecyclerView);
        if (mRecyclerView.isOnTop()) {
            mAppLayout.post(new Runnable() {
                @Override
                public void run() {
                    mAppLayout.autoRefresh();
                }
            });
        }
    }

    @Override
    public void onPostComment(EditCommentDialogFragment dialog, String content, @Nullable EditCommentDialogFragment.Entry entry, boolean isReference) {
        if (entry == null || entry.getSource() == null) return;
        // 发布评论
        mEditCommentDialog.showLoading();
        MomentCommentBean commentBean = (MomentCommentBean) entry.getSource();
        String ingId = commentBean.getIngId();
        String userId = commentBean.getUserAlias();
        String commentId = commentBean.getId();
        content = String.format("@%s：%s", commentBean.getAuthorName(), content);
        mPresenter.postComment(ingId, userId, commentId, content);
    }
}