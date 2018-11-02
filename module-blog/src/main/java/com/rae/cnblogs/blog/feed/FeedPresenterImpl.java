package com.rae.cnblogs.blog.feed;

import android.content.Context;
import android.support.annotation.NonNull;

import com.rae.cnblogs.basic.rx.LifecycleProvider;
import com.rae.cnblogs.blog.fragment.FeedListFragment;

/**
 * Created by dengmaohua on 2018/11/2 16:26.
 */
public class FeedPresenterImpl implements FeedContract.Presenter {
    public FeedPresenterImpl(Context context, FeedListFragment feedListFragment) {

    }

    @NonNull
    @Override
    public LifecycleProvider getLifecycleProvider() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }
}
