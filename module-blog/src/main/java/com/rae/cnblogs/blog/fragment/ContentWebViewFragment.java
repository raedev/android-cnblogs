package com.rae.cnblogs.blog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.rae.cnblogs.blog.BuildConfig;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.R2;
import com.rae.cnblogs.blog.comm.IRefreshable;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.rae.cnblogs.theme.ThemeCompat;
import com.rae.cnblogs.web.WebViewFragment;
import com.rae.cnblogs.web.client.RaeJavaScriptBridge;
import com.rae.cnblogs.web.client.RaeWebViewClient;
import com.rae.cnblogs.widget.ITopScrollable;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeWebView;

import java.io.InputStream;

import butterknife.BindView;

/**
 * 内容查看WebView
 * Created by rae on 2018/5/29.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class ContentWebViewFragment extends WebViewFragment implements ITopScrollable {

    private int mSourceTextZoom; // 刚进来的字体大小
    private CnblogAppConfig mConfig;

    @BindView(R2.id.placeholder)
    PlaceholderView mPlaceholderView;

    @BindView(R2.id.pb_loading_web)
    ProgressBar mProgressBarWeb;

    // 可刷新的接口
    @Nullable
    private IRefreshable mRefreshable;

    @Nullable
    private IBlogDetailFragment mBlogDetailFragment;

    public static ContentWebViewFragment newInstance(Bundle args) {
        ContentWebViewFragment fragment = new ContentWebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_web_content;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化属性操作
        mConfig = CnblogAppConfig.getInstance(getContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  初始化操作
        mSourceTextZoom = getWebView().getSettings().getTextZoom();
        initWebView();
        initFontSize();

        // 可刷新接口
        if (getParentFragment() instanceof IRefreshable) {
            mRefreshable = (IRefreshable) getParentFragment();
            mRefreshable.onRefresh();
        }

        // 父接口
        if (getParentFragment() instanceof IBlogDetailFragment) {
            mBlogDetailFragment = (IBlogDetailFragment) getParentFragment();
        }
    }

    /**
     * 初始化网页操作
     */
    private void initWebView() {
        if (BuildConfig.DEBUG) {
            // 调试模式，不缓存网页
            getWebView().getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            // 生产模式，按需缓存网页，不允许下拉刷新
            enablePullToRefresh(false);
            getWebView().getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        // 重试按钮
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRefreshable != null) {
                    mRefreshable.onRefresh();
                } else {
                    getWebView().reload();
                }
            }
        });

        //  设置网页发生错误时关联替代图
        WebViewClient webViewClient = getWebViewClient();
        if (webViewClient instanceof RaeWebViewClient) {
            ((RaeWebViewClient) webViewClient).setPlaceHolderView(mPlaceholderView);
        }

        getWebView().setOnScrollChangeListener(new RaeWebView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(int x, int y, int oldX, int oldY) {
                if (mBlogDetailFragment != null)
                    mBlogDetailFragment.onScrollChange(x, y, oldX, oldY);
            }
        });
    }


    /**
     * 初始化网页字体大小
     */
    private void initFontSize() {
        if (mSourceTextZoom <= 0) {
            mSourceTextZoom = getWebView().getSettings().getTextZoom();
        }
        int pageTextSize = mConfig.getPageTextSize();
        if (pageTextSize > 0) {
            // 默认字体大小
            int defaultTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());
            int zoom = mSourceTextZoom * pageTextSize / defaultTextSize;
            getWebView().getSettings().setTextZoom(zoom);
        }
    }


    /**
     * 加载网页内容
     * 当网页加载完毕后，会通过交互接口的方式获取要显示博客信息
     * 通过接口{@link RaeJavaScriptBridge#getBlog()}来返回博客的Json对象
     */
    public void loadWebData(String webData) {

        mPlaceholderView.dismiss();

        try {
            if (isDetached() || getContext() == null || !isAdded() || !isVisible())
                return; // fix bug #638

            // 设置数据
            getJavascriptApi().setBlog(webData);

            InputStream stream = getResources().getAssets().open("view.html");
            byte[] data = new byte[stream.available()];
            int len = stream.read(data);
            stream.close();

            // 避免切换夜间模式闪烁问题
            String content = new String(data).replace("{{theme}}", ThemeCompat.isNight() ? "rae-night.css" : "rae.css");
            //  加载网页
            getWebView().loadDataWithBaseURL("file:///android_asset/view.html", content, "text/html", "UTF-8", null);
        } catch (Throwable e) {
            Log.e("rae", "the web content load failed!", e);
            // 如果加载失败了，就从默认打开
            getWebView().loadUrl("file:///android_asset/view.html");
        }
    }

    /**
     * 加载数据失败
     *
     * @param message 错误信息
     */
    public void onLoadDataFailed(String message) {
        mPlaceholderView.retry(message);
    }

    @Override
    public void scrollToTop() {
        Context context = getContext();
        RaeWebView webView = getWebView();
        if (context == null || webView == null) return;
        webView.scrollTo(0, 0);
    }

    @Override
    public ProgressBar getProgressBar() {
        return mProgressBarWeb;
    }
}
