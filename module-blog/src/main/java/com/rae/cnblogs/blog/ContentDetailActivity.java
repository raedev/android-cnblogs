package com.rae.cnblogs.blog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.ContentEntityConverter;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.blog.fragment.BlogDetailFragment;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;

/**
 * 内容详情
 * Created by rae on 2018/5/28.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_CONTENT_DETAIL)
public class ContentDetailActivity extends SwipeBackBasicActivity {

    @BindView(R2.id.pb_loading)
    View mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        // 加载内容区域
        ContentEntity entity = getIntent().getParcelableExtra("entity");
        // 支持路径跳转
        String url = getIntent().getStringExtra("url");
        if (entity == null && !TextUtils.isEmpty(url)) {
            // 获取详情内容
            setTitle(" ");
            loadBlogDetail(url);
        } else {
            initBlogFragment(entity);
        }
    }


    /**
     * 加载博文详情
     */
    private void loadBlogDetail(final String url) {
        IBlogApi blogApi = CnblogsApiFactory.getInstance(this).getBlogApi();
        mProgressBar.setVisibility(View.VISIBLE);
        AndroidObservable.create(blogApi.getBlogDetail(url))
                .with(this)
                .subscribe(new ApiDefaultObserver<BlogBean>() {
                    @Override
                    protected void onError(String message) {
                        mProgressBar.setVisibility(View.GONE);
                        // 加载错误直接加载原文链接
                        AppRoute.routeToWeb(getContext(), url);
                        finish();
                    }

                    @Override
                    protected void accept(BlogBean blogBean) {
                        mProgressBar.setVisibility(View.GONE);
                        ContentEntity entity = ContentEntityConverter.convert(blogBean);
                        initBlogFragment(entity);
                    }
                });
    }

    private void initBlogFragment(ContentEntity entity) {
        BlogDetailFragment fragment = BlogDetailFragment.newInstance(entity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, "detail")
                .commitNow();
    }


//    /**
//     * 点击更多按钮弹出分享
//     */
//    @OnClick(R2.id.img_action_bar_more)
//    public void onActionMenuMoreClick() {
////        BlogDetailFragment fragment = (BlogDetailFragment) getSupportFragmentManager().findFragmentByTag("detail");
////        if (fragment != null) {
////            // 传到Fragment处理
////            fragment.onActionMenuMoreClick();
////        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
