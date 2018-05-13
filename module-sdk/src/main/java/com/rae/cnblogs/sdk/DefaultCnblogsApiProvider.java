package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.api.ICategoryApi;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.api.IPostApi;
import com.rae.cnblogs.sdk.api.IRaeServerApi;
import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.api.impl.CategoryApiImpl;

/**
 * 博客园默认接口实现
 * Created by ChenRui on 2017/1/19 20:45.
 */
public class DefaultCnblogsApiProvider extends CnblogsApiProvider {

    public DefaultCnblogsApiProvider(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public int getApiVersion() {
        return 1;
    }

    @Override
    public IBlogApi getBlogApi() {
        return getRetrofit().create(IBlogApi.class);
    }

    @Override
    public ICategoryApi getCategoriesApi() {
        return new CategoryApiImpl(getContext());
    }

    @Override
    public IUserApi getUserApi() {
        return getRetrofit().create(IUserApi.class);
    }


    @Override
    public IBookmarksApi getBookmarksApi() {
        return getRetrofit().create(IBookmarksApi.class);
    }


    @Override
    public INewsApi getNewsApi() {
        return getRetrofit().create(INewsApi.class);
    }

    @Override
    public IFriendsApi getFriendApi() {
        return getRetrofit().create(IFriendsApi.class);
    }

    @Override
    public ISearchApi getSearchApi() {
        return getRetrofit().create(ISearchApi.class);
    }

    @Override
    public IMomentApi getMomentApi() {
        return getRetrofit().create(IMomentApi.class);
    }

    @Override
    public IPostApi getPostApi() {
        return getRetrofit().create(IPostApi.class);
    }

    @Override
    public IRaeServerApi getRaeServerApi() {
        return getRetrofit().create(IRaeServerApi.class);
    }

}
