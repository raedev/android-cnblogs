package com.rae.cnblogs.basic;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.ButterKnife;

public class RaeUIActivity extends QMUIActivity {

    @Nullable
    Toolbar mToolbar;

   protected TextView mTitleView;


    private boolean mIsTranslucentFull = true; // 是否为沉浸式


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        // 状态栏文字颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        // 根据根布局决定是否沉浸式，腾讯这个坑爹货。
        View view = getLayoutInflater().inflate(layoutResID, null);
        if (view.getFitsSystemWindows()) {
            mIsTranslucentFull = false;
        }

        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        int toolbar = getResources().getIdentifier("tool_bar", "id", getPackageName());
        int title = getResources().getIdentifier("title", "id", getPackageName());
        int back = getResources().getIdentifier("back", "id", getPackageName());
        mToolbar = findViewById(toolbar);
        mTitleView = findViewById(title);
        View backView = findViewById(back);
        if (backView != null) {
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            displayHomeAsUp(mToolbar);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        view.setBackgroundColor(Color.RED);
    }

    /**
     * 显示返回键
     */
    private void displayHomeAsUp(@NonNull Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        CharSequence title = getTitle();
        toolbar.setTitle(title);
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
        if (getHomeAsUpIndicator() != 0) {
            toolbar.setNavigationIcon(getHomeAsUpIndicator());
        }
    }

    protected int getHomeAsUpIndicator() {
        return 0;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mTitleView != null)
            mTitleView.setText(title);
        else
            super.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        if (mTitleView != null)
            mTitleView.setText(titleId);
        else
            super.setTitle(titleId);
    }

    @Override
    protected boolean canDragBack() {
        return false;
    }

    @Override
    protected boolean translucentFull() {
        return mIsTranslucentFull;
    }

}
