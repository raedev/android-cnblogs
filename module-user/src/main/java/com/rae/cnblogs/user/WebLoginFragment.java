package com.rae.cnblogs.user;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.user.login.LoginContract;
import com.rae.cnblogs.user.login.LoginPresenterImpl;
import com.rae.cnblogs.web.WebViewFragment;
import com.rae.cnblogs.web.client.RaeWebViewClient;
import com.rae.cnblogs.widget.LoginPlaceholderView;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 网页登录
 * Created by ChenRui on 2017/2/3 0003 12:01.
 */
public class WebLoginFragment extends WebViewFragment implements LoginContract.View {

    public static WebLoginFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        WebLoginFragment fragment = new WebLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LoginPlaceholderView mPlaceholderView;
    private LoginContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LoginPresenterImpl(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onLoadData() {
        super.onLoadData();
        // 不允许下拉刷新
        enablePullToRefresh(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup parent = (ViewGroup) view.getParent();
        mPlaceholderView = new LoginPlaceholderView(view.getContext());
        mPlaceholderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPlaceholderView.dismiss();
        // 重试按钮
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlaceholderView.isRouteLogin()) {
                    mPlaceholderView.dismiss();
                    // 重新加载登录页面
                    loadUrl(getRawUrl());
                    // 统计失败
                    AppMobclickAgent.onClickEvent(v.getContext(), "WEB_RETRY_LOGIN");
                    CrashReport.postCatchedException(new CnblogsApiException("重试登录失败，当前路径：" + getUrl()));
                    return;
                }

                // 统计登录超时
                AppMobclickAgent.onClickEvent(v.getContext(), "WEB_LOGIN_TIMEOUT");
                mPlaceholderView.loadingWithTimer(v.getContext().getString(R.string.login_retry));
                mPlaceholderView.dismissLoadingRetry();
                reload();
            }
        });
        parent.addView(mPlaceholderView);

    }


    @Override
    public WebViewClient getWebViewClient() {
        return new RaeWebViewClient(getProgressBar(), getAppLayout()) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!TextUtils.isEmpty(url) && url.contains("home.cnblogs.com")) {
                    mPlaceholderView.loadingWithTimer(getString(R.string.loading_web_user_info));
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookie = CookieManager.getInstance().getCookie(url);
                // 登录成功
                if (cookie != null && cookie.contains(".CNBlogsCookie")) {
                    // 请求用户信息
                    mPlaceholderView.loadingWithTimer(getString(R.string.loading_blog_app));
                    mPresenter.loadUserInfo();
                }
            }


        };
    }

    @Override
    public void onUserLicense() {
        // 不用处理
    }

    @Override
    public void onRouteToWebLogin() {
        // 不用处理
    }

    @Override
    public void onLoginSuccess(UserInfoBean data) {
        mPlaceholderView.loadingWithTimer(getString(R.string.loading_user_info, data.getDisplayName()));
        if (getActivity() == null) return;
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onLoginFailed(String message) {
        mPlaceholderView.loadingWithTimer(message);
        mPlaceholderView.dismissLoadingRetry();
    }
}
