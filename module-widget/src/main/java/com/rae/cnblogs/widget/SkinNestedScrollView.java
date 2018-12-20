package com.rae.cnblogs.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.FitWindowsLinearLayout;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by rae on 2018/12/14.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SkinNestedScrollView extends NestedScrollView implements SkinCompatSupportable {

    private SkinCompatBackgroundHelper mBackgroundHelper;

    public SkinNestedScrollView(Context context) {
        super(context);
        mBackgroundHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundHelper.loadFromAttributes(null, 0);
    }

    public SkinNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundHelper.loadFromAttributes(attrs, 0);
    }

    @Override
    public void applySkin() {
        mBackgroundHelper.applySkin();
    }
}
