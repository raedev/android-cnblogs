package com.rae.cnblogs.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kyleduo.switchbutton.SwitchButton;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.home.mine.MineContract;
import com.rae.cnblogs.home.mine.MinePresenterImpl;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.theme.ThemeCompat;
import com.rae.cnblogs.widget.R;

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
    @BindView(R2.id.tv_no_login)
    View mNoLoginTextView;
    @BindView(R2.id.ll_follow_fans)
    View mFansAndFollowLayout;
    @BindView(R2.id.img_system_message_badge)
    View mSystemMessageBadgeView;
    @BindView(R2.id.img_feedback_badge)
    View mFeedbackBadgeView;
    @BindView(R2.id.sb_night_mode)
    SwitchButton mNightModeButton;

    private MineContract.Presenter mPresenter;


    @Override
    protected int getLayoutId() {
        return R2.layout.fm_mine;
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
        mNightModeButton.setCheckedNoEvent(ThemeCompat.isNight());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 夜间模式切换
        mNightModeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ThemeCompat.switchNightMode();
            }
        });
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
        mNoLoginTextView.setVisibility(View.GONE);
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
        mDisplayNameView.setVisibility(View.GONE);
        mNoLoginTextView.setVisibility(View.VISIBLE);
        mFansAndFollowLayout.setVisibility(View.GONE);
        mFansCountView.setText("0");
        mFollowCountView.setText("0");
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
    @OnClick({R2.id.img_blog_avatar, R2.id.tv_mine_name, R2.id.tv_no_login})
    public void onLoginClick() {
        if (mPresenter.isLogin())
            AppRoute.routeToBlogger(getContext(), UserProvider.getInstance().getLoginUserInfo().getBlogApp());
        else
            AppRoute.routeToLogin(getContext());
    }

    /**
     * 我的收藏
     */
    @OnClick(R2.id.ll_favorites)
    public void onFavoritesClick() {
        AppMobclickAgent.onClickEvent(getContext(), "Favorites");
        AppRoute.routeToFavorites(this.getActivity());
    }

    /**
     * 问题反馈
     */
    @OnClick(R2.id.ll_feedback)
    public void onFeedbackClick() {
        mFeedbackBadgeView.setVisibility(View.INVISIBLE);
        AppMobclickAgent.onClickEvent(getContext(), "Feedback");
        AppRoute.routeToFeedback(getContext());
    }

    /**
     * 浏览记录
     */
    @OnClick(R2.id.ll_history)
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
     * 设置
     */
    @OnClick(R2.id.ll_setting)
    public void onSettingClick() {
        AppRoute.routeToSetting(this.getContext());
    }

    /**
     * 夜间模式
     */
    @OnClick(R2.id.ll_night)
    public void onNightClick() {
        AppMobclickAgent.onClickEvent(getContext(), "NightMode");
        mNightModeButton.performClick();
    }


    @Override
    public void onLoadFeedbackMessage(boolean hasNewMessage) {
        UICompat.setVisibility(mFeedbackBadgeView, hasNewMessage);
    }
}
