package com.rae.cnblogs.blog.detail;

import com.rae.cnblogs.ContentEntityConverter;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;

/**
 * 博客路由
 * Created by rae on 2018/6/4.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class BlogRoutePresenterImpl extends BasicPresenter<BlogRouteContract.View> implements BlogRouteContract.Presenter {

    private IBlogApi mBlogApi;

    public BlogRoutePresenterImpl(BlogRouteContract.View view) {
        super(view);
        mBlogApi = CnblogsApiFactory.getInstance(getContext()).getBlogApi();
    }

    @Override
    protected void onStart() {
        String url = getView().getUrl();
        AndroidObservable.create(mBlogApi.getBlog(url))
                .with(this)
                .subscribe(new ApiDefaultObserver<BlogBean>() {
                    @Override
                    protected void onError(String message) {
                        getView().onLoadDataFailed(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NullPointerException) {
                            onError("获取博客信息失败");
                            return;
                        }
                        super.onError(e);
                    }

                    @Override
                    protected void accept(BlogBean blogBean) {
                        // 转换对象
                        blogBean.setBlogType(BlogType.BLOG.getTypeName());
                        ContentEntity entity = ContentEntityConverter.convert(blogBean);
                        getView().onLoadData(entity);
                    }
                });

    }
}
