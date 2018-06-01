package com.rae.cnblogs.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.CnblogsApplication;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.dialog.ShareDialogFragment;
import com.rae.cnblogs.dialog.VersionDialogFragment;
import com.rae.cnblogs.home.setting.SettingContract;
import com.rae.cnblogs.home.setting.SettingPresenterImpl;
import com.rae.cnblogs.sdk.bean.VersionInfo;
import com.rae.cnblogs.widget.ImageLoadingView;
import com.tencent.bugly.beta.tinker.TinkerManager;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;


/**
 * 设置
 * Created by ChenRui on 2017/7/24 0024 1:18.
 */
@Route(path = AppRoute.PATH_SETTING)
public class SettingActivity extends SwipeBackBasicActivity implements SettingContract.View {

    @BindView(R2.id.img_clear_cache)
    ImageLoadingView mClearCacheView;

    @BindView(R2.id.tv_clear_cache)
    TextView mClearCacheTextView;

    @BindView(R2.id.ll_logout)
    View mLogoutLayout;


    @BindView(R2.id.tv_check_update)
    TextView mCheckUpdateMsgView;

    @BindView(R2.id.pb_check_update)
    ProgressBar mCheckUpdateProgress;

    private SettingContract.Presenter mPresenter;

    private ShareDialogFragment mShareDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPresenter = new SettingPresenterImpl(this);
        mPresenter.start();
    }

    @Override
    public void onNotLogin() {
        mLogoutLayout.setVisibility(View.GONE);
    }

    @Override
    public void onLoadVersionInfo(String versionName) {
        mCheckUpdateMsgView.setText(versionName);
    }

    @Override
    public void onLogout() {
        finish();
    }

    /**
     * 清除缓存
     */
    @OnClick(R2.id.ll_clear_cache)
    public void onClearCacheClick() {
        AppMobclickAgent.onClickEvent(getContext(), "ClearCache");
        new DefaultDialogFragment
                .Builder()
                .cancelable(true)
                .message(getString(R.string.tips_clean_cache))
                .confirmText(getString(R.string.clean_now))
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performClearCache();
                    }
                })
                .show(getSupportFragmentManager(), "cleanCache");
    }

    /**
     * 执行清除缓存
     */
    private void performClearCache() {
        // 显示loading
        mClearCacheView.setVisibility(View.VISIBLE);
        mClearCacheView.loading();
        mClearCacheTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        // 清除缓存
        ((CnblogsApplication) getApplication()).clearCache();
        mClearCacheTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClearCacheView.anim();
            }
        }, 1000);

        // 3秒后隐藏
        mClearCacheView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClearCacheView.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                mClearCacheView.setVisibility(View.GONE);

                mClearCacheTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.default_right_arrow, 0);
            }
        }, 5000);
    }

    /**
     * 退出登录
     */
    @OnClick(R2.id.btn_logout)
    public void onLogoutClick() {
        AppMobclickAgent.onClickEvent(getContext(), "Logout");
        new DefaultDialogFragment
                .Builder()
                .cancelable(true)
                .message(getString(R.string.tips_logout))
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.logout();
                    }
                })
                .show(getSupportFragmentManager(), "Logout");
    }

    /**
     * 分享
     */
    @OnClick(R2.id.ll_share)
    public void onShareClick() {
        AppMobclickAgent.onClickEvent(getContext(), "ShareApp");
        if (mShareDialogFragment == null) {
            String url = getString(R.string.share_app_url);
            String title = getString(R.string.share_app_title);
            String desc = getString(R.string.share_app_desc);
            mShareDialogFragment = ShareDialogFragment.newInstance(url, title, desc, null, R.drawable.ic_share_app, false, false);
        }
        mShareDialogFragment.show(getSupportFragmentManager(), "shareApp");
    }

    /**
     * 开源项目
     */
    @OnClick(R2.id.ll_github)
    public void onOpenSourceClick() {
        AppMobclickAgent.onClickEvent(getContext(), "OpenSource");
        AppRoute.routeToWeb(this.getContext(), getString(R.string.github_url));
    }

    /**
     * 开源许可
     */
    @OnClick(R2.id.ll_open_source)
    public void onOpenSourceLicenseClick() {
        AppMobclickAgent.onClickEvent(getContext(), "OpenSourceLicense");
        AppRoute.routeToWeb(this.getContext(), getString(R.string.url_license));
    }

    /**
     * 帮助中心
     */
    @OnClick(R2.id.ll_help_center)
    public void onHelpCenterClick() {
        AppMobclickAgent.onClickEvent(getContext(), "HelpCenter");
        AppRoute.routeToWeb(this.getContext(), getString(R.string.url_help_center));
    }

    @Override
    public void onNoVersion() {
        mCheckUpdateProgress.setVisibility(View.INVISIBLE);
        mCheckUpdateMsgView.setVisibility(View.VISIBLE);
        mCheckUpdateMsgView.setText("已是最新版本");
    }

    @Override
    public void onNewVersion(VersionInfo versionInfo) {
        mCheckUpdateProgress.setVisibility(View.INVISIBLE);
        mCheckUpdateMsgView.setVisibility(View.VISIBLE);
        mCheckUpdateMsgView.setText("发现新版本");
        VersionDialogFragment
                .newInstance(
                        versionInfo.getVersionName(),
                        versionInfo.getAppDesc(),
                        versionInfo.getDownloadUrl())
                .show(getSupportFragmentManager());
    }

    /**
     * 检查更新
     */
    @OnClick(R2.id.ll_check_update)
    public void onCheckUpdateClick() {
        AppMobclickAgent.onClickEvent(getContext(), "CheckUpdate");
        mCheckUpdateProgress.setVisibility(View.VISIBLE);
        mCheckUpdateMsgView.setVisibility(View.GONE);
        mPresenter.checkUpdate();
    }

    /**
     * 好评
     */
    @OnClick(R2.id.praises)
    public void onPraisesClick() {
        AppMobclickAgent.onClickEvent(getContext(), "Praises");
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_url))));
        } catch (Exception e) {
            UICompat.failed(this, getString(R.string.praises_error));
        }
    }

    @OnClick(R2.id.ll_font_setting)
    public void onFontSettingClick() {
        AppRoute.routeToFontSetting(this);
    }

    @OnClick(R2.id.ll_about_me)
    public void onAboutMeClick() {
        AppRoute.routeToAboutMe(this);
    }

    @OnLongClick(R2.id.tv_check_update)
    public boolean onVersionLongClick() {
        String newTinkerId = TinkerManager.getNewTinkerId();
        String tinkerId = TinkerManager.getTinkerId();
        if (!TextUtils.isEmpty(newTinkerId)) {
            UICompat.toastInCenter(this, "基准版本：" + tinkerId + "；补丁版本：" + newTinkerId);
        } else {
            UICompat.toastInCenter(this, "当前版本暂无补丁版本！");
        }
        return false;
    }
}