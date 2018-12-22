package com.rae.cnblogs.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;

import com.rae.cnblogs.theme.ThemeCompat;

import skin.support.widget.SkinCompatTextView;

/**
 * Created by rae on 2018/12/21.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SkinDrawableTextView extends SkinCompatTextView {

    public SkinDrawableTextView(Context context) {
        this(context, null);
    }

    public SkinDrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply();
    }

    @Override
    public void applySkin() {
        super.applySkin();
        apply();
    }

    private void apply() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        if (ThemeCompat.isNight()) {
            // 夜间模式的颜色
            int nightColor = getResources().getColor(R.color.nightIconColor);
            setCompoundDrawableTintList(ColorStateList.valueOf(nightColor));
        } else {
            setCompoundDrawableTintList(null);
        }
    }
}
