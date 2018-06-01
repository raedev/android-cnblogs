package com.rae.cnblogs.basic.rx;

import android.util.Log;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by rae on 2018/5/31.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public abstract class DefaultEmptyObserver<T> extends DisposableObserver<T> {

    @Override
    public void onError(Throwable e) {
        Log.e("rae", "empty observer error!", e);
    }

    @Override
    public void onComplete() {

    }
}
