package com.rae.cnblogs.moment.main;

import android.text.TextUtils;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.event.PostMomentEvent;
import com.rae.cnblogs.sdk.event.UserInfoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * moment
 * Created by ChenRui on 2017/10/27 0027 10:57.
 */
public class MomentPresenterImpl extends BasicPresenter<IMomentContract.View> implements IMomentContract.Presenter {

    private IMomentApi mMomentApi;
    private int mReplyMeCount;
    private int mAtMeCount;
    private PageObservable<MomentBean> mPageObservable;


    public MomentPresenterImpl(IMomentContract.View view) {
        super(view);
        EventBus.getDefault().register(this);
        mMomentApi = CnblogsApiFactory.getInstance(getContext()).getMomentApi();
        mPageObservable = new PageObservable<MomentBean>(view, this) {
            @Override
            protected Observable<List<MomentBean>> onCreateObserver(int page) {
                return mMomentApi.getMoments(getView().getType(), page, System.currentTimeMillis());
            }
        };
    }

    @Override
    protected void onStart() {
        boolean isLogin = UserProvider.getInstance().isLogin();
        String type = getView().getType();
        // 没有登录
        if (!isLogin && !TextUtils.equals(type, IMomentApi.MOMENT_TYPE_ALL)) {
            getView().onLoginExpired();
            return;
        }
        //  登录情况
        if (isLogin) {
            onLoadData();
        }
        mPageObservable.start();
    }

    private void onLoadData() {
        // 查询回复我的数量
        AndroidObservable
                .create(mMomentApi.queryReplyCount(System.currentTimeMillis()))
                .with(this)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) {
                        mReplyMeCount = Rx.parseInt(s);
                        return AndroidObservable
                                .create(mMomentApi.queryAtMeCount(System.currentTimeMillis()))
                                .with(MomentPresenterImpl.this);
                    }
                })
                .subscribe(new ApiDefaultObserver<String>() {
                    @Override
                    protected void onError(String message) {
                        getView().onMessageCountChanged(mReplyMeCount, mAtMeCount);
                    }

                    @Override
                    protected void accept(String s) {
                        mAtMeCount = Rx.parseInt(s);
                        getView().onMessageCountChanged(mReplyMeCount, mAtMeCount);
                    }
                });
    }

    @Override
    public void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(this);
        mPageObservable.destroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserInfoEvent event) {
        // 重新加载数据
        start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostMomentEvent event) {
        // 重新加载数据
        if (event.isDeleted()) {
            start();
        }
    }

    @Override
    public void loadMore() {
        mPageObservable.loadMore();
    }
}
