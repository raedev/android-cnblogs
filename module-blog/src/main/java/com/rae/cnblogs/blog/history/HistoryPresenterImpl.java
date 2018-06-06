package com.rae.cnblogs.blog.history;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rae on 2018/6/6.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class HistoryPresenterImpl extends BasicPresenter<HistoryContract.View> implements HistoryContract.Presenter {

    private DbBlog mDbBlog;

    private PageObservable<BlogBean> mPageObservable;

    public HistoryPresenterImpl(HistoryContract.View view) {
        super(view);
        mDbBlog = DbFactory.getInstance().getBlog();
        mPageObservable = new PageObservable<BlogBean>(view, this) {
            @Override
            protected Observable<List<BlogBean>> onCreateObserver(int page) {
                return Observable
                        .just(page)
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Function<Integer, ObservableSource<List<BlogBean>>>() {
                            @Override
                            public ObservableSource<List<BlogBean>> apply(Integer p) {
                                return Observable.just(mDbBlog.getRecentHistory(p));
                            }
                        });
            }
        };

    }

    @Override
    protected void onStart() {
        mPageObservable.start();
    }

    @Override
    public void onLoadMore() {
        mPageObservable.loadMore();
    }
}
