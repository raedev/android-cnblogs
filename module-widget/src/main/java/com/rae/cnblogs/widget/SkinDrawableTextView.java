package com.rae.cnblogs.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.rae.cnblogs.theme.ThemeCompat;

import skin.support.widget.SkinCompatTextView;

/**
 * Created by rae on 2018/12/21.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SkinDrawableTextView extends SkinCompatTextView {
    public SkinDrawableTextView(Context context) {
        super(context);
    }

    public SkinDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SkinDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH && left != null) {
            Drawable[] drawables = new Drawable[]{left, top, right, bottom};
            Log.i("rae", "是否为夜间模式：" + ThemeCompat.isNight());
            for (Drawable drawable : drawables) {
                if (drawable == null) continue;
                if (ThemeCompat.isNight()) {
                    drawable.setTint(Color.RED);
                } else {
                    drawable.clearColorFilter();
                }
            }
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }
}
