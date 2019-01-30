package com.rae.cnblogs.discover.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.discover.R;

/**
 * 新闻
 */
@Route(path = AppRoute.PATH_DISCOVER_NEWS)
public class NewsActivity extends SwipeBackBasicActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, AppRoute.newNewsFragment())
                .commitNow();
    }
}
