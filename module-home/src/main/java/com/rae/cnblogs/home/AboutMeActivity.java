package com.rae.cnblogs.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.ApplicationCompat;
import com.rae.cnblogs.dialog.DefaultDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rae on 2018/12/17.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Route(path = AppRoute.PATH_ABOUT_ME)
public class AboutMeActivity extends SwipeBackBasicActivity {

    @BindView(R2.id.tv_version_code)
    TextView mVersionName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        String version = "v" + ApplicationCompat.getVersionName(this);
        mVersionName.setText(version);
    }

    @OnClick(R2.id.tv_mine_name)
    public void onMeClick() {
        new DefaultDialogFragment.Builder()
                .message("感谢大佬关注！")
                .confirmText("FIND ME IN GITHUB")
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://github.com/raedev"));
                        startActivity(intent);
                    }
                })
                .show(getSupportFragmentManager(), "aboutMe");
    }
}
