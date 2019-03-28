package com.rae.cnblogs.basic;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 基类视图
 * Created by ChenRui on 2016/12/1 21:35.
 */
public abstract class BasicActivity extends RaeUIActivity implements IPresenterView {

    private boolean mDisabeMobclickAgent; // 是否禁止统计


    @NonNull
    @Override
    public Context getContext() {
        return this;
    }

    public void setDisabeMobclickAgent(boolean disabeMobclickAgent) {
        mDisabeMobclickAgent = disabeMobclickAgent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mDisabeMobclickAgent) {
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mDisabeMobclickAgent) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 传递到Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment == null || fragment.isDetached()) continue;
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
