package com.rae.cnblogs.moment.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.dialog.EditCommentDialogFragment;
import com.rae.cnblogs.moment.R;
import com.rae.cnblogs.moment.R2;
import com.rae.cnblogs.moment.adapter.MomentAdapter;
import com.rae.cnblogs.moment.adapter.MomentDetailAdapter;
import com.rae.cnblogs.moment.detail.IMomentDetailContract;
import com.rae.cnblogs.moment.detail.MomentDetailPresenterImpl;
import com.rae.cnblogs.moment.holder.MomentHolder;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeLoadMoreView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 闪存详情
 * Created by ChenRui on 2017/11/2 0002 15:35.
 */
public class MomentDetailFragment extends BasicFragment implements IMomentDetailContract.View, EditCommentDialogFragment.OnEditCommentListener {

    @BindView(R2.id.recycler_view)
    RaeRecyclerView mRecyclerView;
    @BindView(R2.id.placeholder)
    PlaceholderView mPlaceholderView;
    @BindView(R2.id.ptr_content)
    AppLayout mAppLayout;
    private MomentBean mData;
    private MomentDetailAdapter mAdapter;
    @Nullable
    EditCommentDialogFragment mEditCommentDialogFragment;
    @Nullable
    private DefaultDialogFragment mDeleteDialogFragment;
    private IMomentDetailContract.Presenter mPresenter;

    // 点击关注
    private final View.OnClickListener mOnFollowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!UserProvider.getInstance().isLogin()) {
                AppRoute.routeToLogin(v.getContext());
                return;
            }
            ((Button) v).setText("请稍后");
            v.setEnabled(false);
            mPresenter.follow();
        }
    };


    public static MomentDetailFragment newInstance(MomentBean data) {
        Bundle args = new Bundle();
        args.putParcelable("data", data);
        MomentDetailFragment fragment = new MomentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_moment_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MomentDetailPresenterImpl(this);

        if (getArguments() != null) {
            mData = getArguments().getParcelable("data");
        }
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mData == null) {
            mPlaceholderView.empty("闪存数据为空");
            mAppLayout.setEnabled(false);
            return;
        }

        mPlaceholderView.dismiss();
        mAdapter = new MomentDetailAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNoMoreText(R.string.no_more_comment);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(true);
        mAdapter.setOnPlaceholderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommentClick();
            }
        });
        mAdapter.setOnBloggerClickListener(new MomentAdapter.OnBloggerClickListener() {
            @Override
            public void onBloggerClick(String blogApp) {
                AppRoute.routeToBlogger(getContext(), blogApp);
            }
        });
        mAdapter.setOnFollowClickListener(mOnFollowClickListener);
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<MomentCommentBean>() {
            @Override
            public void onItemClick(Context context, MomentCommentBean item) {
                if (item == null) {
                    UICompat.failed(getContext(), "数据为空");
                    return;
                }

                // 如果是自己的话就弹出删除
                if (UserProvider.getInstance().isLogin()
                        && UserProvider.getInstance().getLoginUserInfo().getBlogApp().equals(item.getBlogApp())) {
                    showDeleteDialog(true, item.getId());
                } else {
                    // 显示评论
                    EditCommentDialogFragment.Entry entry = new EditCommentDialogFragment.Entry();
                    entry.setAuthorName(item.getAuthorName());
                    entry.setContent(item.getContent());
                    entry.setSource(item);
                    EditCommentDialogFragment fragment = EditCommentDialogFragment.newInstance(EditCommentDialogFragment.FROM_TYPE_MOMENT, entry);
                    mEditCommentDialogFragment = fragment;
                    fragment.show(getChildFragmentManager(), "");
                }
            }

        });
        mAdapter.setMomentDeleteOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除闪存
                showDeleteDialog(false, null);
            }
        });

        // 加载更多样式
        RaeLoadMoreView footView = mRecyclerView.getFootView();
        footView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.dividerColor));
        footView.setPadding(footView.getPaddingLeft(), footView.getPaddingTop() + 20, footView.getPaddingRight(), footView.getPaddingBottom() + 20);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });

        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mData != null) {
                    mPresenter.refresh();
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mRecyclerView.isOnTop();
            }
        });

    }

    /**
     * 显示删除对话框
     *
     * @param isComment 是否为评论，否则为闪存
     */
    private void showDeleteDialog(final boolean isComment, @Nullable final String id) {
        mDeleteDialogFragment = new DefaultDialogFragment
                .Builder()
                .message(isComment ? "是否删除这条评论" : "是否删除这条闪存？")
                .confirmText(getString(R.string.delete))
                .cancelable(true)
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 执行删除
                        UICompat.loading(getContext(), "正在删除");
                        if (isComment) {
                            mPresenter.deleteComment(id);
                        } else {
                            // 删除闪存
                            mPresenter.deleteMoment();
                        }
                        dialog.dismiss();
                    }
                })
                .show(getFragmentManager(), "deleteDialog");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();

        // 点击返回顶部
        if (activity != null) {
            activity.findViewById(R.id.tool_bar_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UICompat.scrollToTop(mRecyclerView);
                }
            });
        }

        start();
    }

    public void start() {
        if (mData != null) {
            mPresenter.start();
        }
    }

    @Override
    public MomentBean getMomentInfo() {
        return mData;
    }

    @Override
    public void onEmptyComment(String message) {
        UICompat.dismiss();
        mRecyclerView.setNoMore(true);
        mAppLayout.refreshComplete();
        mRecyclerView.loadMoreComplete();
        mAdapter.empty(getString(R.string.empty_comment));
    }

    @Override
    public void onLoadComments(List<MomentCommentBean> data, boolean hasMore) {
        UICompat.dismiss();
        mRecyclerView.loadMoreComplete();
        mAppLayout.refreshComplete();
        mAdapter.invalidate(data);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setNoMore(!hasMore);
    }

    @Override
    public void onPostCommentFailed(String message) {
        if (mEditCommentDialogFragment != null)
            mEditCommentDialogFragment.dismissLoading();
        UICompat.failed(getContext(), message);
    }

    @Override
    public void onPostCommentSuccess() {
        if (mEditCommentDialogFragment != null)
            mEditCommentDialogFragment.dismiss();
        UICompat.toastInCenter(getContext(), getString(R.string.tips_comment_success));
        // 重新加载
        mPresenter.refresh();
    }

    @Override
    public String getBlogApp() {
        return mData.getBlogApp();
    }

    @Override
    public void onLoadBloggerInfoFailed(String msg) {
        MomentHolder holder = mAdapter.getMomentHolder();
        if (holder != null && holder.followView != null) {
            holder.followView.setEnabled(true);
            if (UserProvider.getInstance().isLogin())
                holder.followView.setText("信息异常");
        }
    }

    @Override
    public void onLoadBloggerInfo(FriendsInfoBean info) {
        MomentHolder holder = mAdapter.getMomentHolder();
        if (holder != null && holder.followView != null) {
            holder.followView.setEnabled(true);
            holder.followView.setText(info.isFollowed() ? "取消关注" : "加关注");
        }
    }

    @Override
    public void onFollowFailed(String msg) {
        UICompat.failed(getContext(), msg);
        onFollowSuccess();
    }

    @Override
    public void onFollowSuccess() {
        MomentHolder holder = mAdapter.getMomentHolder();
        if (holder != null && holder.followView != null) {
            holder.followView.setEnabled(true);
            holder.followView.setText(mPresenter.isFollowed() ? R.string.cancel_follow : R.string.following);
        }
    }

    /**
     * 取消删除对话框
     */
    private void dismissDeleteDialog() {
        if (mDeleteDialogFragment != null) {
            mDeleteDialogFragment.dismiss();
        }
    }

    @Override
    public void onDeleteCommentFailed(String message) {

        UICompat.dismiss();
        UICompat.failed(getContext(), message);
    }

    @Override
    public void onDeleteMomentFailed(String msg) {
        UICompat.dismiss();
        UICompat.failed(getContext(), msg);
    }

    @Override
    public void onDeleteMomentSuccess() {
        UICompat.dismiss();
        UICompat.success(getContext(), R.string.tips_del_moment_success);
        getActivity().finish();
    }

    @Override
    public void onLoadMoreNotLogin() {
        mRecyclerView.loadMoreComplete();
        new DefaultDialogFragment.Builder()
                .message(getString(R.string.moment_unlogin_hint))
                .confirmText(getString(R.string.go_login))
                .cancelable(true)
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AppRoute.routeToLogin(getContext());
                    }
                })
                .show(getFragmentManager());
    }

    @Override
    public void onNotLogin() {
        onLoadBloggerInfoFailed(getString(R.string.not_login));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    /**
     * 评论
     */
    @OnClick(R2.id.tv_edit_comment)
    public void onCommentClick() {
        //  检查登录
        if (!UserProvider.getInstance().isLogin()) {
            AppRoute.routeToLogin(getContext());
            return;
        }
        EditCommentDialogFragment fragment = EditCommentDialogFragment.newInstance(EditCommentDialogFragment.FROM_TYPE_MOMENT, null);
        fragment.show(getChildFragmentManager(), "editComment");
        mEditCommentDialogFragment = fragment;
    }


    @Override
    public void onPostComment(EditCommentDialogFragment dialog, String content, @Nullable EditCommentDialogFragment.Entry entry, boolean isReference) {
        MomentCommentBean commentBean = entry == null ? null : (MomentCommentBean) entry.getSource();
        String ingId = mData.getId();
        String userId = commentBean == null ? mData.getUserAlias() : commentBean.getUserAlias();
        String commentId = commentBean == null ? "0" : commentBean.getId();
        if (commentBean != null && !TextUtils.isEmpty(commentBean.getUserAlias())) {
            content = String.format("@%s：%s", commentBean.getAuthorName(), content);
        }
        mPresenter.postComment(ingId, userId, commentId, content);
    }
}
