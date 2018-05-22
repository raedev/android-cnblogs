package com.rae.cnblogs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * can fixed viewpager
 * Created by ChenRui on 2017/2/2 00:07.
 */
public class RaeViewPager extends ViewPager {

    public static final int MODE_SCROLLABLE = 0;
    public static final int MODE_FIXED = 1;

    private int mMode = MODE_SCROLLABLE;
    private int mLastItem;

    public RaeViewPager(Context context) {
        super(context);
    }

    public RaeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RaeViewPager);

        int count = a.getIndexCount();

        for (int i = 0; i < count; i++) {
            int index = a.getIndex(i);

            if (index == R.styleable.RaeViewPager_pageMode) {
                mMode = a.getInt(index, 0);
            }
        }

        a.recycle();


    }

    private boolean isFixed() {
        return mMode == MODE_FIXED;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isFixed()) return false;
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isFixed()) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item) {

        if (isFixed()) {
            super.setCurrentItem(item, false);
        } else {
            super.setCurrentItem(item);
        }

        mLastItem = item;
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
    }
}
