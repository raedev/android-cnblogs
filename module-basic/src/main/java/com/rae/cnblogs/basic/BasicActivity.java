package com.rae.cnblogs.basic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * 基类视图
 * Created by ChenRui on 2016/12/1 21:35.
 */
public abstract class BasicActivity extends AppCompatActivity implements IPresenterView {

    @Nullable
    Toolbar mToolbar;

    TextView mTitleView;

    @Nullable
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setContentView(int layoutResID) {
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

    /**
     * 获取主内容视图
     */
    public View getContentView() {
        return getWindow().getDecorView();
    }

    protected int getHomeAsUpIndicator() {
        return 0;
    }
}
