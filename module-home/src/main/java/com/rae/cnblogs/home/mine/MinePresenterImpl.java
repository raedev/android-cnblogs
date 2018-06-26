package com.rae.cnblogs.home.mine;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.feedback.Comment;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;
import com.rae.cnblogs.sdk.CnblogsReportException;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.api.IRaeServerApi;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

/**
 * 我的
 * Created by rae on 2018/5/14.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class MinePresenterImpl extends BasicPresenter<MineContract.View> implements MineContract.Presenter {

    private final IRaeServerApi mRaeServerApi;
    private final IFriendsApi mFriendApi;
    private final IUserApi mUserApi;
    private CnblogAppConfig mConfig;

    public MinePresenterImpl(MineContract.View view) {
        super(view);
        mConfig = CnblogAppConfig.getInstance(getContext());
        CnblogsApiProvider provider = CnblogsApiFactory.getInstance(getContext());
        mUserApi = provider.getUserApi();
        mFriendApi = provider.getFriendApi();
        mRaeServerApi = provider.getRaeServerApi();
    }

    @Override
    protected void onStart() {
        loadFeedbackMessage();
        loadSystemMessage();
    }

    /**
     * 加载系统消息
     */
    private void loadSystemMessage() {
        AndroidObservable
                .create(mRaeServerApi.getMessageCount())
                .with(this)
                .subscribe(new ApiDefaultObserver<Integer>() {
                    @Override
                    protected void onError(String message) {

                    }

                    @Override
                    protected void accept(Integer value) {
                        int count = value == null ? 0 : value;
                        // 对比本地的消息数量
                        int dbCount = mConfig.getMessageCount();
                        boolean hasNew = dbCount != count;
                        getView().onLoadSystemMessage(count, hasNew);
                    }
                });
    }

    @Override
    public boolean isLogin() {
        return UserProvider.getInstance().isLogin();
    }

    @Override
    public void loadUserInfo() {
        UserInfoBean userInfo = UserProvider.getInstance().getLoginUserInfo();

        // 用户没有登录
        if (userInfo == null) {
            getView().onNotLogin();
            return;
        }

        getView().onLoadUserInfo(userInfo);

        // 加载粉丝关注数量
        AndroidObservable
                .create(mFriendApi.getFriendsInfo(userInfo.getBlogApp()))
                .with(this)
                .subscribe(new ApiDefaultObserver<FriendsInfoBean>() {
                    @Override
                    protected void onError(String message) {
                        getView().onLoadFansCount("0", "0");
                    }

                    @Override
                    protected void accept(FriendsInfoBean m) {
                        getView().onLoadFansCount(m.getFans(), m.getFollows());
                    }
                });

        // 刷新用户信息
        AndroidObservable
                .create(mUserApi.getUserInfo(userInfo.getBlogApp()))
                .with(this)
                .subscribe(new ApiDefaultObserver<UserInfoBean>() {
                    @Override
                    protected void onError(String message) {
                        // 不做处理
                    }

                    @Override
                    protected void onLoginExpired() {
                        // 登录过期
                        getView().onNotLogin();
                        getView().onLoginExpired();
                    }

                    @Override
                    protected void accept(UserInfoBean userInfoBean) {
                        // 更新用户信息
                        if (!TextUtils.isEmpty(userInfoBean.getUserId())) {
                            UserProvider.getInstance().setLoginUserInfo(userInfoBean);
                        }
                    }
                });
    }


    /**
     * 检查是否有新的意见反馈回复消息
     */
    private void loadFeedbackMessage() {
        try {
            final int originalCount = FeedbackThread.getInstance().getCommentsList().size();
            FeedbackThread.getInstance().sync(new FeedbackThread.SyncCallback() {
                @Override
                public void onCommentsSend(List<Comment> list, AVException e) {

                }

                @Override
                public void onCommentsFetch(List<Comment> list, AVException e) {
                    // 有新的回复消息
                    getView().onLoadFeedbackMessage(list.size() > originalCount);
                }
            });
        } catch (Exception e) {
            CrashReport.postCatchedException(new CnblogsReportException("意见反馈发生异常！", e));
        }
    }
}
