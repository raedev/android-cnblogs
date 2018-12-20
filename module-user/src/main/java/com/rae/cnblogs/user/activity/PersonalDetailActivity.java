package com.rae.cnblogs.user.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.fragment.PersonalAccountFragment;
import com.rae.cnblogs.user.fragment.PersonalIntroduceFragment;
import com.rae.cnblogs.user.fragment.PersonalNickNameFragment;
import com.rae.cnblogs.user.fragment.PersonalPasswordFragment;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_PERSONAL_DETAIL)
public class PersonalDetailActivity extends SwipeBackBasicActivity {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_ACCOUNT = 1;
    private static final int TYPE_NICKNAME = 2;
    private static final int TYPE_PWD = 3;
    private static final int TYPE_DESC = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);

        int type = getIntent().getIntExtra("type", TYPE_NORMAL);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        switch (type) {
            case TYPE_ACCOUNT:
                fragment = new PersonalAccountFragment();
                setTitle("修改账号");
                break;
            case TYPE_NICKNAME:
                fragment = new PersonalNickNameFragment();
                setTitle("修改昵称");
                break;
            case TYPE_PWD:
                setTitle("修改密码");
                fragment = new PersonalPasswordFragment();
                break;
            case TYPE_DESC:
            default:
                fragment = new PersonalIntroduceFragment();
                getWindow().clearFlags(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                break;
        }

        transaction.replace(R.id.content, fragment);
        transaction.commit();

    }

}
