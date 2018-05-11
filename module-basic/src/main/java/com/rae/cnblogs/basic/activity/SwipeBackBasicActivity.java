//package com.rae.cnblogs.basic.activity;
//
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.view.View;
//
//import com.rae.cnblogs.ThemeCompat;
//
//import me.imid.swipebacklayout.lib.SwipeBackLayout;
//import me.imid.swipebacklayout.lib.Utils;
//import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
//import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
//
///**
// * 可以滑动返回的Activity
// * Created by ChenRui on 2017/1/4 0004 10:03.
// */
//public abstract class SwipeBackBasicActivity extends BasicActivity implements SwipeBackActivityBase {
//
//    private SwipeBackActivityHelper mHelper;
//
//    // 是否为滑动返回
//    private boolean mIsSwipeBack;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mHelper = new SwipeBackActivityHelper(this);
//        mHelper.onActivityCreate();
//        // 夜间模式
//        if (ThemeCompat.isNight()) {
//            getSwipeBackLayout().setScrimColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
//        }
//    }
//
//    @Override
//    public void setContentView(int layoutResID) {
//        super.setContentView(layoutResID);
//        showHomeAsUp();
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mHelper.onPostCreate();
//        getSwipeBackLayout().setShadow(new ColorDrawable(Color.TRANSPARENT), SwipeBackLayout.EDGE_ALL); // 不显示阴影
//        getSwipeBackLayout().addSwipeListener(new SwipeBackLayout.SwipeListener() {
//            @Override
//            public void onScrollStateChange(int state, float scrollPercent) {
//                mIsSwipeBack = state == SwipeBackLayout.STATE_SETTLING;
//            }
//
//            @Override
//            public void onEdgeTouch(int edgeFlag) {
//
//            }
//
//            @Override
//            public void onScrollOverThreshold() {
//            }
//        });
//    }
//
//    @Override
//    public View findViewById(int id) {
//        View v = super.findViewById(id);
//        if (v == null && mHelper != null)
//            return mHelper.findViewById(id);
//        return v;
//    }
//
//    @Override
//    public SwipeBackLayout getSwipeBackLayout() {
//        return mHelper.getSwipeBackLayout();
//    }
//
//    @Override
//    public void setSwipeBackEnable(boolean enable) {
//        getSwipeBackLayout().setEnableGesture(enable);
//    }
//
//    @Override
//    public void scrollToFinishActivity() {
//        Utils.convertActivityToTranslucent(this);
//        getSwipeBackLayout().scrollToFinishActivity();
//        mIsSwipeBack = true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        mIsSwipeBack = false;
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        if (mIsSwipeBack) {
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        }
//    }
//}
