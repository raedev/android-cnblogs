package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Scroller;

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


    // 弹性滑动
    private Scroller mScroller;

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
        init();
    }

    public RaeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RaeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RaeWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public RaeWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }

    private void init() {
        mScroller = new Scroller(getContext());
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

    private void smoothScroll(int destX, int destY) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;
        mScroller.startScroll(scrollX, 0, deltaX, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }



}
