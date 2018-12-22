package com.rae.cnblogs.home.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.home.R;
import com.rae.cnblogs.home.fragment.HotSearchFragment;
import com.rae.cnblogs.home.fragment.SearchResultFragment;
import com.rae.cnblogs.home.fragment.SearchSuggestFragment;
import com.rae.cnblogs.sdk.event.SearchEvent;
import com.rae.cnblogs.user.R2;
import com.rae.cnblogs.user.friends.ISearchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索
 * Created by rae on 2018/7/25.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_SEARCH)
public class SearchActivity extends BasicActivity {

    @BindView(R2.id.et_search)
    EditText mSearchView;

    @BindView(R2.id.btn_search)
    TextView mSearchActionView;

    @BindView(R2.id.img_edit_delete)
    ImageView mDeleteView;

    Fragment mHotSearchFragment; // 热门搜索
    Fragment mSearchSuggestFragment; // 搜索建议
    @Nullable
    Fragment mSearchResultFragment; // 搜索结果

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.keep_current);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AppMobclickAgent.onClickEvent(getContext(), "Search"); // 统计
        EventBus.getDefault().register(this);
        initViews();
        mHotSearchFragment = new HotSearchFragment();
        mSearchSuggestFragment = new SearchSuggestFragment();
        showHotSearchFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViews() {
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && v.length() > 0) {
                    onSearchClick();
                    return true;
                }
                return false;
            }
        });

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                UICompat.setVisibility(mDeleteView, mSearchView.length() > 0);
                if (mSearchView.length() > 0) {
                    showSuggestFragment();
                    mSearchActionView.setText(com.rae.cnblogs.user.R.string.search);
                    ISearchListener searchListener = (ISearchListener) mSearchSuggestFragment;
                    searchListener.onSearch(text);
                } else {
                    showHotSearchFragment();
                    mSearchActionView.setText(com.rae.cnblogs.user.R.string.cancel);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick(R2.id.btn_search)
    public void onSearchClick() {
        UICompat.hideSoftInputFromWindow(this);
        if (mSearchView.length() > 0)
            // 执行搜索
            performSearch(mSearchView.getText().toString());
        else
            finish();
    }

    /**
     * 执行搜索
     */
    private void performSearch(String text) {
        if (TextUtils.isEmpty(text)) return;
        // 显示搜索结果页，由子Fragment处理
        if (mSearchResultFragment == null) {
            mSearchResultFragment = new SearchResultFragment();
        }
        text = text.trim();
        Bundle args = new Bundle();
        args.putString(Intent.EXTRA_TEXT, text);
        mSearchResultFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.content, mSearchResultFragment, "SearchResult");
        transaction.commit();
        UICompat.hideSoftInputFromWindow(this);

        // 埋点
        AppMobclickAgent.onSearchEvent(this, text);
        // 保存搜索记录
//        saveHistory(text);
    }

    @OnClick(R2.id.img_edit_delete)
    public void onDeleteClick() {
        mSearchView.setText("");
    }

    @Subscribe
    public void onEvent(SearchEvent event) {
        // 从热门搜索里面进来的
        UICompat.setText(mSearchView, event.getSearchText());
        onSearchClick();
    }


    /**
     * 显示热门搜索
     */
    private void showHotSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.content, mHotSearchFragment, "HotSearch");
        transaction.commit();
        Log.i("rae", "加载热门搜索");
    }

    /**
     * 显示搜索建议
     */
    private void showSuggestFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Suggest");
        if (fragment != null) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.content, mSearchSuggestFragment, "Suggest");
        transaction.commit();
        Log.i("rae", "加载搜索建议");
    }
}
