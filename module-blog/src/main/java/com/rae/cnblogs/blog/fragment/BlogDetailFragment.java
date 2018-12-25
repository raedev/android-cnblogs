package com.rae.cnblogs.blog.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.R2;
import com.rae.cnblogs.blog.comm.IRefreshable;
import com.rae.cnblogs.blog.detail.BlogDetailPresenterImpl;
import com.rae.cnblogs.blog.detail.ContentDetailContract;
import com.rae.cnblogs.blog.detail.KbDetailPresenterImpl;
import com.rae.cnblogs.blog.detail.NewDetailPresenterImpl;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.dialog.EditCommentDialogFragment;
import com.rae.cnblogs.dialog.ShareDialogFragment;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;
import com.rae.cnblogs.sdk.event.FontChangedEvent;
import com.rae.cnblogs.theme.AppThemeManager;
import com.rae.cnblogs.widget.ImageLoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 博客详情
 * Created by rae on 2018/5/28.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class BlogDetailFragment extends BasicFragment implements ContentDetailContract.View,
        IBlogDetailFragment, IRefreshable,
        EditCommentDialogFragment.OnEditCommentListener,
        ShareDialogFragment.OnShareListener {

//    // 返回按钮
//    @Nullable
//    private ImageView mBackView;
//    // 更多按钮
//    @Nullable
//    private ImageView mMoreView;

    @BindView(R2.id.tv_like_badge)
    TextView mLikeView;
    @BindView(R2.id.img_content_bookmarks)
    ImageLoadingView mBookmarksView;
    @BindView(R2.id.img_content_like)
    ImageLoadingView mLikeAnimView; // 点赞做动画的视图
    @BindView(R2.id.tv_comment_badge)
    TextView mCommentBadgeView;
    @BindView(R2.id.tv_edit_comment)
    View mPostCommentView;
    @BindView(R2.id.layout_content_comment)
    View mViewCommentView;

    // 内容WebView
    private ContentWebViewFragment mContentWebViewFragment;
    // 内容 Id
    private ContentEntity mContentEntity;
    // 业务
    private ContentDetailContract.Presenter mPresenter;
    @Nullable
    private EditCommentDialogFragment mEditCommentDialogFragment;

    @Override
    public String getWebUrl() {
        return mContentEntity.getUrl();
    }

    @Override
    public void onShare(ShareDialogFragment dialog) {
        // 不用处理
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        // 初始化参数属性
        Bundle arguments = getArguments();
        if (arguments != null) {
            mContentEntity = arguments.getParcelable("entity");
        }

        if (mContentEntity == null) {
            return;
        }

        // 根据类型初始化不同的逻辑处理
        BlogType type = BlogType.typeOf(mContentEntity.getType());

        if (type == BlogType.NEWS) {
            mPresenter = new NewDetailPresenterImpl(this);
        } else if (type == BlogType.KB) {
            mPresenter = new KbDetailPresenterImpl(this);
        } else {
            mPresenter = new BlogDetailPresenterImpl(this);
        }

        // 初始化WebView
        mContentWebViewFragment = ContentWebViewFragment.newInstance(getArguments());
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content, mContentWebViewFragment)
                .commitNow();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 评论角标
        if (!TextUtils.equals(mContentEntity.getCommentCount(), "0")) {
            mCommentBadgeView.setText(mContentEntity.getCommentCount());
            mCommentBadgeView.setSelected(true);
            mCommentBadgeView.setVisibility(View.VISIBLE);
        }

        // 点赞角标
        if (!TextUtils.equals(mContentEntity.getLikeCount(), "0")) {
            mLikeView.setText(mContentEntity.getLikeCount());
            mLikeView.setVisibility(View.VISIBLE);
        }

        // 知识库没有评论处理
        BlogType mBlogType = BlogType.typeOf(mContentEntity.getType());
        if (mBlogType == BlogType.KB) {
            mPostCommentView.setVisibility(View.INVISIBLE);
            mViewCommentView.setVisibility(View.INVISIBLE);
        }
    }

    public static BlogDetailFragment newInstance(ContentEntity entity) {
        Bundle data = new Bundle();
        data.putParcelable("entity", entity);
        BlogDetailFragment fragment = new BlogDetailFragment();
        fragment.setArguments(data);
        return fragment;
    }


    @NonNull
    @Override
    public ContentEntity getContentEntity() {
        return mContentEntity;
    }

    @Override
    public void onCollectFailed(String message) {
        mBookmarksView.setEnabled(true);
        mBookmarksView.release();
        UICompat.scaleIn(mBookmarksView);
        UICompat.failed(getContext(), message);
    }

    @Override
    public void onCollectSuccess() {
        mBookmarksView.setEnabled(true);
        mBookmarksView.setSelected(!mBookmarksView.isSelected());
        mBookmarksView.anim();
    }

    @Override
    public void onCommentFailed(String message) {
        if (mEditCommentDialogFragment != null)
            mEditCommentDialogFragment.dismissLoading();
        UICompat.failed(getContext(), message);
    }

    @Override
    public void onCommentSuccess() {
        UICompat.toastInCenter(getContext(), getString(R.string.dialog_tips_post_comment));
        if (mEditCommentDialogFragment != null)
            mEditCommentDialogFragment.dismiss();
    }

    @Override
    public void onLikeError(String msg) {
        mLikeView.setEnabled(true);
        mLikeAnimView.release();
        mLikeView.setVisibility(View.VISIBLE);
        mLikeAnimView.setVisibility(View.GONE);
        UICompat.scaleIn(mLikeView);
        UICompat.failed(getContext(), msg);
    }

    @Override
    public void onLikeSuccess() {
        int likeCount = Rx.parseInt(mContentEntity.getLikeCount());

        // 点赞
        if (!mLikeView.isSelected()) {
            // 点赞数量加1
            likeCount += 1;
            mLikeView.setText(String.valueOf(likeCount));
            mLikeAnimView.anim(new Runnable() {
                @Override
                public void run() {
                    mLikeAnimView.setVisibility(View.GONE);
                    mLikeView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            // 取消点赞
            likeCount = Math.max(0, likeCount);
            mLikeView.setText(likeCount == 0 ? "" : String.valueOf(likeCount));
            mLikeView.setVisibility(View.VISIBLE);
            mLikeAnimView.setVisibility(View.GONE);
        }
        mLikeView.setEnabled(true);
        mLikeView.setSelected(!mLikeView.isSelected());
    }

    @Override
    public void onLoadDataFailed(String message) {
        UICompat.failed(getContext(), message);
        // 通知里面的Fragment处理
        mContentWebViewFragment.onLoadDataFailed(message);
    }

    @Override
    public void onLoadDataSuccess(BlogBean data, String jsonData) {
        // 加载网页
        mContentWebViewFragment.loadWebData(jsonData);
    }

    @Override
    public void onLoadUserBlogInfo(@NonNull UserBlogInfo m) {
        mLikeView.setSelected(m.isLiked());
        mBookmarksView.setSelected(m.isBookmarks());
    }

    @Override
    public void onNeedLogin() {
        mLikeView.setEnabled(true);
        mBookmarksView.setEnabled(true);
        mBookmarksView.release();
        mLikeAnimView.release();
        mLikeAnimView.setVisibility(View.GONE);
        mLikeView.setVisibility(View.VISIBLE);
        AppRoute.routeToLogin(getContext());
    }

    @Override
    public void onNotSupportCollecte() {
        mBookmarksView.setEnabled(true);
        mBookmarksView.release();

        CnblogAppConfig config = CnblogAppConfig.getInstance(getContext());
        if (config.getOnce("NOT_SUPPORT_COLLECTE")) {
            AppRoute.routeToFavorites(getActivity());
            return;
        }

        // 提示一次就好了
        config.setOnce("NOT_SUPPORT_COLLECTE");
        new DefaultDialogFragment.Builder()
                .message(getString(R.string.cancel_bookmarks_title))
                .cancelable(true)
                .confirmText(getString(R.string.go_now))
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AppRoute.routeToFavorites(getActivity());
                    }
                })
                .show(getFragmentManager());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_detail;
    }

    @Override
    public void onPostComment(EditCommentDialogFragment dialog, String content, @Nullable EditCommentDialogFragment.Entry entry, boolean isReference) {
        // 发表评论
        dialog.showLoading();
        mPresenter.onComment(content);
    }

    @Override
    public void onRefresh() {
        mPresenter.start();
    }

    /**
     * 滑动改变按钮样式
     */
    @Override
    public void onScrollChange(int x, int y, int oldX, int oldY) {
//        if (mBackView == null || mMoreView == null || getContext() == null) return;
//        final int transparentId = ContextCompat.getColor(getContext(), android.R.color.transparent);
//        if (y <= 0) {
//            // 显示默认的
//            mBackView.setBackgroundColor(transparentId);
//            mMoreView.setBackgroundColor(transparentId);
//            mBackView.setImageResource(R.drawable.ic_back);
//            mMoreView.setImageResource(R.drawable.ic_action_bar_more);
//        } else {
//            //  显示圆圈的
//            int bgResId = R.drawable.bg_blog_content_back;
//            mBackView.setBackgroundResource(bgResId);
//            mMoreView.setBackgroundResource(bgResId);
//            mBackView.setImageResource(R.drawable.ic_back_white);
//            mMoreView.setImageResource(R.drawable.ic_blog_content_more);
//        }
    }

    /**
     * 点击喜欢
     */
    @OnClick(R2.id.ll_like)
    public void onLikeClick() {
        mLikeView.setEnabled(false);
        mLikeView.setVisibility(View.GONE);
        mLikeAnimView.setVisibility(View.VISIBLE);
        mLikeAnimView.loading();
        mPresenter.onLike(mLikeView.isSelected());
    }

    /**
     * 点击收藏
     */
    @OnClick(R2.id.ll_content_bookmarks)
    public void onBookmarkClick() {
        mBookmarksView.setEnabled(false);
        mBookmarksView.loading();
        mPresenter.onCollect(mBookmarksView.isSelected());
    }

    /**
     * 查看评论
     */
    @OnClick(R2.id.layout_content_comment)
    public void onCommentClick() {
        AppRoute.routeToComment(getContext(), mContentEntity);
    }

    /**
     * 发表评论
     */
    @OnClick(R2.id.tv_edit_comment)
    public void onEditCommentClick() {
        // 检查登录
        if (UserProvider.getInstance().isNotLogin()) {
            AppRoute.routeToLogin(getContext());
            return;
        }
        // 弹出评论
        EditCommentDialogFragment fragment = EditCommentDialogFragment
                .newInstance(EditCommentDialogFragment.FROM_TYPE_BLOG, null);
        mEditCommentDialogFragment = fragment;
        fragment.show(getChildFragmentManager(), "comment");
    }

    @OnClick(R2.id.back)
    public void onBackClick() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            activity.finish();
    }

    /**
     * 点击更多按钮弹出分享
     */
    public void onActionMenuMoreClick() {
        String url = mContentEntity.getUrl();
        String title = mContentEntity.getTitle();
        String desc = mContentEntity.getSummary();
        String imageUrl = mContentEntity.getAvatar();
        ShareDialogFragment.newInstance(url, title, desc, imageUrl).show(getChildFragmentManager(), "share");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppRoute.REQ_CODE_FAVORITES && resultCode == Activity.RESULT_OK) {
            mPresenter.loadBlogLocalStatus(); // 重新加载数据
        }
    }

    @Subscribe
    public void onEvent(FontChangedEvent event) {
        if (mContentWebViewFragment != null) {
            mContentWebViewFragment.onFontSizeChanged();
        }
    }

    @Subscribe
    public void onEvent(AppThemeManager.ThemeEvent event) {
        if (mContentWebViewFragment != null) {
            mContentWebViewFragment.reload();
        }
    }
}
