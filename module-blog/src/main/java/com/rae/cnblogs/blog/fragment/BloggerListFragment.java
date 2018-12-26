package com.rae.cnblogs.blog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.blog.content.BlogListPresenterImpl;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;

public class BloggerListFragment extends MultipleTypeBlogListFragment {

    public static BloggerListFragment newInstance(String blogApp) {
        Bundle args = new Bundle();
        CategoryBean category = new CategoryBean();
        category.setCategoryId(blogApp);
        args.putParcelable("category", category);
        BloggerListFragment fragment = new BloggerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ContentListContract.Presenter makePresenter() {
        return new BlogListPresenterImpl(this, BlogType.BLOGGER);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppLayout.setPullToRefresh(false);
        mAppLayout.setEnabled(false);
    }
}
