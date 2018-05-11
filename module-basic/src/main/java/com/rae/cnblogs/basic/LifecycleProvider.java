package com.rae.cnblogs.basic;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 生命周期提供者，用于提供Presenter和View之间的关联
 */
public class LifecycleProvider {

    // 生命周期绑定
    private final com.trello.rxlifecycle2.LifecycleProvider<Lifecycle.Event> mLifecycleProvider;


    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public LifecycleProvider(LifecycleOwner view) {
        this(AndroidLifecycle.createLifecycleProvider(view));
    }

    public LifecycleProvider(com.trello.rxlifecycle2.LifecycleProvider<Lifecycle.Event> mLifecycleProvider) {
        this.mLifecycleProvider = mLifecycleProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * 关联到生命周期里面去
     */
    public <T> Observable<T> with(Observable<T> observable) {
        observable = observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) {
                mCompositeDisposable.add(disposable);
            }
        });
        return observable.compose(mLifecycleProvider.<T>bindToLifecycle());
    }

    /**
     * 释放所有，生命周期会自动释放
     */
    public void dispose() {
        mCompositeDisposable.clear();
    }
}
