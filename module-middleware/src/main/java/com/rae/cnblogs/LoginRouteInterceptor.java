package com.rae.cnblogs;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.rae.cnblogs.sdk.UserProvider;

/**
 * 登录路由拦截器
 * Created by rae on 2018/5/14.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Interceptor(priority = 1)
public class LoginRouteInterceptor implements IInterceptor {

    private Context mContext;

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        String path = postcard.getPath();

        // 授权登录的路由，没有登录自动跳转到登录界面
        if (path.contains("auth") && UserProvider.getInstance().isNotLogin()) {
            AppRoute.routeToLogin(mContext);
            callback.onInterrupt(new RuntimeException("登录路由拦截了,路由：" + path));
            return;
        }
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
