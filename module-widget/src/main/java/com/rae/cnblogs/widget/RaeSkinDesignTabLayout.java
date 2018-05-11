package com.rae.cnblogs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.RaeTabLayout;
import android.util.AttributeSet;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatSupportable;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;


public class RaeSkinDesignTabLayout extends RaeTabLayout implements SkinCompatSupportable {

    private SkinCompatBackgroundHelper mBackgroundTintHelper;
    private int mTabIndicatorColorResId;
    private int mTabTextColorsResId;
    private int mTabSelectedTextColorResId;
    private int mIndicatorColorResId = INVALID_ID;
    private int mTextSelectColorResId = INVALID_ID;
    private int mTextUnselectColorResId = INVALID_ID;

    public RaeSkinDesignTabLayout(Context context) {
        this(context, null);
    }

    public RaeSkinDesignTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RaeSkinDesignTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs, defStyleAttr);
        this.applySkin();
    }

    private void initViews(AttributeSet attrs, int defStyleAttr) {
        obtainAttributes(getContext(), attrs);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, android.support.design.widget.R.styleable.TabLayout);
        mIndicatorColorResId = a.getResourceId(android.support.design.widget.R.styleable.TabLayout_tabIndicatorColor, INVALID_ID);
        mIndicatorColorResId = SkinCompatHelper.checkResourceId(mIndicatorColorResId);


        mTextSelectColorResId = a.getResourceId(android.support.design.widget.R.styleable.TabLayout_tabSelectedTextColor, INVALID_ID);
        mTextSelectColorResId = SkinCompatHelper.checkResourceId(mTextSelectColorResId);

        mTextUnselectColorResId = a.getResourceId(android.support.design.widget.R.styleable.TabLayout_tabTextColor, INVALID_ID);
        mTextUnselectColorResId = SkinCompatHelper.checkResourceId(mTextUnselectColorResId);

        this.mTabIndicatorColorResId = 0;
        this.mTabTextColorsResId = 0;
        this.mTabSelectedTextColorResId = 0;
        this.mTabIndicatorColorResId = a.getResourceId(android.support.design.widget.R.styleable.TabLayout_tabIndicatorColor, 0);
        int tabTextAppearance = a.getResourceId(android.support.design.widget.R.styleable.TabLayout_tabTextAppearance, android.support.design.widget.R.style.TextAppearance_Design_Tab);
//        TypedArray ta = context.obtainStyledAttributes(tabTextAppearance, R.styleable.SkinTextAppearance);
//
//
//        try {
//            this.mTabTextColorsResId = ta.getResourceId(R.styleable.SkinTextAppearance_android_textColor, 0);
//        } finally {
//            ta.recycle();
//        }

        if (a.hasValue(android.support.design.widget.R.styleable.TabLayout_tabTextColor)) {
            this.mTabTextColorsResId = a.getResourceId(android.support.design.widget.R.styleable.TabLayout_tabTextColor, 0);
        }

        if (a.hasValue(android.support.design.widget.R.styleable.TabLayout_tabSelectedTextColor)) {
            this.mTabSelectedTextColorResId = a.getResourceId(android.support.design.widget.R.styleable.TabLayout_tabSelectedTextColor, 0);
        }

        a.recycle();
        applyTabLayoutResources();
    }

    private void applyTabLayoutResources() {
        SkinCompatResources resources = SkinCompatResources.getInstance();
        if (mIndicatorColorResId != INVALID_ID) {
            setSelectedTabIndicatorColor(resources.getColor(mIndicatorColorResId));
        }
        if (mTextSelectColorResId != INVALID_ID && mTextUnselectColorResId != INVALID_ID) {
            setTabTextColors(resources.getColor(mTextUnselectColorResId), resources.getColor(mTextSelectColorResId));
        }
    }

    @Override
    public void applySkin() {
        applyTabLayoutResources();
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }
}
