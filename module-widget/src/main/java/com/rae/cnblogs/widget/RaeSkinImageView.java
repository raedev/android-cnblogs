package com.rae.cnblogs.widget;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;

import com.rae.cnblogs.theme.ThemeCompat;

import skin.support.widget.SkinCompatImageView;

public class RaeSkinImageView extends SkinCompatImageView {

    private int mNightColor;

    public RaeSkinImageView(Context context) {
        this(context, null);
    }

    public RaeSkinImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RaeSkinImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RaeSkinImageView);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int index = a.getIndex(i);
            if (index == R.styleable.RaeSkinImageView_nightColor) {
                mNightColor = a.getColor(index, 0);
            }
        }
        a.recycle();
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
            setImageTintMode(PorterDuff.Mode.MULTIPLY);
            // 夜间模式的颜色
            if (mNightColor != 0) {
                setImageTintList(ColorStateList.valueOf(mNightColor));
            } else {
                setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.nightMaskColor)));
            }
        } else {
            setImageTintList(null);
        }
    }
}
