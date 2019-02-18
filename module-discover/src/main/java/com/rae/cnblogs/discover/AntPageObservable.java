package com.rae.cnblogs.discover;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.rx.LifecycleProvider;
import com.rae.cnblogs.sdk.ApiDefaultObserver;

import java.util.List;

public abstract class AntPageObservable<T> extends PageObservable<T> {

    public AntPageObservable(IPageView view, IPresenter presenter) {
        super(view, presenter);
    }

    public AntPageObservable(IPageView view, LifecycleProvider provider, int startIndex) {
        super(view, provider, startIndex);
    }

    @Override
    protected ApiDefaultObserver<List<T>> createObserver() {
        return new AntSdkDefaultObserver<List<T>>() {
            @Override
            protected void onError(String message) {
                notifyError(message);
            }

            @Override
            protected void accept(List<T> data) {
                notifyData(data);
            }

            @Override
            protected void onLoginExpired() {
                notifyLoginExpired();
            }
        };
    }
}
