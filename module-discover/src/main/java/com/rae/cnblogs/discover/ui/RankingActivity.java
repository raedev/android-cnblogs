package com.rae.cnblogs.discover.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.RaeTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.discover.fragment.RankingFragment;
import com.rae.cnblogs.discover.presenter.IRankingContract;
import com.rae.cnblogs.widget.RaeScrollTopTabListener;
import com.rae.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Route(path = AppRoute.PATH_DISCOVER_RANKING)
public class RankingActivity extends SwipeBackBasicActivity {

    @BindView(R2.id.tab)
    RaeTabLayout mTabLayout;

    @BindView(R2.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle(" ");

        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager())
                .Add(IRankingContract.TYPE_TOP_READ, "周阅榜")
                .Add(IRankingContract.TYPE_HOT_SEARCH, "热搜榜")
                .Add(IRankingContract.TYPE_TOP_AUTHOR, "大神榜");

        // 收藏榜要登录之后才能查看
        if (SessionManager.getDefault().isLogin()) {
            adapter.Add(IRankingContract.TYPE_TOP_FAVORITE, "收藏榜");
        }

        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new RaeScrollTopTabListener(mViewPager, getSupportFragmentManager()));
    }


    private class TabFragmentAdapter extends FragmentPagerAdapter {

        @NonNull
        private List<String> mTabTitles = new ArrayList<>();
        private List<Integer> mTypes = new ArrayList<>();

        TabFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        TabFragmentAdapter Add(int type, String name) {
            mTabTitles.add(name);
            mTypes.add(type);
            return this;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles.get(position);
        }

        @Override
        public Fragment getItem(int i) {
            return RankingFragment.newInstance(mTypes.get(i));
        }

        @Override
        public int getCount() {
            return mTabTitles.size();
        }
    }
}
