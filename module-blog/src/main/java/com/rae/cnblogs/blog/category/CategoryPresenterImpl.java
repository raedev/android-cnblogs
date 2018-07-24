package com.rae.cnblogs.blog.category;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.ICategoryApi;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rae on 2018/6/29.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class CategoryPresenterImpl extends BasicPresenter<CategoryContract.View> implements CategoryContract.Presenter {


    private final ICategoryApi mCategoriesApi;

    public CategoryPresenterImpl(CategoryContract.View view) {
        super(view);
        mCategoriesApi = CnblogsApiFactory.getInstance(getContext()).getCategoriesApi();
    }

    @Override
    protected void onStart() {
        AndroidObservable
                .create(mCategoriesApi.getCategories())
                .with(this)
                .subscribe(new ApiDefaultObserver<List<CategoryBean>>() {
                    @Override
                    protected void onError(String message) {
                        getView().onLoadCategoryFailed(message);
                    }

                    @Override
                    protected void accept(List<CategoryBean> categoryBeans) {
                        // 我的分类
                        List<CategoryBean> myCategories = new ArrayList<>();
                        // 推荐的分类
                        List<CategoryBean> recommendCategories = new ArrayList<>();
                        for (CategoryBean item : categoryBeans) {
                            if (item.isHide())
                                recommendCategories.add(item);
                            else
                                myCategories.add(item);
                        }

                        getView().onLoadCategory(myCategories, recommendCategories);
                    }
                });
    }

    @Override
    public void save(List<CategoryBean> mycategories, List<CategoryBean> recommendcategories) {
        // 异步线程处理
        final ThreadEntity entity = new ThreadEntity();
        entity.mycategories = mycategories;
        entity.recommendcategories = recommendcategories;

        AndroidObservable.create(Observable.just(entity))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new DefaultObserver<ThreadEntity>() {
                    @Override
                    public void onNext(ThreadEntity threadEntity) {
                        threadUpdateCategoies(threadEntity.mycategories, threadEntity.recommendcategories);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        threadUpdateCategoies(mycategories, recommendcategories);

    }

    /**
     * 线程更新分类
     */
    private void threadUpdateCategoies(List<CategoryBean> mycategories, List<CategoryBean> recommendcategories) {
        List<CategoryBean> dbEntities = new ArrayList<>();
        int size = mycategories.size();
        for (int i = 0; i < size; i++) {
            CategoryBean item = mycategories.get(i);
            item.setHide(false);
            item.setIsHide(false);
            item.setOrderNo(i);
            dbEntities.add(item);
        }

        size = recommendcategories.size();
        for (int i = 0; i < size; i++) {
            CategoryBean item = recommendcategories.get(i);
            item.setHide(true);
            item.setIsHide(true);
            item.setOrderNo(i);
            dbEntities.add(item);
        }

        mCategoriesApi.updateCategories(dbEntities);
    }

    private class ThreadEntity {
        List<CategoryBean> mycategories;
        List<CategoryBean> recommendcategories;
    }
}
