package com.rae.cnblogs.widget;

import android.content.Context;
import android.support.v7.widget.FitWindowsFrameLayout;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by rae on 2018/12/14.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SkinFitWindowsFrameLayout extends FitWindowsFrameLayout implements SkinCompatSupportable {

    private SkinCompatBackgroundHelper mBackgroundHelper;

    public SkinFitWindowsFrameLayout(Context context) {
        super(context);
        mBackgroundHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundHelper.loadFromAttributes(null, 0);
    }

    public SkinFitWindowsFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundHelper.loadFromAttributes(attrs, 0);
    }

    @Override
    public void applySkin() {
        mBackgroundHelper.applySkin();
    }
}
