package com.rae.cnblogs;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

public class AppActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private final List<Activity> mActivityQueue = new LinkedList<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityQueue.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityQueue.remove(activity);
    }

    public void finish() {
        for (Activity activity : mActivityQueue) {
            activity.finish();
        }

        // 移除所有的
        mActivityQueue.clear();
    }
}
