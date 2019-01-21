package com.rae.cnblogs.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.dialog.ShareDialogFragment;
import com.rae.cnblogs.middleware.R;
import com.rae.cnblogs.middleware.R2;
import com.rae.cnblogs.theme.ThemeCompat;
import com.rae.cnblogs.web.WebViewFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 网页
 * Created by ChenRui on 2017/1/25 0025 9:32.
 */
@Route(path = AppRoute.PATH_WEB)
public class WebActivity extends BasicActivity implements ShareDialogFragment.OnShareListener {


    @BindView(R2.id.tv_web_title)
    TextView mTitleView;

    @BindView(R2.id.img_action_bar_more)
    ImageView mShareView;

    @BindView(R2.id.view_holder)
    View mNightView;

    WebViewFragment mWebViewFragment;

    private ShareDialogFragment mShareDialogFragment;

    @SuppressLint("InvalidR2Usage")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        String url = getUrl();
        if (TextUtils.isEmpty(url)) {
            UICompat.toast(this, "网页路径为空！");
            finish();
            return;
        }

        mShareDialogFragment = ShareDialogFragment.newInstance(null, null, null, null, false);
        // 夜间模式显示遮罩层
        UICompat.setVisibility(mNightView, ThemeCompat.isNight());
        mWebViewFragment = getWebViewFragment(url);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, mWebViewFragment).commitNowAllowingStateLoss();
    }

    protected WebViewFragment getWebViewFragment(String url) {
        return WebViewFragment.newInstance(url, getIntent().getStringExtra("from"));
    }

    @Nullable
    protected String getUrl() {
        Uri uri = getIntent().getData();
        // 没有获取来自深度链接
        if (uri == null || TextUtils.equals(uri.getScheme(), "cnblogs"))
            return getIntent().getStringExtra("url");
        return uri.toString();
    }

    @Override
    public String getWebUrl() {
        return mWebViewFragment.getUrl();
    }

    @Override
    public void onShare(ShareDialogFragment dialog) {
        String url = mWebViewFragment.getUrl();
        if (!url.contains("?")) {
            url += "?share_from=com.rae.cnblogs";
        }
        if (url.contains("&")) {
            url += "&share_from=com.rae.cnblogs";
        }
        dialog.setShareWeb(url, getTitle().toString(), String.format("%s-分享自Android客户端", getTitle()), null);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleView.setText(title);
    }

    @Override
    protected int getHomeAsUpIndicator() {
        if (ThemeCompat.isNight()) return R.drawable.ic_back_closed_night;
        return R.drawable.ic_back_closed;
    }

    @OnClick(R2.id.img_action_bar_more)
    public void onActionMenuMoreClick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = mShareDialogFragment.getTag();
        if (!TextUtils.isEmpty(tag) && fragmentManager.findFragmentByTag(tag) != null) {
            // 已经添加
            fragmentManager.beginTransaction().remove(mShareDialogFragment).commit();
        }
        mShareDialogFragment.show(fragmentManager, "shareWeb");
    }

    public ImageView getShareView() {
        return mShareView;
    }
}
