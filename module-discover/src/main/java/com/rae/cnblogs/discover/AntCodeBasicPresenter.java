package com.rae.cnblogs.discover;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.IPresenterView;

public abstract class AntCodeBasicPresenter<V extends IPresenterView> extends BasicPresenter<V> implements LifecycleOwner {

    public AntCodeBasicPresenter(V view) {
        super(view);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return getView().getLifecycle();
    }
}
