package com.rae.cnblogs.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.middleware.BuildConfig;
import com.rae.cnblogs.middleware.R;
import com.rae.cnblogs.theme.ThemeCompat;
import com.rae.cnblogs.web.client.RaeJavaScriptBridge;
import com.rae.cnblogs.web.client.RaeWebChromeClient;
import com.rae.cnblogs.web.client.RaeWebViewClient;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeWebView;

import java.io.File;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.annotations.Nullable;

/**
 * 网页查看
 * Created by ChenRui on 2016/12/27 23:07.
 */
public class WebViewFragment extends BasicFragment {

    private String mUrl;
    private String mRawUrl;
    private RaeJavaScriptBridge mJavaScriptApi;
    private WebViewClient mRaeWebViewClient;
    private boolean mEnablePullToRefresh = true;
//    private JavaNetCookieJar mJavaNetCookieJar;


    public static WebViewFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    RaeWebView mWebView;
    FrameLayout mContentLayout;
    ProgressBar mProgressBar;
    AppLayout mAppLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_web;
    }


    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentLayout = view.findViewById(R.id.content);
        mProgressBar = view.findViewById(R.id.pb_web_view);
        mAppLayout = view.findViewById(R.id.ptr_content);

        mWebView = new RaeWebView(view.getContext().getApplicationContext());
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentLayout.addView(mWebView);


        mJavaScriptApi = new RaeJavaScriptBridge(getContext());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);


        File cacheDir = getContext().getExternalCacheDir();

        if (cacheDir != null && cacheDir.canRead() && cacheDir.canWrite()) {
            settings.setAppCacheEnabled(true);
            settings.setAppCachePath(cacheDir.getPath());
        }


        mRaeWebViewClient = getWebViewClient();
        mWebView.addJavascriptInterface(getJavascriptApi(), "app");
        mWebView.setWebChromeClient(getWebChromeClient());
        mWebView.setWebViewClient(mRaeWebViewClient);

        if (mWebView != null && mUrl != null) {
            loadUrl(mUrl);
        }

    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public AppLayout getAppLayout() {
        return mAppLayout;
    }

    @Override
    protected void onLoadData() {

        // 夜间模式
        if (ThemeCompat.isNight()) {
            mWebView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_night));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // 下载监听
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                try {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    UICompat.failed(getContext(), "下载文件错误");
                }
            }
        });

        mAppLayout.setEnabled(mEnablePullToRefresh);
        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 下拉刷新禁止使用缓存
                mWebView.reload(); // 刷新WebView
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 是否处于顶部
                return mProgressBar.getVisibility() != View.VISIBLE && !mWebView.canScrollVertically(-1) && super.checkCanDoRefresh(frame, content, header);
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRawUrl = getArguments().getString("url");
            mUrl = mRawUrl;
        }
    }

    public String getRawUrl() {
        return mRawUrl;
    }

    @Override
    public void onDestroy() {
        if (mContentLayout != null) {
            mContentLayout.removeAllViews();
        }
        if (mAppLayout != null) {
            mAppLayout.removeAllViews();
        }
        if (mRaeWebViewClient != null && mRaeWebViewClient instanceof RaeWebViewClient) {
            ((RaeWebViewClient) mRaeWebViewClient).destroy();
        }
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();

        if (activity == null) return;

        // 点击标题返回顶部
        View titleView = activity.findViewById(R.id.tool_bar);
        if (titleView != null) {
            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWebView.scrollTo(0, 0);
                }
            });
        }

        int placeholderId = getResources().getIdentifier("placeholder_web", "id", activity.getPackageName());
        PlaceholderView placeholderView = activity.findViewById(placeholderId);
        if (placeholderView != null && mRaeWebViewClient != null && mRaeWebViewClient instanceof RaeWebViewClient) {
            placeholderView.dismiss();
            placeholderView.setOnRetryClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWebView.reload();
                }
            });
            ((RaeWebViewClient) mRaeWebViewClient).setPlaceHolderView(placeholderView);
        }

    }

    public String getUrl() {
        return mWebView.getUrl();
    }

    /**
     * 获取网页内容
     */
    public String getContent() {
        return mJavaScriptApi.getHtml();
    }

    public WebChromeClient getWebChromeClient() {
        return new RaeWebChromeClient(mProgressBar);
    }

    public WebViewClient getWebViewClient() {
        return new RaeWebViewClient(mProgressBar, mAppLayout);
    }

    public Object getJavascriptApi() {
        return mJavaScriptApi;
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void reload() {
        mWebView.reload();
    }


    public void enablePullToRefresh(boolean enable) {
        if (mAppLayout != null) {
            mAppLayout.setEnabled(enable);
        } else {
            mEnablePullToRefresh = enable;
        }
    }
}
