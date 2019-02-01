package com.rae.cnblogs.discover.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.web.WebViewFragment;

/**
 * 详情
 */
@Route(path = AppRoute.PATH_DISCOVER_BLOG_QUESTION_DETAIL)
public class BlogQuestionDetailActivity extends SwipeBackBasicActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        String url = getIntent().getStringExtra("url");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, QuestionWebFragment.newInstance(url))
                .commitNow();

    }

    @Override
    public void setTitle(CharSequence title) {
        title = getString(R.string.label_question_detail);
        super.setTitle(title);
    }

    public static class QuestionWebFragment extends WebViewFragment {
        public static QuestionWebFragment newInstance(String url) {

            Bundle args = new Bundle();
            args.putString("url", url);
            QuestionWebFragment fragment = new QuestionWebFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            enablePullToRefresh(false);
        }
    }
}
