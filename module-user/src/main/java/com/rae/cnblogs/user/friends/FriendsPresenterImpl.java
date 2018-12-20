package com.rae.cnblogs.user.friends;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by rae on 2018/7/23.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
class FriendsPresenterImpl extends BasicPresenter<FriendsContract.View> implements FriendsContract.Presenter {

    private PageObservable<UserInfoBean> mPageObservable;
    private IFriendsApi mFriendApi;
    private String mType;

    @Nullable
    protected CharSequence mKeyword;

    FriendsPresenterImpl(final FriendsContract.View view, String type) {
        super(view);
        mType = type;
        mFriendApi = CnblogsApiFactory.getInstance(view.getContext()).getFriendApi();
        mPageObservable = new PageObservable<UserInfoBean>(view, this) {
            @Override
            protected Observable<List<UserInfoBean>> onCreateObserver(int page) {
                // 优先搜索
                if (!TextUtils.isEmpty(mKeyword)) {
                    Log.i("Rae", "搜索：" + mKeyword);
                    return mFriendApi.searchFriends(mKeyword.toString(), page);
                }

                switch (mType) {
                    case "fans":
                        return mFriendApi.getFansList(getView().getBlogApp(), page);
                    default:
                        return mFriendApi.getFollowList(getView().getBlogApp(), page);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        mPageObservable.start();
    }

    public void onLoadMore() {
        mPageObservable.loadMore();
    }

    @Override
    public void onSearch(CharSequence text) {
        // 取消上一次搜索
        mKeyword = text;
        onStart();
    }

    @Override
    public void unFollow(final UserInfoBean m) {
        // 取消关注
        Observable<Empty> observable = createFollowObservable(m, true);
        AndroidObservable
                .create(observable)
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onFollowError(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        m.setHasFollow(false);
                        getView().onUnFollowSuccess();
                        // 通知用户信息发生改变
                        EventBus.getDefault().post(new UserInfoChangedEvent());
                    }
                });
    }

    @Override
    public void follow(final UserInfoBean m) {
        // 加关注
        Observable<Empty> observable = createFollowObservable(m, false);
        AndroidObservable.create(observable)
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onFollowError("添加关注失败，可能你已经关注过" + m.getDisplayName());
                    }

                    @Override
                    protected void accept(Empty empty) {
                        m.setHasFollow(true);
                        getView().onFollowSuccess();
                        // 通知用户信息发生改变
                        EventBus.getDefault().post(new UserInfoChangedEvent());
                    }
                });
    }

    /**
     * 创建关注/取消关注观察者
     *
     * @param hasFollow 是否已经关注
     */
    private Observable<Empty> createFollowObservable(UserInfoBean m, boolean hasFollow) {
        Observable<UserInfoBean> userObservable;

        if (TextUtils.isEmpty(m.getUserId())) {
            userObservable = mFriendApi
                    .getFriendsInfo(m.getBlogApp())
                    .map(new Function<FriendsInfoBean, UserInfoBean>() {
                        @Override
                        public UserInfoBean apply(FriendsInfoBean friendsInfoBean) {
                            return friendsInfoBean;
                        }
                    });
        } else {
            userObservable = Observable.just(m);
        }

        if (hasFollow) {
            return userObservable.flatMap(new Function<UserInfoBean, ObservableSource<Empty>>() {
                @Override
                public ObservableSource<Empty> apply(UserInfoBean userInfoBean) {
                    return mFriendApi.unFollow(userInfoBean.getUserId());
                }
            });
        }
        return userObservable.flatMap(new Function<UserInfoBean, ObservableSource<Empty>>() {
            @Override
            public ObservableSource<Empty> apply(UserInfoBean userInfoBean) {
                return mFriendApi.follow(userInfoBean.getUserId());
            }
        });
    }
}
