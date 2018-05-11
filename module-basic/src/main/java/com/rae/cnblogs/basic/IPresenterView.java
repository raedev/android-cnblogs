package com.rae.cnblogs.basic;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 公共的Presenter接口
 * Created by ChenRui on 2018/4/18.
 */
public interface IPresenterView extends LifecycleOwner {

    @NonNull
    @Override
    Lifecycle getLifecycle();

    @Nullable
    Context getContext();
}
