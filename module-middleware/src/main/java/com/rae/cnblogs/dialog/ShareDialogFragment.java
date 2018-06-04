package com.rae.cnblogs.dialog;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.middleware.R2;
import com.rae.cnblogs.theme.ThemeCompat;
import com.rae.cnblogs.widget.R;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rae on 2018/5/15.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class ShareDialogFragment extends BasicDialogFragment {

    public static ShareDialogFragment newInstance(String url,
                                                  String title,
                                                  String desc,
                                                  String imageUrl,
                                                  int resId,
                                                  boolean extVisibility,
                                                  boolean extLayoutVisibility) {

        Bundle args = new Bundle();
        args.putString("shareUrl", url);
        args.putString("shareTitle", title);
        args.putString("shareDesc", desc);
        args.putString("shareImageUrl", imageUrl);
        args.putInt("shareResId", resId);
        args.putBoolean("extLayoutVisibility", extVisibility);
        ShareDialogFragment fragment = new ShareDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ShareDialogFragment newInstance(String url,
                                                  String title,
                                                  String desc,
                                                  String imageUrl,
                                                  int resId,
                                                  boolean extVisibility) {
        return newInstance(url, title, desc, imageUrl, resId, extVisibility, true);
    }

    /**
     * @param extVisibility 扩展栏是否显示
     */
    public static ShareDialogFragment newInstance(String url,
                                                  String title,
                                                  String desc,
                                                  String imageUrl,
                                                  boolean extVisibility) {

        return newInstance(url, title, desc, imageUrl, 0, extVisibility);
    }

    /**
     */
    public static ShareDialogFragment newInstance(String url,
                                                  String title,
                                                  String desc,
                                                  String imageUrl) {

        return newInstance(url, title, desc, imageUrl, 0, true);
    }

    /**
     * @param extVisibility 扩展栏是否显示
     */
    public static ShareDialogFragment newInstance(String url,
                                                  String title,
                                                  String desc,
                                                  int resId,
                                                  boolean extVisibility) {

        return newInstance(url, title, desc, null, resId, extVisibility);
    }

    public interface OnShareListener {

        String getWebUrl();

        void onShare(ShareDialogFragment dialog);
    }

    @BindView(R2.id.tv_share_wechat)
    View mWeChatView;

    @BindView(R2.id.tv_share_wechat_sns)
    View mWeChatSNSView;

    @BindView(R2.id.tv_share_qq)
    View mQQView;

    @BindView(R2.id.tv_share_qzone)
    View mQzoneView;

    @BindView(R2.id.tv_share_sina)
    View mSinaView;

    @BindView(R2.id.tv_share_source)
    View mViewSourceView;

    @BindView(R2.id.tv_share_link)
    View mLinkView;

    @BindView(R2.id.tv_share_browser)
    View mBrowseriew;

    @BindView(R2.id.btn_share_cancel)
    Button mCancelView;

    @BindView(R2.id.tv_share_font)
    TextView mFontSettingView;

    @BindView(R2.id.hl_ext_action_layout)
    View mExtLayout;

    @BindView(R2.id.view_divider)
    View mDividerView;


    @BindView(R2.id.tv_share_night)
    TextView mNightView;

    private OnShareListener mOnShareClickListener;

    ShareAction mShareAction;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_share;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareAction = new ShareAction(getActivity());
        Bundle arg = getArguments();
        if (arg != null) {
            String url = arg.getString("shareUrl", "url");
            String title = arg.getString("shareTitle", "title");
            String desc = arg.getString("shareDesc");
            String imageUrl = arg.getString("shareImageUrl");
            int resId = arg.getInt("shareResId");

            if (resId != 0) {
                setShareWeb(url, title, desc, resId);
            } else {
                setShareWeb(url, title, desc, imageUrl);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setShareIcon(R.drawable.ic_share_app);
    }

    public void setShareTitle(String title) {
        mShareAction.withText(title);
    }

    public void setShareUrl(String url) {
        mShareAction.withMedia(new UMWeb(url));
    }

    public void setShareWeb(@NonNull String url, @NonNull String title, @Nullable String desc, String thumb) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        if (!TextUtils.isEmpty(desc)) {
            web.setDescription(desc);
        }
        if (!TextUtils.isEmpty(thumb)) {
            web.setThumb(new UMImage(getContext(), thumb));
        } else {
            web.setThumb(new UMImage(getContext(), R.drawable.ic_share_app));
        }
        mShareAction.withMedia(web);
    }

    public void setShareWeb(@NonNull String url, @NonNull String title, @Nullable String desc, @Nullable int thumbResId) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setDescription(desc);
        web.setThumb(new UMImage(getContext(), thumbResId));
        mShareAction.withMedia(web);
    }

    public void setShareSummary(String summary) {
        mShareAction.withText(summary);
    }

    public void setShareIcon(int resId) {
        mShareAction.withMedia(new UMImage(getContext(), resId));
    }

    public void setShareIcon(String url) {
        mShareAction.withMedia(new UMImage(getContext(), url));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof OnShareListener) {
            mOnShareClickListener = (OnShareListener) getActivity();
        }
        if (mOnShareClickListener == null && getParentFragment() instanceof OnShareListener) {
            mOnShareClickListener = (OnShareListener) getParentFragment();
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            boolean visibility = arguments.getBoolean("extVisibility", true);
            boolean extLayoutVisibility = arguments.getBoolean("extLayoutVisibility", true);
            setExtVisibility(visibility ? View.VISIBLE : View.GONE);
            setExtLayoutVisibility(extLayoutVisibility ? View.VISIBLE : View.GONE);
        }

        showNightText();
    }

    @Override
    protected void onLoadWindowAttr(@NonNull Window window) {
        super.onLoadWindowAttr(window);
        WindowManager.LayoutParams attr = window.getAttributes();
        attr.gravity = Gravity.START | Gravity.END | Gravity.BOTTOM;
        attr.horizontalMargin = 0;
        attr.verticalMargin = 0;
        attr.width = WindowManager.LayoutParams.MATCH_PARENT;
        attr.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attr);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 设置扩展栏可见性
     */
    public void setExtVisibility(int visibility) {
        mViewSourceView.setVisibility(visibility);
        mNightView.setVisibility(visibility);
        mFontSettingView.setVisibility(visibility);
//        mExtLayout.setVisibility(visibility);
//        mDividerView.setVisibility(visibility);
    }

    public void setExtLayoutVisibility(int visibility) {
        mExtLayout.setVisibility(visibility);
        mDividerView.setVisibility(visibility);
    }

    // 开始动画
    private void startAnim() {

        List<View> views = new ArrayList<>();
        views.add(mQQView);
        views.add(mQzoneView);
        views.add(mWeChatView);
        views.add(mWeChatSNSView);
        views.add(mSinaView);
        startAnimSet(views);

        views.clear();
        views.add(mViewSourceView);
        views.add(mNightView);
        views.add(mFontSettingView);
        views.add(mBrowseriew);
        views.add(mLinkView);
        startAnimSet(views);
        views.clear();


    }

    // 开始动画效果
    private void startAnimSet(List<View> views) {
        long afterTime = 100;
        for (View view : views) {
            afterTime += 100;

            AnimationSet set = new AnimationSet(false);
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0);
            animation.setDuration(800);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setStartOffset(afterTime);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1.0f);
            alphaAnimation.setDuration(300);
            alphaAnimation.setStartOffset(afterTime);

            set.addAnimation(animation);
            set.addAnimation(alphaAnimation);
            view.startAnimation(set);
        }
    }


    /**
     * 调用友盟分享
     */
    protected void share(SHARE_MEDIA type) {
        if (mOnShareClickListener != null) {
            mOnShareClickListener.onShare(this);
        }

        try {

            UMShareAPI umShareAPI = UMShareAPI.get(getContext());

            // fix bug #536 没有安装应用
            if (getContext() instanceof Activity && !umShareAPI.isInstall((Activity) getContext(), type)) {
                UICompat.failed(getContext(), "请安装" + type);
                return;
            }

            mShareAction.setPlatform(type);
            mShareAction.share();
        } catch (Exception e) {
            CrashReport.postCatchedException(e);
        }
    }

    @OnClick({R2.id.tv_share_wechat, R2.id.tv_share_wechat_sns, R2.id.tv_share_qq, R2.id.tv_share_qzone, R2.id.tv_share_sina, R2.id.tv_share_source, R2.id.tv_share_browser, R2.id.tv_share_link, R2.id.tv_share_night})
    void onShareClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_share_wechat)
            share(SHARE_MEDIA.WEIXIN);
        if (id == R.id.tv_share_wechat_sns)
            share(SHARE_MEDIA.WEIXIN_CIRCLE);
        if (id == R.id.tv_share_qq)
            share(SHARE_MEDIA.QQ);
        if (id == R.id.tv_share_qzone)
            share(SHARE_MEDIA.QZONE);
        if (id == R.id.tv_share_sina)
            share(SHARE_MEDIA.SINA);
        if (id == R.id.tv_share_source)
            onViewSourceClick();
        if (id == R.id.tv_share_browser)
            onBrowserViewClick();
        if (id == R.id.tv_share_link)
            onLinkClick();
        if (id == R.id.tv_share_night)
            onNightClick();

        dismiss();

    }

    protected void onNightClick() {
        ThemeCompat.switchNightMode();
        showNightText();
        dismiss();
    }

    private void showNightText() {
        if (ThemeCompat.isNight()) {
            mNightView.setText(R.string.day_mode);


        } else {
            mNightView.setText(R.string.night_mode);
        }
    }


    protected String getUrl() {
        if (getActivity() instanceof OnShareListener) {
            return ((OnShareListener) getActivity()).getWebUrl();
        }
        return getArguments() == null ? null : getArguments().getString("shareUrl");
    }

    // 查看原文
    protected void onViewSourceClick() {
        if (getContext() == null) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("cnblogs://web"));
        intent.putExtra("url", getUrl());
        intent.putExtra("from", "blog");
        ResolveInfo resolveInfo = getContext().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null) {
            getContext().startActivity(intent);
        }
    }

    // 用浏览器打开
    protected void onBrowserViewClick() {
        if (getUrl() == null) return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getUrl()));
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 复制链接
    protected void onLinkClick() {
        if (getUrl() == null) return;
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager == null) return;
        clipboardManager.setPrimaryClip(ClipData.newPlainText("url", getUrl()));
        UICompat.toastInCenter(getContext(), getString(R.string.copy_link_success));
    }

    // 取消
    @OnClick(R2.id.btn_share_cancel)
    void onCancelClick() {
        dismiss();
    }

    // 字体设置
    @OnClick(R2.id.tv_share_font)
    void onFontSettingClick() {
        dismiss();
        AppRoute.routeToFontSetting(getContext());
    }

}
