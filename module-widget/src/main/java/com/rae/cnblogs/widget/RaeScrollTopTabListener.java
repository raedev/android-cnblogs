package com.rae.cnblogs.widget;

import android.support.design.widget.RaeTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.lang.ref.WeakReference;

/**
 * tabLayout 点击TAB返回顶部
 * Fragment 内部需要实现{@link ITopScrollable} 接口自定义实现返回顶部逻辑
 */
public class RaeScrollTopTabListener implements RaeTabLayout.OnTabSelectedListener {

    private final WeakReference<ViewPager> mViewPagerWeakReference;
    private final FragmentManager mFragmentManager;

    public RaeScrollTopTabListener(ViewPager viewPager, FragmentManager fragmentManager) {
        mViewPagerWeakReference = new WeakReference<>(viewPager);
        mFragmentManager = fragmentManager;
    }

    @Override
    public void onTabSelected(RaeTabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(RaeTabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(RaeTabLayout.Tab tab) {
        ViewPager viewPager = mViewPagerWeakReference.get();
        if (viewPager == null) return;
        int position = viewPager.getCurrentItem();
        Fragment fragment = mFragmentManager.findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + position);
        if (fragment instanceof ITopScrollable) {
            ((ITopScrollable) fragment).scrollToTop();
        }
    }
}
