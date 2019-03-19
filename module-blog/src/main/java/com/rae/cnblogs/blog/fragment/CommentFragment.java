package com.rae.cnblogs.blog.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.R2;
import com.rae.cnblogs.blog.adapter.BlogCommentItemAdapter;
import com.rae.cnblogs.blog.comment.CommentContract;
import com.rae.cnblogs.blog.comment.CommentPresenterImpl;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.dialog.EditCommentDialogFragment;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 评论列表
 * Created by rae on 2018/5/30.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class CommentFragment extends BasicFragment implements CommentContract.View, EditCommentDialogFragment.OnEditCommentListener {
    @BindView(R2.id.rec_blog_comment_list)
    RaeRecyclerView mRecyclerView;

    @BindView(R2.id.content_layout)
    AppLayout mAppLayout;

    @BindView(R2.id.placeholder)
    PlaceholderView mPlaceholderView;

    ContentEntity mContentEntity;

    private EditCommentDialogFragment mEditCommentDialogFragment;

    private BlogCommentItemAdapter mAdapter;
    private CommentContract.Presenter mPresenter;

    @Override
    public ContentEntity getContentEntity() {
        return mContentEntity;
    }

    @Override
    public void onCommentFailed(String message) {
        UICompat.failed(getContext(), message);
        if (mEditCommentDialogFragment != null)
            mEditCommentDialogFragment.dismissLoading();
    }

    @Override
    public void onCommentSuccess() {
        UICompat.toastInCenter(getContext(), getString(R.string.dialog_tips_post_comment));
        mAppLayout.autoRefresh(); // 重新刷新数据
        if (mEditCommentDialogFragment != null)
            mEditCommentDialogFragment.dismiss();
    }

    @Override
    public void onDeleteCommentFailed(String message) {
        UICompat.dismiss();
        UICompat.failed(getContext(), message);
    }

    @Override
    public void onDeleteCommentSuccess(BlogCommentBean comment) {
        UICompat.dismiss();
        // 删除成功，从列表移除
        mAdapter.remove(comment);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_comment;
    }

    public static CommentFragment newInstance(ContentEntity entity) {
        Bundle args = new Bundle();
        args.putParcelable("entity", entity);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContentEntity = getArguments().getParcelable("entity");
        }
        mAdapter = new BlogCommentItemAdapter();
        mPresenter = new CommentPresenterImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setAdapter(mAdapter);
        mPlaceholderView.registerAdapterDataObserver(mAdapter);
        mRecyclerView.setNoMoreText(R.string.no_more_comment);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPresenter.onLoadMore();
            }
        });


        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.start();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mRecyclerView.isOnTop();
            }
        });

        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppLayout.autoRefresh();
            }
        });

        initCommentItemClick();
        initCommentItemLongClick();
        initCommentAvatarClick();


    }

    /**
     * 用户信息点击
     */
    private void initCommentAvatarClick() {
        mAdapter.setOnAuthorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() == null) {
                    UICompat.failed(v.getContext(), getString(R.string.tips_comment_empty_blog_app));
                    return;
                }
                AppRoute.routeToBlogger(v.getContext(), v.getTag().toString());
            }
        });

        mPresenter.start();
    }

    /**
     * 长按删除评论
     */
    private void initCommentItemLongClick() {
        mAdapter.setOnBlogCommentItemLongClick(new BlogCommentItemAdapter.OnBlogCommentItemClick() {
            @Override
            public void onItemClick(BlogCommentBean comment) {
                // 判断当前评论是否属于自己的
                UserInfoBean userInfo = UserProvider.getInstance().getLoginUserInfo();
                if (userInfo != null && userInfo.getDisplayName().equalsIgnoreCase(comment.getAuthorName().trim())) {
                    // 自己发表的评论点击删除
                    showDeleteDialog(comment);
                    return;
                }

                // 弹出评论
                showCommentDialog(comment);

            }
        });
    }

    /**
     * 删除评论
     *
     * @param comment 评论
     */
    private void showDeleteDialog(final BlogCommentBean comment) {
        new DefaultDialogFragment.Builder()
                .cancelable(true)
                .message(getString(R.string.tips_delete_comment))
                .confirmText(getString(R.string.delete_comment))
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UICompat.loading(getContext(), getString(R.string.deleting));
                        mPresenter.onDeleteComment(comment);
                    }
                })
                .show(getChildFragmentManager());
    }

    /**
     * 博客点击显示评论弹窗
     */
    private void initCommentItemClick() {
        mAdapter.setOnBlogCommentItemClick(new BlogCommentItemAdapter.OnBlogCommentItemClick() {
            @Override
            public void onItemClick(BlogCommentBean comment) {
                // 显示评论对话框
                showCommentDialog(comment);
            }
        });
    }

    @Override
    public void onPostComment(EditCommentDialogFragment dialog, String content, @Nullable EditCommentDialogFragment.Entry entry, boolean isReference) {
        // 提交评论
        dialog.showLoading();
        BlogCommentBean entity = entry == null ? null : (BlogCommentBean) entry.getSource();
        mPresenter.onPostComment(content, entity, isReference);
    }

    /**
     * 显示评论对话框
     *
     * @param comment 引用的评论，可为空
     */
    private void showCommentDialog(@Nullable BlogCommentBean comment) {
        try {
            EditCommentDialogFragment.Entry<BlogCommentBean> entry = null;
            if (comment != null) {
                entry = new EditCommentDialogFragment.Entry<>();
                entry.setAuthorName(comment.getAuthorName());
                entry.setContent(comment.getBody());
                entry.setSource(comment);
            }

            mEditCommentDialogFragment = EditCommentDialogFragment.newInstance(EditCommentDialogFragment.FROM_TYPE_BLOG, entry);
            mEditCommentDialogFragment.show(getChildFragmentManager(), "EditCommentDialogFragment");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onNoMoreData() {
        mAppLayout.refreshComplete();
        mRecyclerView.loadMoreComplete();
        mRecyclerView.setNoMore(true);
    }

    @Override
    public void onEmptyData(String msg) {
        mAppLayout.refreshComplete();
        mPlaceholderView.empty();
    }

    @Override
    public void onLoadData(List<BlogCommentBean> data) {
        mAppLayout.refreshComplete();
        mRecyclerView.setNoMore(false);
        mPlaceholderView.dismiss();
        mAdapter.setDataList(data);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreComplete();
    }

    @Override
    public void onLoginExpired() {
        AppRoute.routeToLogin(getContext());
    }

    /**
     * 点击评论
     */
    @OnClick(R2.id.tv_edit_comment)
    public void onCommentClick() {
        // 检查登录
        if (UserProvider.getInstance().isNotLogin()) {
            AppRoute.routeToLogin(getContext());
            return;
        }
        showCommentDialog(null);
    }
}
