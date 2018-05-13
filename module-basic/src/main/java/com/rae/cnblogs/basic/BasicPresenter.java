package com.rae.cnblogs.basic;

import android.content.Context;
import android.support.annotation.NonNull;

import com.rae.cnblogs.basic.rx.LifecycleProvider;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * MVP Presenter
 * Created by ChenRui on 2018/4/18.
 */
public abstract class BasicPresenter<V extends IPresenterView> implements IPresenter {

    private final V mView;

    private LifecycleProvider mLifecycleProvider;

    public BasicPresenter(V view) {
        this.mView = checkNotNull(view);
        mLifecycleProvider = new LifecycleProvider(view);
    }

    public V getView() {
        return mView;
    }

    protected Context getContext() {
        return mView.getContext();
    }

    @Override
    public void start() {
        onStart();
    }

    @Override
    public void destroy() {
        mLifecycleProvider.dispose();
        onDestroy();
    }

    /**
     * 释放数据
     */
    protected void onDestroy() {
    }

    /**
     * 加载数据
     */
    protected abstract void onStart();

    /**
     * 检查是否为空，如果为空抛出异常
     */
    private <T> T checkNotNull(T object) {
        if (object == null) {
            throw new NullPointerException("object is null ");
        }
        return object;
    }


    protected <T> Observable<T> createObservable(Observable<T> observable) {
        observable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return mLifecycleProvider.with(observable);
    }

    @NonNull
    @Override
    public LifecycleProvider getLifecycleProvider() {
        return mLifecycleProvider;
    }
}
