package com.rae.cnblogs.widget;

import android.content.Context;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatTextView;

/**
 * 文本
 * Created by ChenRui on 2016/12/1 23:00.
 */
public class RaeTextView extends SkinCompatTextView {
    public RaeTextView(Context context) {
        super(context);
        init();
    }

    public RaeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public RaeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    void init() {
    }
}
