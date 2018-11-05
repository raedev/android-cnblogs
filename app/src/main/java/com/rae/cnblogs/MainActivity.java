package com.rae.cnblogs;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.RaeTabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.basic.AppFragmentAdapter;
import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.blog.CnblogsService;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.dialog.VersionDialogFragment;
import com.rae.cnblogs.home.main.MainContract;
import com.rae.cnblogs.home.main.MainPresenterImpl;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.bean.VersionInfo;
import com.rae.cnblogs.sdk.event.PostMomentEvent;
import com.rae.cnblogs.widget.ITopScrollable;

import butterknife.BindView;

@Route(path = AppRoute.PATH_APP_HOME)
public class MainActivity extends BasicActivity implements MainContract.View, RaeTabLayout.OnTabSelectedListener {

    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    @BindView(R.id.tab_main)
    RaeTabLayout mTabLayout;

    private long mBackKeyDownTime;

    MainContract.Presenter mPresenter;
    private AppFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenterImpl(this);
        // 测试登录
        debugLogin();
        initTab();
        requestPermissions();
        // 启动服务
        startService(new Intent(this, CnblogsService.class));
    }


    protected void debugLogin() {
        String url = "cnblogs.com";
        String cookie = "26004F7A01C39CBACF70ED396FFA307BB21E7AEDE4B6245B5B433F47540749569981C77803953DFE28B7F5741FB795F49187BB7BDA995F5B150A8C65B418D70E25802D583F09F66005298BD6A817C913884D4436";
        String netCoreCookie = "CfDJ8FHXRRtkJWRFtU30nh_M9mBulNHC1c_O8RDhAJhrChUixuMz1QxRLZ-f-3-WHrQV6olTonldUcMkhUBz2wW3bB4ZMIHEnQUr7EP2HREgyEeyaffzgkf-7eMRCxKejSvBV81SdD5CQXuyAQF5jCHElhQ2eMfB8vz26qp4_Df5eZmfH8e0eOiWjrXY1FRW-WcWbUzHLKwNfYiDaZx5F5n1bEPMJDzRfKeOKPZOxFxM_mFCC_BTy-_gQ79Qdvpg15bVs5FkK_oCdnZBS5o9sh5_BrSNWpAnqzWF97uvWptCoJ5N";
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, ".CNBlogsCookie=" + cookie + "; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.setCookie(url, ".Cnblogs.AspNetCore.Cookies=" + netCoreCookie + "; domain=.cnblogs.com; path=/; HttpOnly");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }

        // 获取用户信息
        AndroidObservable.create(CnblogsApiFactory.getInstance(this).getUserApi().getUserInfo("chenrui7"))
                .with(this)
                .subscribe(new ApiDefaultObserver<UserInfoBean>() {
                    @Override
                    protected void onError(String message) {

                    }

                    @Override
                    protected void accept(UserInfoBean userInfo) {
                        UserProvider.getInstance().setLoginUserInfo(userInfo);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    private void initTab() {
        mAdapter = new AppFragmentAdapter(getSupportFragmentManager());

        // 初始化TAB
        addTab(mAdapter, R.string.tab_home, R.drawable.tab_home, AppRoute.newHomeFragment());
        addTab(mAdapter, R.string.tab_sns, R.drawable.tab_news, AppRoute.newMomentFragment());
//        addTab(mAdapter, R.string.tab_discover, R.drawable.tab_library, AppRoute.newDiscoverFragment());
        addTab(mAdapter, R.string.tab_mine, R.drawable.tab_mine, AppRoute.newMineFragment());

        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
        mViewPager.setAdapter(mAdapter);

        // 联动
        mTabLayout.addOnTabSelectedListener(new RaeTabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new RaeTabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(this);
    }

    private void addTab(AppFragmentAdapter adapter, int resId, int iconId, Fragment fragment) {
        if (fragment == null) {
            Log.e("rae", "初始化首页TAB的Fragment为空！" + getString(resId));
            return;
        }

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

    @Override
    protected void onDestroy() {
        mTabLayout.removeOnTabSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public void onTabSelected(RaeTabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(RaeTabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(RaeTabLayout.Tab tab) {
        int position = tab.getPosition();
        Fragment fragment = mAdapter.getCurrent(mViewPager.getId(), position);
        if (fragment instanceof ITopScrollable) {
            ((ITopScrollable) fragment).scrollToTop();
        }
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
                            String[] permissionList = new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.READ_LOGS,
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.SET_DEBUG_APP,
                                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                                    Manifest.permission.GET_ACCOUNTS,
                                    Manifest.permission.WRITE_APN_SETTINGS
                            };
                            ActivityCompat.requestPermissions(MainActivity.this, permissionList, 100);
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

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mBackKeyDownTime) > 2000) {
            UICompat.toast(this, "再按一次退出");
            mBackKeyDownTime = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }
}
