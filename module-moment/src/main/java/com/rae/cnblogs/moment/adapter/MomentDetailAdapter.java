package com.rae.cnblogs.moment.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.GlideApp;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.moment.R;
import com.rae.cnblogs.moment.holder.MomentCommentHolder;
import com.rae.cnblogs.moment.holder.MomentHolder;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.widget.PlaceholderView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 闪存详情
 * Created by ChenRui on 2017/11/2 0002 15:54.
 */
public class MomentDetailAdapter extends BaseItemAdapter<MomentCommentBean, SimpleViewHolder> {

    private static final int VIEW_TYPE_DETAIL = 10;
    private final MomentBean mMomentBean;
    private boolean mIsEmpty;
    private String mEmptyMessage;

    // 显示类型
    private int mViewTypeCount = 1;
    private WeakReference<ViewGroup> mViewParent;
    private View.OnClickListener mOnPlaceholderClickListener;
    private View.OnClickListener mOnFollowClickListener;
    private MomentAdapter.OnBloggerClickListener mOnBloggerClickListener;
    private MomentHolder mMomentHolder;
    private CharSequence mBlogApp;
    private View.OnClickListener mMomentDeleteOnClickListener;


    public MomentDetailAdapter(MomentBean momentBean) {
        mMomentBean = momentBean;
        initBlogApp();
    }

    private void initBlogApp() {
        if (UserProvider.getInstance().isLogin()) {
            mBlogApp = UserProvider.getInstance().getLoginUserInfo().getBlogApp();
        }
    }

    public void setOnPlaceholderClickListener(View.OnClickListener onPlaceholderClickListener) {
        mOnPlaceholderClickListener = onPlaceholderClickListener;
    }

    public void setOnBloggerClickListener(MomentAdapter.OnBloggerClickListener onBloggerClickListener) {
        mOnBloggerClickListener = onBloggerClickListener;
    }

    public void setOnFollowClickListener(View.OnClickListener onFollowClickListener) {
        mOnFollowClickListener = onFollowClickListener;
    }

    @Nullable
    public MomentHolder getMomentHolder() {
        return mMomentHolder;
    }

    public void setMomentDeleteOnClickListener(View.OnClickListener listener) {
        mMomentDeleteOnClickListener = listener;
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();

        // 没有评论或者初始化的时候
        if (count <= 0) {
            mViewTypeCount = 2;
        } else {
            mViewTypeCount = 1;
        }

        return count + mViewTypeCount;
    }

    @Override
    public MomentCommentBean getDataItem(int position) {
        return super.getDataItem(Math.max(0, position - mViewTypeCount));
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPE_DETAIL;
        } else if (position == 1 && Rx.getCount(getDataList()) <= 0) {
            return VIEW_TYPE_EMPTY;
        }

        return super.getItemViewType(position);
    }

    @Override
    protected void onBindItemClickListener(SimpleViewHolder holder, int position, MomentCommentBean dataItem) {
        if (position > 0) {
            super.onBindItemClickListener(holder, position, dataItem);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {

        if (mViewParent == null || mViewParent.get() == null) {
            mViewParent = new WeakReference<>(parent);
        }
        switch (viewType) {
            // 空视图
            case VIEW_TYPE_EMPTY:
                return new SimpleViewHolder(inflateView(parent, R.layout.item_comment_placeholder));
            // 详情
            case VIEW_TYPE_DETAIL:
                mMomentHolder = new MomentHolder(inflateView(parent, R.layout.item_moment_detail_info));
                return mMomentHolder;
            default:
                return new MomentCommentHolder(inflateView(parent, R.layout.item_moment_comment));
        }

    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position, MomentCommentBean m) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            // 空视图
            case VIEW_TYPE_EMPTY:
                onBindEmptyViewHolder((PlaceholderView) holder.itemView);
                return;
            // 详情
            case VIEW_TYPE_DETAIL:
                onBindDetailInfoViewHolder((MomentHolder) holder, mMomentBean);
                return;
            default:
                onBindCommentViewHolder((MomentCommentHolder) holder, m);
                return;
        }

    }

    /**
     * 评论
     */
    private void onBindCommentViewHolder(MomentCommentHolder holder, MomentCommentBean m) {
        if (m == null) return;
        SpannableString content = new SpannableString(m.getContent());

        if (!TextUtils.isEmpty(m.getAtAuthorName())) {
            content.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary)),
                    0,
                    // fix bug #762
                    Math.min(m.getAtAuthorName().length() + 1, content.length()),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (!TextUtils.isEmpty(m.getAvatar())) {
            AppImageLoader.displayAvatar(m.getAvatar(), holder.avatarView);
        }

        View.OnClickListener onClickListener = TextUtils.isEmpty(m.getBlogApp()) ? null : new MomentAdapter.ItemBloggerClickListener(m.getBlogApp(), mOnBloggerClickListener);
        holder.authorView.setOnClickListener(onClickListener);
        holder.avatarView.setOnClickListener(onClickListener);
        holder.dateView.setOnClickListener(onClickListener);

        int index = getDataList().indexOf(m);
        holder.commentTextView.setText(holder.itemView.getContext().getString(R.string.title_comment, mMomentBean.getCommentCount()));
        holder.titleLayout.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
        holder.dividerView.setVisibility(index == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        holder.authorView.setText(m.getAuthorName());
        holder.dateView.setText(m.getPostTime());
        holder.summaryView.setText(content);
    }

    /**
     * 详情
     */
    private void onBindDetailInfoViewHolder(MomentHolder holder, MomentBean m) {

        holder.followView.setOnClickListener(mOnFollowClickListener);
        holder.followView.setVisibility(TextUtils.equals(m.getBlogApp(), mBlogApp) ? View.GONE : View.VISIBLE);
        holder.mRecyclerView.setVisibility(Rx.isEmpty(m.getImageList()) ? View.GONE : View.VISIBLE);
        holder.deleteView.setOnClickListener(mMomentDeleteOnClickListener);
        holder.deleteView.setVisibility(TextUtils.equals(m.getBlogApp(), mBlogApp) ? View.VISIBLE : View.GONE);

        int imageCount = Rx.getCount(m.getImageList());
        if (imageCount == 1) {
            holder.mRecyclerView.setVisibility(View.INVISIBLE);
            holder.mRecyclerView.removeAllViews();
        } else {
            int spanCount = imageCount == 4 || imageCount == 2 ? 2 : 3;
            holder.mRecyclerView.setVisibility(View.VISIBLE);
            holder.mRecyclerView.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), spanCount));
        }

        holder.thumbView.setVisibility(imageCount == 1 ? View.VISIBLE : View.GONE);
        if (imageCount == 1) {
            String url = m.getImageList().get(0);
            GlideApp.with(holder.thumbView)
                    .load(url)
                    .into(holder.thumbView);
            holder.thumbView.setOnClickListener(new MomentAdapter.ItemImageClickListener(url));
        }


        View.OnClickListener onClickListener = TextUtils.isEmpty(m.getBlogApp()) ? null : new MomentAdapter.ItemBloggerClickListener(m.getBlogApp(), mOnBloggerClickListener);
        holder.authorView.setOnClickListener(onClickListener);
        holder.avatarView.setOnClickListener(onClickListener);
        holder.dateView.setOnClickListener(onClickListener);

        holder.mRecyclerView.setAdapter(new MomentImageAdapter(m.getImageList()));
        AppImageLoader.displayAvatar(m.getAvatar(), holder.avatarView);
        holder.authorView.setText(m.getAuthorName());
        holder.dateView.setText(m.getPostTime());
        holder.summaryView.setText(Html.fromHtml(m.getContent()));
//        holder.commentView.setText(m.getCommentCount());
        holder.androidTagView.setVisibility(m.isAndroidClient() ? View.VISIBLE : View.GONE);
    }

    /**
     * 空视图
     */
    private void onBindEmptyViewHolder(final PlaceholderView view) {
        // 绑定点击事件
        view.setOnClickListener(mOnPlaceholderClickListener);

        // 有评论
        if (getItemCount() > mViewTypeCount) {
            view.dismiss();
        } else if (mIsEmpty) {
            view.empty(mEmptyMessage);
        } else {
            view.loading();
        }

    }

    @Override
    public void invalidate(List<MomentCommentBean> data) {
        mIsEmpty = false;
        initBlogApp();
        super.invalidate(data);
    }

    public void empty(String message) {
        mIsEmpty = true;
        mEmptyMessage = message;
        clear();
        notifyDataSetChanged();
    }

}
