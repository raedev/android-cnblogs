package com.rae.cnblogs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

/**
 * 登录的
 * Created by ChenRui on 2017/11/10 0010 10:12.
 */
public class LoginPlaceholderView extends PlaceholderView implements Runnable {

    private Button mLoginRetryView;
    private TextView mLoginTipsView;
    private View mRetryLayout;
    // 重试次数
    int mRetryCount = 0;

    public LoginPlaceholderView(Context context) {
        super(context);
    }

    public LoginPlaceholderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginPlaceholderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoginPlaceholderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initView(AttributeSet attrs, int defStyleAttr) {
        super.initView(attrs, defStyleAttr);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_placeholder_login_retry, (ViewGroup) mLoadingView);
        mLoginRetryView = view.findViewById(R.id.btn_placeholder_retry);
        mRetryLayout = view.findViewById(R.id.placeholder_retry_layout);
        mLoginTipsView = view.findViewById(R.id.tv_placeholder_retry_tips);
        mRetryLayout.setVisibility(View.INVISIBLE);

    }

    @Override
    public void setOnRetryClickListener(final OnClickListener listener) {
        mLoginRetryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRetryCount += 1;
                listener.onClick(v);
            }
        });
    }

    public void loadingWithTimer(String msg) {
        loading(msg);
        // 定时器
        removeCallbacks(this);
        // 连接超时
        postDelayed(this, 6000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this);
        mLoginRetryView.setOnClickListener(null);
    }

    public void dismissLoadingRetry() {
        mRetryLayout.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mLoadingTextView.setVisibility(View.VISIBLE);
        mRetryLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        mRetryLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setVisibility(View.GONE);
        removeCallbacks(this);
    }

    @Override
    public void run() {

        // 已经试过一次，还是不行提示重新登录
        if (mRetryCount > 0) {
            mLoginRetryView.setText(R.string.go_login);
            mLoginTipsView.setText(R.string.login_retry_route_tips);
            mLoadingProgressBar.setVisibility(View.GONE);
            mLoadingTextView.setVisibility(View.GONE);
        }

        mRetryLayout.setVisibility(View.VISIBLE);
        mRetryLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
    }

    /**
     * 是否达到去登录的重试次数了
     */
    public boolean isRouteLogin() {
        return mRetryCount > 2;
    }
}
