package com.rae.cnblogs.moment.fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.RaeTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.moment.R;
import com.rae.cnblogs.moment.R2;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.event.PostMomentEvent;
import com.rae.cnblogs.widget.ITopScrollable;
import com.rae.cnblogs.widget.RaeViewPager;
import com.rae.cnblogs.widget.ToolbarToastView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 朋友圈（闪存）
 * Created by ChenRui on 2017/10/26 0026 23:31.
 */
@Route(path = AppRoute.PATH_FRAGMENT_MOMENT)
public class MomentHomeFragment extends BasicFragment implements ITopScrollable {

    public static MomentHomeFragment newInstance() {
        return new MomentHomeFragment();
    }

    @BindView(R2.id.tab_layout)
    RaeTabLayout mTabLayout;
    @BindView(R2.id.view_pager)
    RaeViewPager mViewPager;

    @BindView(R2.id.tool_bar_toast_view)
    ToolbarToastView mToastView;

    private MomentHomeFragmentAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_moment_home;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MomentHomeFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        // 相互关联
        mViewPager.addOnPageChangeListener(new RaeTabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new RaeTabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.addOnTabSelectedListener(new DefaultOnTabSelectedListener());
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RaeTabLayout.Tab tab = mTabLayout.getTabAt(0);
        if (tab != null) {
            tab.select();
        }
    }

    @OnClick(R2.id.tv_post)
    public void onPostClick() {
        // 统计闪存发布按钮点击
        AppMobclickAgent.onClickEvent(getContext(), "PostMoment_Enter");
        if (!UserProvider.getInstance().isLogin()) {
            AppRoute.routeToLoginForResult(getActivity());
        } else {
            AppRoute.routeToPostMoment(getActivity());
        }
    }


    @OnClick(R2.id.img_mine)
    public void onMessageClick() {
        if (UserProvider.getInstance().isLogin()) {
            dismissToast();
            AppRoute.routeToMomentMessage(this.getContext());
        } else {
            AppRoute.routeToLogin(getContext());
        }
    }

    @OnClick(R2.id.tool_bar_toast_view)
    public void onToastClick() {
        mToastView.dismiss();
        int type = mToastView.getType();
        // 回复我的消息
        if (type == ToolbarToastView.TYPE_REPLY_ME) {
            AppRoute.routeToMomentMessage(this.getContext());
        }
        // 闪存发布成功，点击返回顶部
        if (type == ToolbarToastView.TYPE_POST_SUCCESS && mAdapter != null && mViewPager.getCurrentItem() >= 0) {
            Fragment momentFragment = mAdapter.getItem(mViewPager.getCurrentItem());
            if (momentFragment instanceof ITopScrollable) {
                ((ITopScrollable) momentFragment).scrollToTop();
            }
        }
        // 提到我的
        if (type == ToolbarToastView.TYPE_AT_ME) {
            AppRoute.routeToMomentAtMe(getContext());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AppRoute.REQ_LOGIN) {
            AppRoute.routeToPostMoment(getActivity());
        }
    }

    @Override
    public void scrollToTop() {
        Fragment fragment = mAdapter.getCurrent(mViewPager.getId(), mViewPager.getCurrentItem());
        if (fragment instanceof ITopScrollable) {
            ((ITopScrollable) fragment).scrollToTop();
        }
    }

    public void showToast(int type, String msg) {
        mToastView.setType(type);
        mToastView.show(msg);
    }

    public void dismissToast() {
        mToastView.dismiss();
    }

    public static class MomentHomeFragmentAdapter extends FragmentPagerAdapter {

        private final List<MomentFragment> mFragments = new ArrayList<>();
        private final FragmentManager mFragmentManager;

        MomentHomeFragmentAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragments.add(MomentFragment.newInstance(IMomentApi.MOMENT_TYPE_ALL));
            mFragments.add(MomentFragment.newInstance(IMomentApi.MOMENT_TYPE_FOLLOWING));
            mFragments.add(MomentFragment.newInstance(IMomentApi.MOMENT_TYPE_MY));
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return Rx.getCount(mFragments);
        }

        public Fragment getCurrent(int viewId, int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + viewId + ":" + position);
        }
    }

    class DefaultOnTabSelectedListener implements RaeTabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(RaeTabLayout.Tab tab) {
            int count = mTabLayout.getTabCount();
            for (int i = 0; i < count; i++) {
                RaeTabLayout.Tab tabAt = mTabLayout.getTabAt(i);
                if (tabAt == null) continue;
                tabAt.setTextStyle(tab == tabAt ? 1 : 0);
            }
        }

        @Override
        public void onTabUnselected(RaeTabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(RaeTabLayout.Tab tab) {
            onTabSelected(tab);
            scrollToTop();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PostMomentEvent event) {

        // 闪存事件
        if (event.isDeleted()) return;

        if (event.getIsSuccess()) {
            showToast(ToolbarToastView.TYPE_POST_SUCCESS, "发布成功");
            cancelPostMomentNotification(event);
        } else {
            new DefaultDialogFragment
                    .Builder()
                    .confirmText("立即查看")
                    .message("发布闪存失败")
                    .confirm(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // 跳转到闪存发布
                            AppRoute.routeToPostMoment(getActivity(), event.getMomentMetaData());
                        }
                    })
                    .dismiss(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            cancelPostMomentNotification(event);
                        }
                    })
                    .show(getFragmentManager());
        }

    }

    private void cancelPostMomentNotification(PostMomentEvent event) {
        // 清除通知
        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null && event.getNotificationId() > 0) {
            nm.cancel(event.getNotificationId());
        }
    }
}
