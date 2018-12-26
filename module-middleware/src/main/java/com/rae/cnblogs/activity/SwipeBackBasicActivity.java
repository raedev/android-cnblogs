package com.rae.cnblogs.activity;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.rae.cnblogs.basic.BasicActivity;

/**
 * 可以滑动返回的Activity
 * Created by ChenRui on 2017/1/4 0004 10:03.
 */
public abstract class SwipeBackBasicActivity extends BasicActivity {
    @Override
    protected boolean canDragBack() {
        return true;
    }

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(this, 100);
    }
}
