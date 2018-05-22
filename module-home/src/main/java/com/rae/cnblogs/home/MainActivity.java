package com.rae.cnblogs.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.RaeTabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.dialog.VersionDialogFragment;
import com.rae.cnblogs.home.main.MainContract;
import com.rae.cnblogs.home.main.MainPresenterImpl;
import com.rae.cnblogs.sdk.bean.VersionInfo;
import com.rae.cnblogs.sdk.event.PostMomentEvent;
import com.rae.swift.app.RaeFragmentAdapter;

import butterknife.BindView;

@Route(path = AppRoute.PATH_APP_HOME)
public class MainActivity extends BasicActivity implements MainContract.View {

    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    @BindView(R.id.tab_main)
    RaeTabLayout mTabLayout;

    MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenterImpl(this);
        initTab();
        requestPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    private void initTab() {
        RaeFragmentAdapter adapter = new RaeFragmentAdapter(getSupportFragmentManager());

        // 初始化TAB
//        addTab(adapter, R.string.tab_home, R.drawable.tab_home, HomeFragment.newInstance());
        addTab(adapter, R.string.tab_sns, R.drawable.tab_news, AppRoute.newMomentFragment());
//        addTab(adapter, R.string.tab_discover, R.drawable.tab_library, DiscoverFragment.newInstance());
        addTab(adapter, R.string.tab_mine, R.drawable.tab_mine, AppRoute.newMineFragment());

        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.setAdapter(adapter);

        // 联动
        mTabLayout.addOnTabSelectedListener(new RaeTabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new RaeTabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    private void addTab(RaeFragmentAdapter adapter, int resId, int iconId, @NonNull Fragment fragment) {
        RaeTabLayout.Tab tab = mTabLayout.newTab();
        View tabView = View.inflate(this, R.layout.tab_view, null);
        TextView v = tabView.findViewById(R.id.tv_tab_view);
        ImageView iconView = tabView.findViewById(R.id.img_tab_icon);
        v.setText(resId);
        iconView.setImageResource(iconId);
        tab.setCustomView(tabView);
        mTabLayout.addTab(tab);
        adapter.add(getString(resId), fragment);
    }


    private void requestPermissions() {
        // 检查权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            new DefaultDialogFragment
                    .Builder()
                    .message(getString(R.string.permission_request_message))
                    .confirmText(getString(R.string.allow))
                    .cancelText(getString(R.string.permission_cancel))
                    .confirm(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                        }
                    })
                    .show(getSupportFragmentManager(), "permissionDialog");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 授权返回
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // 用户拒绝授权，再一次提示授权
            new DefaultDialogFragment
                    .Builder()
                    .message(getString(R.string.permission_tips_message))
                    .confirmText(getString(R.string.permission_granted))
                    .cancelText(getString(R.string.permission_cancel))
                    .confirm(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                        }
                    })
                    .show(getSupportFragmentManager(), "permissionDialog");

        }
    }

    @Override
    public void onNewVersion(VersionInfo versionInfo) {
        VersionDialogFragment
                .newInstance(versionInfo.getVersionName(), versionInfo.getAppDesc(), versionInfo.getDownloadUrl())
                .show(getSupportFragmentManager());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String event = intent.getStringExtra("event");

        // 发布闪存成功，从通知栏点击返回到首页来，切换到闪存TAB
        if (event != null && PostMomentEvent.class.getName().equals(event)) {
            mViewPager.setCurrentItem(1);
        }

    }
}
