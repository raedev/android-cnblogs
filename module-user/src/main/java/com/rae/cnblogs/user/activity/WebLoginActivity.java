package com.rae.cnblogs.user.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.WebActivity;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.fragment.WebLoginFragment;
import com.rae.cnblogs.web.WebViewFragment;

/**
 * 网页版登录
 * Created by ChenRui on 2017/2/3 0003 10:47.
 */
@Route(path = AppRoute.PATH_WEB_LOGIN)
public class WebLoginActivity extends WebActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getShareView().setVisibility(View.GONE);
    }

    @Override
    protected WebViewFragment getWebViewFragment(String url) {
        return WebLoginFragment.newInstance(url);
    }

    @Override
    public void setTitle(CharSequence title) {
        title = "博客园官网登录";
        super.setTitle(title);
    }

    @Override
    @NonNull
    protected String getUrl() {
        return getString(R.string.url_signin);
    }

}
