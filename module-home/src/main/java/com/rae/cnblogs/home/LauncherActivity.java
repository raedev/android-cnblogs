package com.rae.cnblogs.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.basic.activity.BasicActivity;
import com.rae.cnblogs.home.launcher.LauncherContract;

/**
 * 启动页
 * Created by ChenRui on 2016/12/22 22:08.
 * Refactor by ChenRui on 2018/05/11 11:00.
 */
public class LauncherActivity extends BasicActivity implements LauncherContract.View {
    //
//    @BindView(R.id.img_launcher_display)
//    ImageView mDisplayView;
//
//    @BindView(R.id.tv_launcher_name)
//    TextView mNameView;
//
//    @BindView(R.id.tv_skip)
//    CountDownTextView mCountDownTextView;
//
//    ILauncherPresenter mLauncherPresenter;
//
//
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 非栈顶的时候，点击首页图标不跳转到这里
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_launcher);
//        mLauncherPresenter = CnblogsPresenterFactory.getLauncherPresenter(this, this);
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mLauncherPresenter.start();
//        mCountDownTextView.start();
//    }
//
//    @Override
//    protected void onStop() {
//        mLauncherPresenter.cancel();
//        mCountDownTextView.stop();
//        mCountDownTextView.reset();
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mLauncherPresenter.destroy();
//        super.onDestroy();
//    }
//
//    private void showImage(String url) {
//        RaeImageLoader.displayImage(url, mDisplayView);
//    }
//
//    @Override
//    public void onLoadImage(String name, String url) {
//        if (TextUtils.isEmpty(url)) {
//            onNormalImage();
//            return;
//        }
//        mNameView.setText(Html.fromHtml(name));
//        showImage(url);
//    }
//
//    @Override
//    public void onJumpToWeb(String url) {
//        // 网页路径为空不跳转
//        if (TextUtils.isEmpty(url)) {
//            return;
//        }
//        AppRoute.routeToMain(this);
//        AppRoute.routeToWeb(this, url);
//        finish();
//    }
//
//    @Override
//    public void onJumpToBlog(String id) {
//        AppRoute.routeToMain(this);
//        AppRoute.routeToBlogContent(this, id, BlogType.BLOG);
//        finish();
//    }
//
//    @Override
//    public void onNormalImage() {
//        // 加载默认
//        if (!ThemeCompat.isNight() && mDisplayView != null) {
//            mDisplayView.setImageResource(R.drawable.launcher_background);
//        }
//    }
//
//    @OnClick(R.id.img_launcher_display)
//    public void onAdClick() {
//        mLauncherPresenter.advertClick();
//    }
//
//    @Override
//    public void onJumpToMain() {
//        AppRoute.routeToMain(this);
//        finish();
//    }
//
//
//    @OnClick(R.id.tv_skip)
//    public void onSkipClick() {
//        mLauncherPresenter.stop();
//        mCountDownTextView.stop();
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//    }
}
