package com.rae.cnblogs;

import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.basic.rx.LifecycleProvider;
import com.rae.cnblogs.sdk.ApiDefaultObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public abstract class PageObservable<T> {
    public static int startPageIndex = 1;
    protected int mPage;
    private IPageView<T> mView;
    private final List<T> mDataList = new ArrayList<>();
    private LifecycleProvider mProvider;

    public PageObservable(IPageView<T> view, IPresenter presenter) {
        this(view, presenter.getLifecycleProvider(), startPageIndex);
    }

    public PageObservable(IPageView<T> view, LifecycleProvider provider, int startIndex) {
        this.mPage = startIndex;
        this.mView = view;
        mProvider = provider;
    }

    public void start() {
        this.mPage = startPageIndex;
        this.onLoadData(this.mPage);
    }

    public void loadMore() {
        this.onLoadData(this.mPage);
    }

    public void complete(List<T> dataList) {
        if (canResetData()) {
            onClearData();
        }

        if (this.mPage > startPageIndex && Rx.isEmpty(dataList)) {
            this.onNoMoreData();
        } else {
            this.mDataList.addAll(dataList);
            this.onLoadDataComplete(this.mDataList);
            ++this.mPage;
        }
    }

    protected void onClearData() {
        this.mDataList.clear();
    }

    /**
     * 释放资源
     */
    public void destroy() {
        this.mDataList.clear();
        this.mProvider.dispose();
        this.mView = null;
        this.mProvider = null;
    }

    public boolean canResetData() {
        return this.mPage <= startPageIndex;
    }

    protected void onNoMoreData() {
        this.mView.onNoMoreData();
    }


    protected void onLoadData(int page) {
        AndroidObservable
                .create(onCreateObserver(page))
                .with(mProvider)
                .subscribe(createObserver());
    }

    /**
     * 创建回调监听
     */
    protected ApiDefaultObserver<List<T>> createObserver() {
        return new ApiDefaultObserver<List<T>>() {
            protected void onError(String message) {
                notifyError(message);
            }

            protected void accept(List<T> data) {
                notifyData(data);
            }

            protected void onLoginExpired() {
                notifyLoginExpired();
            }
        };
    }

    /**
     * 通知错误消息
     */
    protected void notifyError(String message) {
        if (mView == null) return;
        if (mPage > PageObservable.startPageIndex) {
            mView.onNoMoreData();
        } else {
            mView.onEmptyData(message);
        }
    }

    protected void notifyData(List<T> data) {
        if (mView == null) return;
        if (Rx.isEmpty(data)) {
            notifyError("暂无数据");
        } else {
            complete(data);
        }
    }

    protected void notifyLoginExpired() {
        if (mView == null) return;
        mView.onLoginExpired();
    }

    protected abstract Observable<List<T>> onCreateObserver(int page);

    protected void onLoadDataComplete(List<T> dataList) {
        this.mView.onLoadData(dataList);
    }

    public int getCurrentPage() {
        return mPage;
    }
}
