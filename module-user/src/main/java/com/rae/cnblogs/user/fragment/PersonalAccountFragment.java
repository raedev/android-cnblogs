package com.rae.cnblogs.user.fragment;

import android.widget.EditText;

import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;

import butterknife.BindView;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PersonalAccountFragment extends BasicFragment {

    @BindView(R2.id.et_text)
    EditText mTextView;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_personal_account;
    }

    @Override
    public void onResume() {
        super.onResume();
        UserInfoBean userInfo = UserProvider.getInstance().getLoginUserInfo();
        if (userInfo != null) {
            mTextView.setText(userInfo.getBlogApp());
            mTextView.setSelection(mTextView.length());
            mTextView.setHint("请输入新的账号");
        }
    }
}
