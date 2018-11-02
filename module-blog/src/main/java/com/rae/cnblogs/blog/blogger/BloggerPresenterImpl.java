package com.rae.cnblogs.blog.blogger;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;

import io.reactivex.Observable;

/**
 * 博主
 * Created by ChenRui on 2017/2/24 0024 16:25.
 */
public class BloggerPresenterImpl extends BasicPresenter<BloggerContract.View> implements BloggerContract.Presenter {

    private final IFriendsApi mFriendApi;
    private FriendsInfoBean mBloggerInfo;

    public BloggerPresenterImpl(BloggerContract.View view) {
        super(view);
        mFriendApi = CnblogsApiFactory.getInstance(getContext()).getFriendApi();
    }


    @Override
    public void destroy() {

    }

    @Override
    protected void onStart() {
        if (!UserProvider.getInstance().isLogin()) {
            getView().onLoginExpired();
            return;
        }
        AndroidObservable.create(mFriendApi.getFriendsInfo(getView().getBlogApp())).subscribe(new ApiDefaultObserver<FriendsInfoBean>() {
            @Override
            protected void onError(String msg) {
                getView().onLoadBloggerInfoFailed(msg);
            }

            @Override
            protected void accept(FriendsInfoBean friendsInfoBean) {
                mBloggerInfo = friendsInfoBean;
                getView().onLoadBloggerInfo(friendsInfoBean);
            }
        });
    }

    @Override
    public void doFollow() {
        if (mBloggerInfo == null) {
            getView().onFollowFailed(getContext().getString(R.string.tips_blogger_follow_not_info));
            return;
        }
        Observable<Empty> observable;
        if (mBloggerInfo.isFollowed()) {
            observable = mFriendApi.unFollow(mBloggerInfo.getUserId());
        } else {
            observable = mFriendApi.follow(mBloggerInfo.getUserId());
        }

        AndroidObservable.create(observable).subscribe(new ApiDefaultObserver<Empty>() {
            @Override
            protected void onError(String message) {
                getView().onFollowFailed(message);
            }

            @Override
            protected void accept(Empty empty) {
                if (mBloggerInfo != null) {
                    mBloggerInfo.setFollowed(!mBloggerInfo.isFollowed());
                }
                getView().onFollowSuccess();
            }
        });
    }

    @Override
    public boolean isFollowed() {
        return mBloggerInfo != null && mBloggerInfo.isFollowed();
    }

}
