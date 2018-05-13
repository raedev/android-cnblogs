package com.rae.cnblogs.basic.rx;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import io.reactivex.schedulers.Schedulers;

/**
 * 适用于异步处理跟Android生命周期进行关联
 *
 * @param <T>
 */
public class AndroidObservable<T> extends Observable<T> implements HasUpstreamObservableSource<T> {

    public static <T> AndroidObservable<T> create(Observable<T> observable) {
        return new AndroidObservable<>(observable);
    }

    private final Observable<T> source;
    private LifecycleProvider mLifecycleProvider;

    private AndroidObservable(final Observable<T> value) {
        this.source = value
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public AndroidObservable<T> with(@NonNull LifecycleProvider provider) {
        mLifecycleProvider = provider;
        return this;
    }

    public AndroidObservable<T> with(@NonNull LifecycleObserver observer) {
        mLifecycleProvider = observer.getLifecycleProvider();
        return this;
    }


    @Override
    protected void subscribeActual(Observer<? super T> s) {
        if (mLifecycleProvider != null) {
            mLifecycleProvider.with(source).subscribe(s);
        } else {
            source.subscribe(s);
        }
    }

    @Override
    public ObservableSource<T> source() {
        return source;
    }
}
