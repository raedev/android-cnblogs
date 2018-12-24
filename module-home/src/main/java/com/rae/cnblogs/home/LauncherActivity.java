package com.rae.cnblogs.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.home.launcher.LauncherContract;
import com.rae.cnblogs.home.launcher.LauncherPresenterImpl;
import com.rae.cnblogs.theme.ThemeCompat;
import com.rae.cnblogs.widget.CountDownTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 启动页
 * Created by ChenRui on 2016/12/22 22:08.
 * Refactor by ChenRui on 2018/05/11 11:00.
 */
public class LauncherActivity extends BasicActivity implements LauncherContract.View {

    @BindView(R2.id.img_launcher_display)
    ImageView mDisplayView;

    @BindView(R2.id.tv_launcher_name)
    TextView mNameView;

    @BindView(R2.id.tv_skip)
    CountDownTextView mCountDownTextView;

    LauncherContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 非栈顶的时候，点击首页图标不跳转到这里
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_launcher);
        mPresenter = new LauncherPresenterImpl(this);

//        if (BuildConfig.DEBUG) {
//            onRouteToHome();
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
        mCountDownTextView.start();
    }

    @Override
    protected void onStop() {
        mPresenter.destroy();
        mCountDownTextView.stop();
        mCountDownTextView.reset();
        super.onStop();
    }

    /**
     * 显示图片
     */
    private void showImage(String url) {
        Log.i("Rae", "加载图片：" + url);
        AppImageLoader.displayWithoutPlaceHolder(url, mDisplayView);
    }

    @Override
    public void onLoadImage(String name, @NonNull String url) {
        mNameView.setText(Html.fromHtml(name));
        showImage(url);
    }

    @Override
    public void onImageChanged() {
        mCountDownTextView.reset();
        mCountDownTextView.start();
    }

    @Override
    public void onRouteToWeb(@NonNull String url) {
        AppRoute.routeToMain(this);
        AppRoute.routeToWeb(this, url);
        finish();
    }

    @Override
    public void onEmptyImage() {
        // 加载默认
        if (!ThemeCompat.isNight() && mDisplayView != null) {
            mDisplayView.setImageResource(R.drawable.launcher_background);
        }
    }

    @OnClick(R2.id.img_launcher_display)
    public void onAdClick() {
        mPresenter.onAdClick();
    }

    @Override
    public void onRouteToHome() {
        AppRoute.routeToMain(this);
        finish();
    }


    @OnClick(R2.id.tv_skip)
    public void onSkipClick() {
        onRouteToHome();
    }
}
