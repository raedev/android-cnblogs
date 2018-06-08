package com.rae.cnblogs.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.dialog.ShareDialogFragment;
import com.rae.cnblogs.home.mine.MineContract;
import com.rae.cnblogs.home.mine.MinePresenterImpl;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.theme.ThemeCompat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的
 * Created by ChenRui on 2017/1/19 00:13.
 */
@Route(path = AppRoute.PATH_FRAGMENT_MINE)
public class MineFragment extends BasicFragment implements MineContract.View {

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @BindView(R2.id.img_blog_avatar)
    ImageView mAvatarView;

    @BindView(R2.id.tv_mine_name)
    TextView mDisplayNameView;

    @BindView(R2.id.tv_follow_count)
    TextView mFollowCountView;
    @BindView(R2.id.tv_fans_count)
    TextView mFansCountView;

    @BindView(R2.id.ll_follow_fans)
    View mFansAndFollowLayout;
    @BindView(R2.id.img_system_message_badge)
    View mSystemMessageBadgeView;
    @BindView(R2.id.img_feedback_badge)
    View mFeedbackBadgeView;
//    @BindView(R2.id.sb_night_mode)
//    SwitchButton mNightModeButton;

    @BindView(R2.id.tv_night_mode)
    TextView mNightModeView;

    private MineContract.Presenter mPresenter;


    @Override
    protected int getLayoutId() {
        return R.layout.fm_mine;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MinePresenterImpl(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 夜间模式处理
        mNightModeView.setSelected(ThemeCompat.isNight());
    }


    @Override
    protected void onLoadData() {
        mPresenter.start();
        mPresenter.loadUserInfo();
    }

    @Override
    public void onLoadSystemMessage(int count, boolean hasNew) {
        UICompat.setVisibility(mSystemMessageBadgeView, hasNew);
    }

    @Override
    public void onLoadFansCount(String fans, String follows) {
        mFansCountView.setText(fans);
        mFollowCountView.setText(follows);
    }

    /**
     * 加载用户信息
     */
    @Override
    public void onLoadUserInfo(@NonNull UserInfoBean user) {
        mDisplayNameView.setVisibility(View.VISIBLE);
        mFansAndFollowLayout.setVisibility(View.VISIBLE);
        AppImageLoader.displayAvatar(user.getAvatar(), mAvatarView);
        mDisplayNameView.setText(user.getDisplayName());
    }

    @Override
    public void onLoginExpired() {
        UICompat.failed(getContext(), getString(com.rae.cnblogs.widget.R.string.login_expired));
        AppRoute.routeToLogin(getContext());
    }

    @Override
    public void onNotLogin() {
        // 没有登录的UI
        mAvatarView.setImageResource(R.drawable.boy);
        mFansAndFollowLayout.setVisibility(View.GONE);
        mFansCountView.setText("0");
        mFollowCountView.setText("0");
    }


    @OnClick(R2.id.tv_night_mode)
    public void onNightModeClick() {
        AppMobclickAgent.onClickEvent(getContext(), "NightMode");
        ThemeCompat.switchNightMode();
    }

    @OnClick(R2.id.layout_account_fans)
    public void onFansClick() {
        AppRoute.routeToFans(this.getContext(), getString(R.string.me), UserProvider.getInstance().getLoginUserInfo().getBlogApp());
    }

    @OnClick(R2.id.layout_account_follow)
    public void onFollowClick() {
        AppRoute.routeToFollow(this.getContext(), getString(R.string.me), UserProvider.getInstance().getLoginUserInfo().getBlogApp());
    }


    /**
     * 登录
     */
    @OnClick({R2.id.img_blog_avatar, R2.id.tv_mine_name})
    public void onLoginClick() {
        if (mPresenter.isLogin())
            AppRoute.routeToBlogger(getContext(), UserProvider.getInstance().getLoginUserInfo().getBlogApp());
        else
            AppRoute.routeToLogin(getContext());
    }

    /**
     * 我的收藏
     */
    @OnClick(R2.id.tv_favorites)
    public void onFavoritesClick() {
        AppMobclickAgent.onClickEvent(getContext(), "Favorites");
        AppRoute.routeToFavorites(this.getActivity());
    }

    /**
     * 浏览记录
     */
    @OnClick(R2.id.tv_history)
    public void onHistoryClick() {
        AppMobclickAgent.onClickEvent(getContext(), "History");
        AppRoute.routeToHistory(getContext());
    }


    /**
     * 系统消息
     */
    @OnClick(R2.id.ll_system_message)
    public void onSystemMessageClick() {
        AppMobclickAgent.onClickEvent(getContext(), "SystemMessage");
        mSystemMessageBadgeView.setVisibility(View.INVISIBLE);
        AppRoute.routeToSystemMessage(this.getContext());
    }

    /**
     * 字体设置
     */
    @OnClick(R2.id.ll_font_setting)
    public void onFontSettingClick() {
        AppRoute.routeToFontSetting(this.getContext());
    }

    /**
     * 意见反馈
     */
    @OnClick(R2.id.ll_feedback)
    public void onFeedbackClick() {
        mFeedbackBadgeView.setVisibility(View.INVISIBLE);
        AppMobclickAgent.onClickEvent(getContext(), "Feedback");
        AppRoute.routeToFeedback(getContext());
    }

    /**
     * 分享
     */
    @OnClick(R2.id.ll_share)
    public void onShareClick() {
        AppMobclickAgent.onClickEvent(getContext(), "ShareApp");
        String url = getString(R.string.share_app_url);
        String title = getString(R.string.share_app_title);
        String desc = getString(R.string.share_app_desc);
        ShareDialogFragment fragment = ShareDialogFragment.newInstance(url, title, desc, null, R.drawable.ic_share_app, false, false);
        fragment.show(getChildFragmentManager(), "shareApp");
    }

    /**
     * 好评
     */
    @OnClick(R2.id.ll_praises)
    public void onPraisesClick() {
        AppMobclickAgent.onClickEvent(getContext(), "Praises");
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_url))));
        } catch (Exception e) {
            UICompat.failed(getContext(), getString(R.string.praises_error));
        }
    }

    /**
     * 设置
     */
    @OnClick(R2.id.ll_setting)
    public void onSettingClick() {
        AppRoute.routeToSetting(this.getContext());
    }


    @Override
    public void onLoadFeedbackMessage(boolean hasNewMessage) {
        UICompat.setVisibility(mFeedbackBadgeView, hasNewMessage);
    }
}
