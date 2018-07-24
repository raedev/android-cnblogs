package com.rae.cnblogs.blog.content;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by rae on 2018/6/1.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class BookmarkListPresenterImpl extends BasicPresenter<BookmarkListContract.View> implements BookmarkListContract.Presenter, IPageView<BookmarksBean> {
    // 维护数据源
    private final List<ContentEntity> mDataList = new ArrayList<>();
    private PageObservable<BookmarksBean> mPageObservable;
    private IBookmarksApi mBookmarksApi;

    /**
     */
    public BookmarkListPresenterImpl(BookmarkListContract.View view) {
        super(view);
        mBookmarksApi = CnblogsApiFactory.getInstance(getContext()).getBookmarksApi();
        // 创建分页对象
        mPageObservable = new PageObservable<BookmarksBean>(this, this) {
            /**
             * 需要清除数据源的时候触发清除关联的数据
             */
            @Override
            protected void onClearData() {
                super.onClearData();
                mDataList.clear();
            }

            @Override
            protected Observable<List<BookmarksBean>> onCreateObserver(int page) {
                CategoryBean category = getView().getCategory();
                // 获取博客列表
                if (category == null)
                    return mBookmarksApi.getBookmarks(page);
                return mBookmarksApi.getTagBookmarks(category.getName(), page);
            }
        };

    }

    /**
     * 博客对象转换为内容实体
     *
     * @param data 博客列表
     */
    private List<ContentEntity> convertEntity(List<BookmarksBean> data) {
        for (BookmarksBean b : data) {
            ContentEntity m = new ContentEntity();
            mDataList.add(m);
            m.setId(String.valueOf(b.getWzLinkId()));
            m.setTitle(b.getTitle());
            m.setUrl(b.getLinkUrl());
            m.setSummary(b.getSummary());
            m.setDate(b.getDateAdded());
        }
        return mDataList;
    }

    @Override
    public void loadMore() {
        // 加载更多数据
        mPageObservable.loadMore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataList.clear();
        if (mPageObservable != null) {
            mPageObservable.destroy();
            mPageObservable = null;
        }
    }

    @Override
    public void delete(final ContentEntity item) {
        AndroidObservable.create(mBookmarksApi.delBookmarks(item.getId()))
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onDeleteBookmarksError(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        getView().onDeleteBookmarksSuccess(item);
                    }
                });
    }

    @Override
    public void onEmptyData(String msg) {
        onNoMoreData();
    }

    @Override
    public void onLoadData(List<BookmarksBean> data) {
        // 转换对象
        getView().onLoadData(convertEntity(data));
    }

    @Override
    public void onLoginExpired() {
        getView().onLoginExpired();
    }

    @Override
    public void onNoMoreData() {
        // 查询本地缓存数据
        getView().onNoMoreData();
    }

    @Override
    protected void onStart() {
        mPageObservable.start();
    }
}