package com.rae.cnblogs.home.search;

import android.text.TextUtils;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.db.DbSearch;
import com.rae.cnblogs.sdk.db.model.DbSearchInfo;
import com.rae.cnblogs.sdk.event.SearchEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class HotSearchPresenterImpl extends BasicPresenter<HotSearchContract.View> implements HotSearchContract.Presenter {

    private final ISearchApi mSearchApi;

    private DbSearch mDbSearch;

    public HotSearchPresenterImpl(HotSearchContract.View view) {
        super(view);
        mSearchApi = CnblogsApiFactory.getInstance(getContext()).getSearchApi();
        mDbSearch = DbFactory.getInstance().getSearch();
        EventBus.getDefault().register(this);
    }

    @Override
    public void clearSearchHistory() {
        mDbSearch.clearSearchHistory();
        loadHistory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        loadHotSearch();
        loadHistory();
    }

    /**
     * 加载历史记录
     */
    private void loadHistory() {
        AndroidObservable.create(Observable.just(mDbSearch))
                .with(this)
                .map(new Function<DbSearch, List<String>>() {
                    @Override
                    public List<String> apply(DbSearch dbSearch) {
                        List<DbSearchInfo> list = dbSearch.getSearchHistory();
                        List<String> data = new ArrayList<>();
                        for (DbSearchInfo dbSearchInfo : list) {
                            data.add(dbSearchInfo.getKeyword());
                        }
                        return data;
                    }
                })
                .subscribe(new ApiDefaultObserver<List<String>>() {
                    @Override
                    protected void onError(String message) {
                        getView().onEmptySearchHistoryData();
                    }

                    @Override
                    protected void accept(List<String> data) {
                        if (Rx.getCount(data) <= 0) {
                            getView().onEmptySearchHistoryData();
                            return;
                        }
                        getView().onLoadSearchHistoryData(data);
                    }
                });
    }

    /**
     * 加载热搜
     */
    private void loadHotSearch() {
        // 从缓存中获取
        List<DbSearchInfo> hotSearch = mDbSearch.getHotSearch();
        if (Rx.getCount(hotSearch) > 0) {
            List<String> data = new ArrayList<>();
            for (DbSearchInfo search : hotSearch) {
                data.add(search.getKeyword());
            }
            getView().onLoadHotSearchData(data);
        }


        // 从网络获取
        AndroidObservable.create(mSearchApi.hotSearch())
                .with(this)
                .subscribe(new ApiDefaultObserver<List<String>>() {
                    @Override
                    protected void onError(String message) {
                        getView().onEmptyHotSearchData();
                    }

                    @Override
                    protected void accept(List<String> data) {
                        if (Rx.getCount(data) > 10) {
                            data = data.subList(0, 10);
                        }
                        getView().onLoadHotSearchData(data);

                        // 缓存记录
                        cacheData(data);
                    }

                    private void cacheData(List<String> data) {
                        Observable.just(data)
                                .subscribeOn(Schedulers.io())
                                .subscribe(new DefaultObserver<List<String>>() {

                                    @Override
                                    public void onNext(List<String> strings) {
                                        // 先清除所有缓存记录
                                        mDbSearch.clearHotSearchCache();
                                        // 添加到缓存中
                                        mDbSearch.cacheHotSearch(strings);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                });
    }

//    @Override
//    public void saveHistory(String keyword) {
//        // 保存历史记录
//        Observable.just(keyword)
//                .subscribeOn(Schedulers.io())
//                .subscribe(new DefaultObserver<String>() {
//                    @Override
//                    public void onNext(String s) {
//                        // 清除相同的记录
//                        mDbSearch.deleteSearchHistory(s);
//                        // 添加到历史中
//                        mDbSearch.addSearchHistory(s);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    @Subscribe
    public void onEvent(SearchEvent event) {
        if (TextUtils.isEmpty(event.getSearchText())) return;
        // 保存搜索记录
//        saveHistory(event.getSearchText());
        loadHistory();
    }
}
