package com.rae.cnblogs.discover.ui;

import android.os.Bundle;
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
import com.rae.cnblogs.discover.fragment.AntColumnFragment;
import com.rae.cnblogs.discover.presenter.IAntColumnContract;
import com.rae.cnblogs.widget.RaeScrollTopTabListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 专栏列表
 */
@Route(path = AppRoute.PATH_DISCOVER_COLUMN)
public class AntColumnActivity extends SwipeBackBasicActivity {
    @BindView(R2.id.view_pager)
    ViewPager mViewPager;

    @BindView(R2.id.tab)
    RaeTabLayout mRaeTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_column);
        setTitle(" ");

        AntColumnAdapter adapter = new AntColumnAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
        mRaeTabLayout.setupWithViewPager(mViewPager);
        mRaeTabLayout.addOnTabSelectedListener(new RaeScrollTopTabListener(mViewPager, getSupportFragmentManager()));

        int position = getIntent().getIntExtra("position", 0);
        mViewPager.setCurrentItem(position);
    }

    @OnClick(R2.id.img_question)
    public void onQuestionClick() {
        AppRoute.routeToAntColumnWeb(this, getString(R.string.url_antcode_about));
    }

    class AntColumnAdapter extends FragmentPagerAdapter {
        private final List<String> titles = new ArrayList<>();
        private final List<Integer> mTypes = new ArrayList<>();

        AntColumnAdapter(FragmentManager fm) {
            super(fm);
            titles.add("我的");
            mTypes.add(IAntColumnContract.TYPE_MY);

            titles.add("推荐");
            mTypes.add(IAntColumnContract.TYPE_RECOMMEND);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int i) {
            Integer type = mTypes.get(i);
            return AntColumnFragment.newInstance(type);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

    }
}
