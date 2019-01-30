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
import com.rae.cnblogs.discover.fragment.BlogQuestionFragment;
import com.rae.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 博问列表
 */
@Route(path = AppRoute.PATH_DISCOVER_BLOG_QUESTION)
public class BlogQuestionActivity extends SwipeBackBasicActivity {

    @BindView(R2.id.view_pager)
    ViewPager mViewPager;

    @BindView(R2.id.tab)
    RaeTabLayout mRaeTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_question);
        mViewPager.setAdapter(new QuestionAdapter(getSupportFragmentManager()));
        mRaeTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(3);
        setTitle(" ");
    }

    class QuestionAdapter extends FragmentPagerAdapter {
        private List<String> titles = new ArrayList<>();

        QuestionAdapter(FragmentManager fm) {
            super(fm);
            titles.add("待解决");
            titles.add("高分题");
            if (SessionManager.getDefault().isLogin()) {
                titles.add("我的");
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int i) {
            return BlogQuestionFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return titles.size();
        }
    }
}
