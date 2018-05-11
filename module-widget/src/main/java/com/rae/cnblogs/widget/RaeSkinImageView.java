package com.rae.cnblogs.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
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
        init();
    }

    private void init() {
        // 初始化的时候不用取反
        boolean isNight = ThemeCompat.isNight();

        if (isNight && mNightColor != 0 && getDrawable() != null) {
            getDrawable().setColorFilter(mNightColor, PorterDuff.Mode.SRC_ATOP);
        }
        setAlpha(isNight ? getResources().getInteger(R.integer.imageAlpha_night) / 100.0f : 1f);
    }

    @Override
    public void applySkin() {
        super.applySkin();
        if (mNightColor != 0 && getDrawable() != null) {
            if (isNight()) {
                getDrawable().setColorFilter(mNightColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                getDrawable().clearColorFilter();
            }
        } else {
            setAlpha(isNight() ? getResources().getInteger(R.integer.imageAlpha_night) / 100.0f : 1f);
        }
    }


    public boolean isNight() {
        // 因为是先应用主题之后才会设置主题名称，所以这里取反。
        return !ThemeCompat.isNight();
    }
}
