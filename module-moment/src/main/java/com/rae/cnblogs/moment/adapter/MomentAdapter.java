package com.rae.cnblogs.moment.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.GlideApp;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.holder.ItemLoadingViewHolder;
import com.rae.cnblogs.basic.holder.SimpleViewHolder;
import com.rae.cnblogs.moment.R;
import com.rae.cnblogs.moment.holder.MomentHolder;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 闪存
 * Created by ChenRui on 2017/10/27 0027 10:49.
 */
public class MomentAdapter extends BaseItemAdapter<MomentBean, SimpleViewHolder> implements View.OnClickListener {


    private String blogApp;

    public MomentAdapter() {
        initUserInfo();
        int size = 5;
        List<MomentBean> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MomentBean m = new MomentBean();
            m.setId("loading");
            data.add(m);
        }
        invalidate(data);
    }

    private void initUserInfo() {
        UserInfoBean userInfoBean = UserProvider.getInstance().getLoginUserInfo();
        if (userInfoBean != null) {
            blogApp = userInfoBean.getBlogApp();
        }
    }

    @Override
    public void invalidate(List<MomentBean> data) {
        super.invalidate(data);
        initUserInfo();
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnBloggerClickListener {
        void onBloggerClick(String blogApp);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String ingId, int position);
    }

    private OnBloggerClickListener mOnBloggerClickListener;

    public void setOnBloggerClickListener(OnBloggerClickListener onBloggerClickListener) {
        mOnBloggerClickListener = onBloggerClickListener;
    }


    @Override
    public int getItemViewType(int position) {
        MomentBean dataItem = getDataItem(position);
        if (dataItem != null && "loading".equalsIgnoreCase(dataItem.getId())) {
            return VIEW_TYPE_LOADING;
        }
        return super.getItemViewType(position);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            return new ItemLoadingViewHolder(inflateView(parent, R.layout.item_list_loading));
        }
        return new MomentHolder(inflateView(parent, R.layout.item_moment_list));
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder viewHolder, int position, MomentBean m) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_LOADING) return;

        MomentHolder holder = (MomentHolder) viewHolder;
        int imageCount = Rx.getCount(m.getImageList());
        holder.mRecyclerView.setVisibility(imageCount > 1 ? View.VISIBLE : View.GONE);

        if (imageCount > 1) {
            int spanCount = imageCount == 4 || imageCount == 2 ? 2 : 3;
            holder.mRecyclerView.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), spanCount));
            holder.mRecyclerView.setAdapter(new MomentImageAdapter(m.getImageList()));
        }


        View.OnClickListener onClickListener = TextUtils.isEmpty(m.getBlogApp()) ? null : new ItemBloggerClickListener(m.getBlogApp(), mOnBloggerClickListener);
        holder.authorView.setOnClickListener(onClickListener);
        holder.avatarView.setOnClickListener(onClickListener);
        holder.dateView.setOnClickListener(onClickListener);

        holder.thumbView.setVisibility(imageCount == 1 ? View.VISIBLE : View.GONE);
        if (imageCount == 1) {
            String url = m.getImageList().get(0);
            GlideApp.with(holder.thumbView)
                    .load(url)
                    .into(holder.thumbView);
            holder.thumbView.setOnClickListener(new ItemImageClickListener(url));
        }

        AppImageLoader.displayAvatar(m.getAvatar(), holder.avatarView);
        holder.authorView.setText(m.getAuthorName());
        holder.dateView.setText(m.getPostTime());
        holder.summaryView.setText(Html.fromHtml(m.getContent()));
        holder.androidTagView.setVisibility(m.isAndroidClient() ? View.VISIBLE : View.GONE);
        holder.commentView.setVisibility("0".equals(m.getCommentCount()) ? View.GONE : View.VISIBLE);
        holder.commentView.setText("0".equals(m.getCommentCount()) ? "" : m.getCommentCount() + "条回复");
    }

    static class ItemImageClickListener implements View.OnClickListener {
        private String mUrl;

        public ItemImageClickListener(String url) {
            mUrl = url;
        }

        @Override
        public void onClick(View v) {
            AppRoute.routeToImagePreview(v.getContext(), mUrl);
        }
    }

    private static class ItemDeleteClickListener implements View.OnClickListener {

        private final WeakReference<OnDeleteClickListener> mOnDeleteClickListener;
        private final String mId;
        private final int mPosition;

        public ItemDeleteClickListener(String id, int position, OnDeleteClickListener onDeleteClickListener) {
            mId = id;
            mPosition = position;
            mOnDeleteClickListener = new WeakReference<>(onDeleteClickListener);
        }

        @Override
        public void onClick(View v) {
            if (mOnDeleteClickListener.get() != null) {
                mOnDeleteClickListener.get().onDeleteClick(mId, mPosition);
            }
        }
    }


    public static class ItemBloggerClickListener implements View.OnClickListener {
        private String blogApp;
        private WeakReference<OnBloggerClickListener> mOnBloggerClickListenerWeakReference;

        public ItemBloggerClickListener(String blogApp, OnBloggerClickListener onBloggerClickListener) {
            this.blogApp = blogApp;
            mOnBloggerClickListenerWeakReference = new WeakReference<OnBloggerClickListener>(onBloggerClickListener);
        }

        @Override
        public void onClick(View v) {
            if (mOnBloggerClickListenerWeakReference.get() != null) {
                mOnBloggerClickListenerWeakReference.get().onBloggerClick(blogApp);
            }
        }
    }
}
