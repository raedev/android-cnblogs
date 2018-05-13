package com.rae.cnblogs.basic.rx;

import android.support.annotation.NonNull;

public interface LifecycleObserver {

    @NonNull
    LifecycleProvider getLifecycleProvider();
}
