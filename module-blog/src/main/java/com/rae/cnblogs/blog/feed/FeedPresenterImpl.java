package com.rae.cnblogs.blog.feed;

import android.support.annotation.NonNull;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.basic.rx.LifecycleProvider;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.swift.Rx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengmaohua on 2018/11/2 16:26.
 */
public class FeedPresenterImpl extends BasicPresenter<FeedContract.View> implements FeedContract.Presenter {
    private int mPage = 1;
    private final List<UserFeedBean> mDataList = new ArrayList<>();

    public FeedPresenterImpl(FeedContract.View view) {
        super(view);
    }

    @NonNull
    @Override
    public LifecycleProvider getLifecycleProvider() {
        return null;
    }


    @Override
    protected void onStart() {
        mPage = 1;
        loadData();
    }

    @Override
    public void loadMore() {
        loadData();
    }

    private void loadData() {
        AndroidObservable.create(CnblogsApiFactory.getInstance(getContext()).getFriendApi().getFeeds(mPage, getView().getBlogApp()))
                .with(this)
                .subscribe(new ApiDefaultObserver<List<UserFeedBean>>() {
                    @Override
                    protected void onError(String message) {
                        if (mPage <= 1) {
                            getView().onLoadFeedFailed(message);
                        } else {
                            getView().onLoadMoreFeedFailed(message);
                        }
                    }

                    @Override
                    protected void onEmpty(List<UserFeedBean> userFeedBeans) {
                        if (mPage > 1) {
                            getView().onLoadMoreFinish();
                        }
                    }

                    @Override
                    protected void accept(List<UserFeedBean> data) {
                        if (mPage <= 1) {
                            mDataList.clear();
                        } else {
                            if (Rx.isEmpty(data)) {
                                getView().onLoadMoreFinish();
                                return;
                            }
                        }
                        mDataList.addAll(data);
                        getView().onLoadFeedSuccess(mDataList);
                        mPage++;
                    }
                });


    }
}
