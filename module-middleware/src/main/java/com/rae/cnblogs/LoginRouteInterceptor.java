package com.rae.cnblogs;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * 登录路由拦截器
 * Created by rae on 2018/5/14.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Interceptor(priority = 1)
public class LoginRouteInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
//        String group = postcard.getGroup();
        callback.onContinue(postcard);
//        callback.onInterrupt(new RuntimeException("登录路由拦截了"));
    }

    @Override
    public void init(Context context) {

    }
}
