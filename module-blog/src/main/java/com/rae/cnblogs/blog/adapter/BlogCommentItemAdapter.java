package com.rae.cnblogs.blog.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.holder.BlogCommentViewHolder;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;


/**
 * 评论列表ITEM
 * Created by ChenRui on 2016/12/15 22:48.
 */
public class BlogCommentItemAdapter extends BaseItemAdapter<BlogCommentBean, BlogCommentViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private OnBlogCommentItemClick mOnBlogCommentItemLongClick;
    private View.OnClickListener mOnAuthorClickListener;

    @Override
    public void onClick(View v) {
        BlogCommentBean m = (BlogCommentBean) v.getTag();
        if (mOnBlogCommentItemClick == null || m == null) return;
        mOnBlogCommentItemClick.onItemClick(m);
    }

    @Override
    public boolean onLongClick(View v) {
        BlogCommentBean m = (BlogCommentBean) v.getTag();
        if (mOnBlogCommentItemLongClick == null || m == null) return false;
        mOnBlogCommentItemLongClick.onItemClick(m);
        return true;
    }


    public interface OnBlogCommentItemClick {
        void onItemClick(BlogCommentBean comment);
    }

    private OnBlogCommentItemClick mOnBlogCommentItemClick;

    public void setOnBlogCommentItemClick(OnBlogCommentItemClick onBlogCommentItemClick) {
        mOnBlogCommentItemClick = onBlogCommentItemClick;
    }

    public void setOnAuthorClickListener(View.OnClickListener onAuthorClickListener) {
        mOnAuthorClickListener = onAuthorClickListener;
    }

    public void setOnBlogCommentItemLongClick(OnBlogCommentItemClick onBlogCommentItemClick) {
        mOnBlogCommentItemLongClick = onBlogCommentItemClick;
    }

    @Override
    public BlogCommentViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BlogCommentViewHolder(inflateView(parent, R.layout.item_blog_comment));
    }

    @Override
    public void onBindViewHolder(BlogCommentViewHolder holder, int position, BlogCommentBean m) {
        holder.authorLayout.setTag(m.getBlogApp());
        holder.authorLayout.setOnClickListener(mOnAuthorClickListener);
        holder.authorTitleView.setText(m.getAuthorName());
        holder.dateView.setText(m.getDate());
        holder.contentView.setText(m.getBody());
        holder.itemView.setTag(m);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);

        holder.quoteLayout.setVisibility(TextUtils.isEmpty(m.getQuote()) ? View.GONE : View.VISIBLE);
        holder.quoteBlogAppView.setText(String.format("回复%s：", m.getQuoteBlogApp()));
        holder.quoteContentView.setText(m.getQuote());

        if (!TextUtils.isEmpty(m.getAvatar())) {
            AppImageLoader.displayAvatar(m.getAvatar(), holder.avatarView);
        }
    }
}
