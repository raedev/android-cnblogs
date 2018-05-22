package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebView;

import java.lang.reflect.Field;

/**
 * Created by ChenRui on 2017/1/25 0025 9:48.
 */
public class RaeWebView extends WebView {
    private static Field sConfigCallback;

    public interface OnScrollChangeListener {
        void onScrollChange(int x, int y, int oldX, int oldY);
    }

    protected OnScrollChangeListener mOnScrollChangeListener;

    static {
        try {
            sConfigCallback = Class.forName("android.webkit.BrowserFrame")
                    .getDeclaredField("sConfigCallback");
            sConfigCallback.setAccessible(true);
        } catch (Exception e) {
            // ignored
        }

    }

    public RaeWebView(Context context) {
        super(context);
    }

    public RaeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RaeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RaeWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public RaeWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void destroy() {
        mOnScrollChangeListener = null;
        clearHistory();
        setTag(null);
        super.destroy();
        try {
            if (sConfigCallback != null)
                sConfigCallback.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChange(l, t, oldl, oldt);
        }
    }
}
