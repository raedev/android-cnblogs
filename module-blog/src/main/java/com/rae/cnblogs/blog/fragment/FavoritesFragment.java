package com.rae.cnblogs.blog.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BaseItemAdapter;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.blog.content.BookmarkListContract;
import com.rae.cnblogs.blog.content.BookmarkListPresenterImpl;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 我的收藏
 * Created by rae on 2018/6/1.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class FavoritesFragment extends MultipleTypeBlogListFragment implements BookmarkListContract.View {
    /**
     * @param tag 标签，如果为空则是所有
     */
    public static FavoritesFragment newInstance(String tag) {
        Bundle args = new Bundle();
        if (!TextUtils.equals("全部", tag)) {
            CategoryBean category = new CategoryBean();
            category.setName(tag);
            args.putParcelable("category", category);
        }
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ContentListContract.Presenter makePresenter() {
        return new BookmarkListPresenterImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPlaceholderView.loading();
        getAdapter().setOnItemClickListener(new BaseItemAdapter.onItemClickListener<ContentEntity>() {
            @Override
            public void onItemClick(Context context, ContentEntity item) {
                // 跳转网页
                String url = item.getUrl();
                if (TextUtils.isEmpty(url)) {
                    UICompat.failed(getContext(), "收藏地址为空");
                    return;
                }
                if (url.contains("news.cnblogs.com") || url.contains("kb.cnblogs.com")) {
                    // 新闻\知识库跳转网页
                    AppRoute.routeToWeb(getContext(), url);
                } else if (url.contains("cnblogs")) {
                    // 只有博客园的地址才跳到博文详情
                    AppRoute.newContentDetail(getContext(), url).show(getChildFragmentManager(), "contentDetail");
                } else {
                    // 普通网页
                    AppRoute.routeToWeb(getContext(), url);
                }
            }
        });

        getAdapter().setOnItemLongClickListener(new BaseItemAdapter.onItemLongClickListener<ContentEntity>() {
            @Override
            public void onItemLongClick(Context context, final ContentEntity item) {
                new DefaultDialogFragment.Builder()
                        .message("确定要删除这条收藏吗")
                        .confirmText("删除")
                        .confirm(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                UICompat.loading(getContext(), "正在删除");
                                ((BookmarkListContract.Presenter) getPresenter()).delete(item);
                            }
                        })
                        .show(getChildFragmentManager(), "delete");
            }
        });

    }

    @Override
    public void onDeleteBookmarksError(String message) {
        UICompat.dismiss();
        UICompat.failed(getContext(), message);
    }

    @Override
    public void onDeleteBookmarksSuccess(ContentEntity item) {
        UICompat.dismiss();
        UICompat.success(getContext(), R.string.delete_bookmarks_success);
        getAdapter().remove(item);
        getAdapter().notifyDataSetChanged();
    }
}
