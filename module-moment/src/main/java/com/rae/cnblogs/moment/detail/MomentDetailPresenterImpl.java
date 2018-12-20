package com.rae.cnblogs.moment.detail;

import android.text.TextUtils;

import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.sdk.event.PostMomentEvent;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闪存详情
 * Created by ChenRui on 2017/11/2 0002 16:59.
 */
public class MomentDetailPresenterImpl extends BasicPresenter<IMomentDetailContract.View> implements IMomentDetailContract.Presenter {

    private IMomentApi mMomentApi;
    private final IFriendsApi mFriendApi;
    private FriendsInfoBean mBloggerInfo;
    private boolean mRefresh;

    public MomentDetailPresenterImpl(IMomentDetailContract.View view) {
        super(view);
        mMomentApi = CnblogsApiFactory.getInstance(getContext()).getMomentApi();
        mFriendApi = CnblogsApiFactory.getInstance(getContext()).getFriendApi();
    }

    @Override
    protected void onStart() {
        MomentBean momentInfo = getView().getMomentInfo();

        // 如果自带有评论就不用再发起请求了
        if (Rx.isEmpty(momentInfo.getCommentList()) || mRefresh) {
            loaMomentDetail(momentInfo);
        } else {
            notifyCommentSuccess(momentInfo.getCommentList());
        }

        boolean isLogin = UserProvider.getInstance().isLogin();
        if (!isLogin) {
            getView().onNotLogin();
            return;
        }

        // 加载博主信息
        AndroidObservable
                .create(mFriendApi.getFriendsInfo(getView().getBlogApp()))
                .with(this)
                .subscribe(new ApiDefaultObserver<FriendsInfoBean>() {
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

    /**
     * 加载闪存详情
     */
    private void loaMomentDetail(MomentBean momentInfo) {
        // 加载详情里面的
        AndroidObservable
                .create(mMomentApi.getMomentDetail(
                        momentInfo.getUserAlias(),
                        momentInfo.getId(),
                        System.currentTimeMillis()))
                .with(this)
                .subscribe(new ApiDefaultObserver<MomentBean>() {
                    @Override
                    protected void onError(String message) {
                        // 加载失败后，默认加载
                        loadComments(getView().getMomentInfo());
                    }

                    @Override
                    protected void accept(MomentBean momentBean) {
                        if (Rx.isEmpty(momentBean.getCommentList())) {
                            loadComments(getView().getMomentInfo());
                        } else {
                            getView().onLoadComments(momentBean.getCommentList(), false);
                        }
                    }
                });
    }

    /**
     * 加载评论
     */
    private void loadComments(MomentBean momentInfo) {
        AndroidObservable
                .create(mMomentApi.getMomentSingleComments(
                        momentInfo.getId(),
                        momentInfo.getUserAlias(),
                        System.currentTimeMillis()))
                .with(this)
                .subscribe(new ApiDefaultObserver<List<MomentCommentBean>>() {
                    @Override
                    protected void onError(String message) {
                        getView().onEmptyComment(message);
                    }

                    @Override
                    protected void accept(List<MomentCommentBean> momentCommentBeans) {
                        notifyCommentSuccess(momentCommentBeans);
                    }
                });
    }

    /**
     * 回调成功
     */
    private void notifyCommentSuccess(List<MomentCommentBean> momentCommentBeans) {
        if (Rx.isEmpty(momentCommentBeans)) {
            getView().onEmptyComment(null);
            return;
        }

        // 判断是否还有更多评论
        MomentCommentBean commentBean = momentCommentBeans.get(momentCommentBeans.size() - 1);
        boolean hasMore = "more".equals(commentBean.getId());
        if (hasMore)
            momentCommentBeans.remove(commentBean);

        getView().onLoadComments(momentCommentBeans, hasMore);
    }

    @Override
    public void refresh() {
        mRefresh = true;
        start();
    }

    @Override
    public void loadMore() {
        if (!UserProvider.getInstance().isLogin()) {
            getView().onLoadMoreNotLogin();
            return;
        }
        // 目前没有发现有多页的情况，就重新刷新当前页面
        refresh();
    }

    @Override
    public void postComment(String ingId, String userId, String commentId, String content) {

        AppMobclickAgent.onClickEvent(getContext(), "MOMENT_COMMENT");

        AndroidObservable.create(mMomentApi.postComment(ingId, userId, commentId, content))
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        CrashReport.postCatchedException(new CnblogsApiException("闪存评论发生异常！", e));
                    }

                    @Override
                    protected void onError(String message) {
                        getView().onPostCommentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        getView().onPostCommentSuccess();
                    }
                });
    }

    @Override
    public void follow() {
        if (mBloggerInfo == null) {
            getView().onFollowFailed(null);
            return;
        }
        Observable<Empty> observable;
        if (mBloggerInfo.isFollowed()) {
            observable = mFriendApi.unFollow(mBloggerInfo.getUserId());
        } else {
            observable = mFriendApi.follow(mBloggerInfo.getUserId());
        }

        AndroidObservable
                .create(observable)
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
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
                        // 通知更新用户信息
                        EventBus.getDefault().post(new UserInfoChangedEvent());
                    }
                });
    }

    @Override
    public boolean isFollowed() {
        return mBloggerInfo != null && mBloggerInfo.isFollowed();
    }

    @Override
    public void deleteComment(String commentId) {
        AndroidObservable.create(mMomentApi.deleteMomentComment(commentId))
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onDeleteCommentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        // 重新加载数据
                        loaMomentDetail(getView().getMomentInfo());
                    }
                });
    }

    @Override
    public void deleteMoment() {
        String ingId = getView().getMomentInfo().getId();
        if (!UserProvider.getInstance().isLogin()) {
            getView().onDeleteMomentFailed("没有登录");
            return;
        }
        if (TextUtils.isEmpty(ingId)) {
            getView().onDeleteMomentFailed("闪存ID为空");
            return;
        }

        AndroidObservable.create(mMomentApi.deleteMoment(ingId))
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onDeleteMomentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        PostMomentEvent postMomentEvent = new PostMomentEvent();
                        postMomentEvent.setDeleted(true);
                        EventBus.getDefault().post(postMomentEvent);
                        getView().onDeleteMomentSuccess();
                    }
                });
    }
}
