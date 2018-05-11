package com.rae.cnblogs.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

public class SkinCardView extends CardView implements SkinCompatSupportable {

    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinCardView(Context context) {
        super(context);
        init(null, 0);
    }

    public SkinCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

    }

    public SkinCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void setBackgroundResource(@DrawableRes int resId) {
        super.setBackgroundResource(resId);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundResource(resId);
        }

    }

    private void init(AttributeSet attrs, int defStyle) {
        this.mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        this.mBackgroundTintHelper.loadFromAttributes(attrs, defStyle);
    }

    @Override
    public void applySkin() {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.applySkin();
        }
    }
}
