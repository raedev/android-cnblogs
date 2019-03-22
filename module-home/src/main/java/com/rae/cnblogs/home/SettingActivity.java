package com.rae.cnblogs.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kyleduo.switchbutton.SwitchButton;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.CnblogsApplication;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.dialog.VersionDialogFragment;
import com.rae.cnblogs.home.setting.SettingContract;
import com.rae.cnblogs.home.setting.SettingPresenterImpl;
import com.rae.cnblogs.sdk.bean.VersionInfo;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
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


    @BindView(R2.id.sb_wifi_image)
    SwitchButton mBlogImageButton;

    @BindView(R2.id.sb_web_reader)
    SwitchButton mReaderTipsButton;

    @BindView(R2.id.tv_check_update)
    TextView mCheckUpdateMsgView;

    @BindView(R2.id.pb_check_update)
    ProgressBar mCheckUpdateProgress;

    private SettingContract.Presenter mPresenter;
    private CnblogAppConfig mAppConfig;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mAppConfig = CnblogAppConfig.getInstance(this);
        mPresenter = new SettingPresenterImpl(this);
        mPresenter.start();

        // 智能无图按钮点击
        mBlogImageButton.setChecked(mAppConfig.disableBlogImage());
        mBlogImageButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                mAppConfig.setDisableBlogImage(isChecked);
            }
        });

        // 阅读优化提示
        mReaderTipsButton.setChecked(mAppConfig.canReaderTips());
        mReaderTipsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                mAppConfig.setReaderTips(isChecked);
            }
        });
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
                        dialog.dismiss();
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
                .confirmText("立即退出")
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.logout();
                    }
                })
                .show(getSupportFragmentManager(), "Logout");
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
        ContentEntity entity = new ContentEntity();
        entity.setId("10131552");
        entity.setTitle("博客园APP帮助中心");
        entity.setDate("2018-12-17");
        entity.setUrl("https://www.cnblogs.com/chenrui7/p/10131552.html");
        entity.setSummary("博客园APP帮助中心");
        entity.setAuthor("RAE");
        entity.setAuthorId("chenrui7");
        AppRoute.routeToContentDetail(getContext(), entity);
//        AppRoute.routeToWeb(this.getContext(), getString(R.string.url_help_center));
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

    @OnClick(R2.id.ll_about_me)
    public void onAboutMeClick() {
        AppRoute.routeToAboutMe(this);
    }


    /**
     * 长按进入开发工具
     */
    @OnLongClick(R2.id.ll_about_me)
    public boolean onAboutMeLongClick() {
        startActivity(new Intent(this, ToolActivity.class));
        return true;
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
