package com.rae.cnblogs.web.client;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.rae.cnblogs.UICompat;

import java.lang.ref.WeakReference;

/**
 * Created by ChenRui on 2016/12/27 23:12.
 */
public class RaeWebChromeClient extends WebChromeClient {

    private WeakReference<ProgressBar> mProgressBar;

    public RaeWebChromeClient(ProgressBar progressBar) {
        mProgressBar = new WeakReference<>(progressBar);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        UICompat.toast(view.getContext(), message);
        result.cancel();
        return true;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        showProgress(newProgress);
    }

    private void showProgress(int progress) {
        if (mProgressBar.get() == null) return;
        mProgressBar.get().setProgress(progress);
    }
}
