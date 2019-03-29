package com.rae.cnblogs.blog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.RaeTabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jcodecraeer.xrecyclerview.AppBarStateChangeListener;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.GlideApp;
import com.rae.cnblogs.blog.blogger.BloggerContract;
import com.rae.cnblogs.blog.blogger.BloggerPresenterImpl;
import com.rae.cnblogs.blog.fragment.BloggerListFragment;
import com.rae.cnblogs.blog.fragment.FeedListFragment;
import com.rae.cnblogs.blog.fragment.MultipleTypeBlogListFragment;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;
import com.rae.cnblogs.theme.ThemeCompat;
import com.rae.cnblogs.widget.RaeSkinDesignTabLayout;
import com.rae.swift.app.RaeFragmentAdapter;

import org.greenrobot.eventbus.EventBus;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

/**
 * blogger info
 * Created by ChenRui on 2017/2/9 0009 10:02.
 */
@Route(path = AppRoute.PATH_BLOGGER)
public class BloggerActivity extends SwipeBackBasicActivity implements BloggerContract.View, RaeSkinDesignTabLayout.OnTabSelectedListener {

    @BindView(R2.id.img_background)
    ImageView mBackgroundView;

    @BindView(R2.id.img_blog_avatar)
    ImageView mAvatarView;

    @BindView(R2.id.tv_blogger_name)
    TextView mBloggerNameView;

    @BindView(R2.id.tv_follow_count)
    TextView mFollowCountView;

    @BindView(R2.id.tv_fans_count)
    TextView mFansCountView;

    @BindView(R2.id.tv_age)
    TextView mSnsAgeView;

    @BindView(R2.id.btn_blogger_follow)
    Button mFollowView;

//    @BindView(R2.id.tool_bar)
//    Toolbar mToolbar;

    @BindView(R2.id.vp_blogger)
    ViewPager mViewPager;

    @BindView(R2.id.tab_category)
    RaeSkinDesignTabLayout mTabLayout;

    @BindView(R2.id.layout_account_fans)
    View mFansLayout;

    @BindView(R2.id.layout_account_follow)
    View mFollowLayout;

    @BindView(R2.id.tv_title_1)
    TextView mTitleView;

    @BindView(R2.id.app_bar)
    AppBarLayout mAppBarLayout;

//    @BindView(R2.id.view_bg_holder)
//    View mBloggerBackgroundView;

//    @BindView(R2.id.pb_blogger_follow)
//    View mFollowProgressBar;

    @BindView(R2.id.tv_blogger_introduce)
    TextView mIntroduceView;


    String mBlogApp;

    @Nullable
    private FriendsInfoBean mUserInfo;
    private BloggerContract.Presenter mBloggerPresenter;
    private FeedListFragment mFeedListFragment;
    private MultipleTypeBlogListFragment mBlogListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fm_blogger_info);
        ButterKnife.bind(this);
        mFollowView.setEnabled(false);

        mBlogApp = getIntent().getStringExtra("blogApp");

        if (mBlogApp == null) {
            UICompat.failed(this, "BlogApp为空！");
            finish();
            return;
        }

        RaeFragmentAdapter adapter = new RaeFragmentAdapter(getSupportFragmentManager());

        mFeedListFragment = FeedListFragment.newInstance(getBlogApp());
        mBlogListFragment = BloggerListFragment.newInstance(getBlogApp());


        adapter.add(getString(R.string.feed), mFeedListFragment);
        adapter.add(getString(R.string.blog), mBlogListFragment);

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.addOnTabSelectedListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            final Animation mAnimation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                Log.i("rae", "状态改变：" + state);
                mAnimation.setDuration(800);

                if (state == State.COLLAPSED) {
                    ThemeCompat.refreshStatusColor(BloggerActivity.this, true);
                    setHomeAsUpIndicator(R.drawable.ic_back);
                    mTitleView.setVisibility(View.VISIBLE);
                    mTitleView.clearAnimation();
                    mTitleView.startAnimation(mAnimation);
                } else {
                    mTitleView.clearAnimation();
                    mTitleView.setVisibility(View.GONE);
                    ThemeCompat.refreshStatusColor(BloggerActivity.this, false);
                    setHomeAsUpIndicator(R.drawable.ic_back_white);
                }
            }

            void setHomeAsUpIndicator(int homeAsUpIndicator) {
                if (getSupportActionBar() != null)
                    getSupportActionBar().setHomeAsUpIndicator(homeAsUpIndicator);
            }
        });


        // 获取博主信息
        mBloggerPresenter = new BloggerPresenterImpl(this);
        mBloggerPresenter.start();

    }

    @Override
    protected void onDestroy() {
        if (mBloggerPresenter != null)
            mBloggerPresenter.destroy();
        super.onDestroy();
        mTabLayout.removeOnTabSelectedListener(this);
    }

    //    @Override
    public void onLoadBloggerInfo(final FriendsInfoBean userInfo) {
        mUserInfo = userInfo;
        mFansLayout.setClickable(true);
        mFollowLayout.setClickable(true);
        mFollowView.setEnabled(true);
        if (!TextUtils.isEmpty(userInfo.getIntroduce())) {
            mIntroduceView.setText(userInfo.getIntroduce());
        }

        // 园龄
        if (!TextUtils.isEmpty(userInfo.getJoinDate())) {
            mSnsAgeView.setText(userInfo.getJoinDate());
        }

        // 如果是自己，则隐藏关注按钮
        if (UserProvider.getInstance().isLogin() && TextUtils.equals(mBlogApp, UserProvider.getInstance().getLoginUserInfo().getBlogApp())) {
            mFollowView.setVisibility(View.GONE);
        } else {
            mFollowView.setVisibility(View.VISIBLE);
        }

        GlideApp.with(this)
                .load(userInfo.getAvatar())
                .centerCrop()
                .placeholder(R.drawable.default_avatar_placeholder)
                .into(mAvatarView);

        showAvatar(userInfo.getBlogApp(), userInfo.getAvatar());

        mBloggerNameView.setText(userInfo.getDisplayName());
        mTitleView.setText(userInfo.getDisplayName());
        mFansCountView.setText(userInfo.getFans());
        mFollowCountView.setText(userInfo.getFollows());
        mFollowView.setText(userInfo.isFollowed() ? R.string.cancel_follow : R.string.following);
    }

    @Override
    public void onFollowSuccess() {
        mFollowView.setEnabled(true);
        mFollowView.setText(mBloggerPresenter.isFollowed() ? R.string.cancel_follow : R.string.following);
        setResult(RESULT_OK);
        // 发送通知
        EventBus.getDefault().post(new UserInfoChangedEvent());
    }

    @Override
    public void onFollowFailed(String message) {
        mFollowView.setEnabled(true);
        mFollowView.setText(R.string.following);
        UICompat.toast(this, message);
    }

    private void showAvatar(String blogApp, final String url) {
        if (TextUtils.isEmpty(url) || url.endsWith("simple_avatar.gif")) return;
        // 封面图
        final String coverUrl = String.format("https://files.cnblogs.com/files/%s/app-cover.bmp", blogApp);
        int alphaColor = ContextCompat.getColor(getContext(), R.color.blogger_image_alpha_color);
        // 自定义头像
        GlideApp.with(this)
                .load(coverUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mBackgroundView.setContentDescription(coverUrl);
                        return false;
                    }
                })
                .error(GlideApp
                        .with(this)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(new ColorFilterTransformation(alphaColor)))
                )
                .apply(RequestOptions.bitmapTransform(new ColorFilterTransformation(alphaColor)))
                .into(mBackgroundView);

    }

    //    @Override
    public String getBlogApp() {
        return URLDecoder.decode(mBlogApp);
    }

    @Override
    public void onLoadBloggerInfoFailed(String msg) {
        UICompat.toast(this, msg);
    }

    /**
     * 粉丝
     */
    @OnClick(R2.id.layout_account_fans)
    public void onFansClick() {
        if (mUserInfo == null) return;
        AppRoute.routeToFans(this.getContext(), mUserInfo.getDisplayName(), mUserInfo.getBlogApp());
    }


    /**
     * 关注
     */
    @OnClick(R2.id.layout_account_follow)
    public void onFollowClick() {
        if (mUserInfo == null) return;
        AppRoute.routeToFollow(this.getContext(), mUserInfo.getDisplayName(), mUserInfo.getBlogApp());
    }

    @OnClick(R2.id.btn_blogger_follow)
    public void onFollowButtonClick() {
        if (mUserInfo == null) return;
        mFollowView.setEnabled(false);
        mFollowView.setText(R.string.loading);
        mBloggerPresenter.doFollow();
    }


    /**
     * 头像点击
     */
    @OnClick({R2.id.img_background, R2.id.img_blog_avatar})
    public void onAvatarClick(View view) {
        if (mUserInfo == null) return;
        ArrayList<String> images = new ArrayList<>();
        if (view.getId() == R.id.img_background && !TextUtils.isEmpty(view.getContentDescription())) {
            images.add(view.getContentDescription().toString());
        }
        images.add(mUserInfo.getAvatar());
        AppRoute.routeToImagePreview(this, images, 0);
    }


//    @OnClick(R2.id.tool_bar)
//    public void onTitleClick() {
//        takeScrollToTop(mViewPager.getCurrentItem());
//    }

    /**
     * 返回顶部
     */
    private void takeScrollToTop(int position) {
        if (position == 0)
            mFeedListFragment.scrollToTop();
        if (position == 1)
            mBlogListFragment.scrollToTop();
    }

    @Override
    public void onNoMoreData() {

    }

    @Override
    public void onEmptyData(String msg) {

    }

    @Override
    public void onLoadData(List<BlogCommentBean> data) {

    }

    @Override
    public void onLoginExpired() {

    }

    @Override
    public void onTabSelected(RaeTabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(RaeTabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(RaeTabLayout.Tab tab) {
        takeScrollToTop(tab.getPosition());
    }
}