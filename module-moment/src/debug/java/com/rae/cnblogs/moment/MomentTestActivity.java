package com.rae.cnblogs.moment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.moment.fragment.MomentHomeFragment;

/**
 * Created by rae on 2018/5/17.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class MomentTestActivity extends BasicActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, MomentHomeFragment.newInstance())
                .commit();
    }
}
