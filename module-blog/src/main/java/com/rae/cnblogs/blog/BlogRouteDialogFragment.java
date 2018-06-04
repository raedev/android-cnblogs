package com.rae.cnblogs.blog;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.blog.detail.BlogRouteContract;
import com.rae.cnblogs.blog.detail.BlogRoutePresenterImpl;
import com.rae.cnblogs.dialog.BasicDialogFragment;

/**
 * 博客中转跳转页面
 * 用于需要异步请求的博客，提供一个更加友好的页面
 * Created by rae on 2018/6/4.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_BLOG_ROUTE)
public class BlogRouteDialogFragment extends BasicDialogFragment implements BlogRouteContract.View {

    /**
     * 博客链接地址
     */
    private String mUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_blog_route;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BlogRouteContract.Presenter presenter = new BlogRoutePresenterImpl(this);
        if (getArguments() != null)
            mUrl = getArguments().getString("url");
        presenter.start();
    }

    @NonNull
    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void onLoadData(ContentEntity entity) {
        AppRoute.routeToContentDetail(getContext(), entity);
        this.dismiss();
    }

    @Override
    public void onLoadDataFailed(String message) {
        // 失败后，自动跳转网页
        AppRoute.routeToWeb(getContext(), mUrl);
        dismiss();
    }

    @Override
    protected void onLoadWindowAttr(@NonNull Window window) {
        super.onLoadWindowAttr(window);
        window.setDimAmount(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            window.setElevation(0);
        }
    }
}
