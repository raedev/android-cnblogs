package com.rae.cnblogs.discover.presenter;

import android.support.annotation.Nullable;

import com.antcode.sdk.AntCodeSDK;
import com.antcode.sdk.IAntColumnApi;
import com.antcode.sdk.model.AntArticleInfo;
import com.antcode.sdk.model.AntColumnInfo;
import com.antcode.sdk.model.AntEmptyInfo;
import com.rae.cnblogs.discover.AntCodeBasicPresenter;
import com.rae.cnblogs.discover.AntPageObservable;
import com.rae.cnblogs.discover.AntSdkDefaultObserver;
import com.rae.cnblogs.discover.SubscribeColumnMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;

public class AntUserColumnDetailPresenterImpl extends AntCodeBasicPresenter<IAntUserColumnDetailContract.View> implements IAntUserColumnDetailContract.Presenter {

    private final IAntColumnApi mColumnApi;

    @Nullable
    private AntColumnInfo mColumnInfo;

    private AntPageObservable<AntArticleInfo> mPageObservable;

    public AntUserColumnDetailPresenterImpl(IAntUserColumnDetailContract.View view) {
        super(view);
        AntCodeSDK antCodeSDK = AntCodeSDK.getInstance();
        mColumnApi = antCodeSDK.getColumnApi();
        mPageObservable = new AntPageObservable<AntArticleInfo>(view, this) {
            @Override
            protected Observable<List<AntArticleInfo>> onCreateObserver(int page) {
                final String columnId = getView().getColumnId();
                return mColumnApi.getColumnArticles(columnId, page);
            }
        };
    }

    @Override
    protected void onStart() {
        final String columnId = getView().getColumnId();
        mColumnApi.getUserColumnDetail(columnId).with(this).subscribe(new AntSdkDefaultObserver<AntColumnInfo>() {
            @Override
            protected void onError(String message) {
                getView().onLoadDataError(message);
            }

            @Override
            protected void accept(AntColumnInfo columnInfo) {
                mColumnInfo = columnInfo;
                getView().onLoadColumnDetail(columnInfo);
            }

            @Override
            protected void onLoginExpired() {
                super.onLoginExpired();
                getView().onLoginExpired();
            }
        });
    }


    @Override
    public void loadData() {
        mPageObservable.start();
    }

    @Override
    public void loadMore() {
        mPageObservable.loadMore();
    }

    @Override
    public void unsubscribe() {
        mColumnApi.unsubscribe(getView().getColumnId())
                .with(this)
                .subscribe(new AntSdkDefaultObserver<AntEmptyInfo>() {
                    @Override
                    protected void onError(String message) {
                        getView().onUnsubscribeError(message);
                    }

                    @Override
                    protected void accept(AntEmptyInfo antEmptyInfo) {
                        EventBus.getDefault().post(new SubscribeColumnMessage(getView().getColumnId()));
                        getView().onUnsubscribeSuccess();
                    }
                });
    }
}
