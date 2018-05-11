package com.rae.cnblogs.basic.activity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rae.cnblogs.basic.IPresenterView;

/**
 * 基类
 * Created by ChenRui on 2016/12/1 21:35.
 */
public abstract class BasicActivity extends AppCompatActivity implements IPresenterView {
    @Nullable
    @Override
    public Context getContext() {
        return this;
    }
}
