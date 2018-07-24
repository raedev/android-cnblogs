package com.rae.cnblogs.user.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;
import com.rae.cnblogs.user.fragment.SearchFriendsFragment;
import com.rae.cnblogs.user.friends.ISearchListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * search friends
 * Created by rae on 2018/7/23.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_FRIENDS_SEARCH)
public class SearchFriendsActivity extends SwipeBackBasicActivity {

    ISearchListener mSearchListener;

    @BindView(R2.id.et_search)
    EditText mSearchView;

    @BindView(R2.id.btn_search)
    Button mSearchActionView;

    @BindView(R2.id.img_edit_delete)
    ImageView mDeleteView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_search);
        SearchFriendsFragment fragment = new SearchFriendsFragment();
        mSearchListener = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content, fragment)
                .commitNowAllowingStateLoss();

        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UICompat.setVisibility(mDeleteView, mSearchView.length() > 0);
                if (mSearchView.length() > 1) {
                    mSearchActionView.setText(R.string.search);
                } else {
                    mSearchActionView.setText(R.string.cancel);
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
        if (mSearchView.length() > 1)
            mSearchListener.onSearch(mSearchView.getText());
        else
            finish();
    }

    @OnClick(R2.id.img_edit_delete)
    public void onDeleteClick() {
        mSearchView.setText("");
    }

}
