package com.rae.cnblogs.basic;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

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


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 传递到Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null) return;
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
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
}
