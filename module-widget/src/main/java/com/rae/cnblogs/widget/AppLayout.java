package com.rae.cnblogs.widget;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * 下拉刷新
 * Created by ChenRui on 2016/12/2 01:03.
 */
public class AppLayout extends PtrClassicFrameLayout implements SkinCompatSupportable {
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public AppLayout(Context context) {
        super(context);
        initViews(null, 0);
    }

    public AppLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs, 0);
    }

    public AppLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs, defStyle);
    }

    private void initViews(AttributeSet attrs, int defStyleAttr) {
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }
}
