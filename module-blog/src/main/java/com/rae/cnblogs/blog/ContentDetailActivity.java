package com.rae.cnblogs.blog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.blog.fragment.BlogDetailFragment;

import butterknife.OnClick;

/**
 * 内容详情
 * Created by rae on 2018/5/28.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_CONTENT_DETAIL)
public class ContentDetailActivity extends SwipeBackBasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        // 加载内容区域
        ContentEntity entity = getIntent().getParcelableExtra("entity");
        BlogDetailFragment fragment = BlogDetailFragment.newInstance(entity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, "detail")
                .commitNow();
    }


    /**
     * 点击更多按钮弹出分享
     */
    @OnClick(R2.id.img_action_bar_more)
    public void onActionMenuMoreClick() {
        BlogDetailFragment fragment = (BlogDetailFragment) getSupportFragmentManager().findFragmentByTag("detail");
        if (fragment != null) {
            // 传到Fragment处理
            fragment.onActionMenuMoreClick();
        }
    }
}
