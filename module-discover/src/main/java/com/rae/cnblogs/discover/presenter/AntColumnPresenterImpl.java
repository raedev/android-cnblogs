package com.rae.cnblogs.discover.presenter;

import com.antcode.sdk.AntCodeSDK;
import com.antcode.sdk.IAntColumnApi;
import com.antcode.sdk.model.AntColumnInfo;
import com.antcode.sdk.model.AntUserInfo;
import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.discover.AntCodeBasicPresenter;
import com.rae.cnblogs.discover.AntPageObservable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.AndroidObservable;

public class AntColumnPresenterImpl extends AntCodeBasicPresenter<IAntColumnContract.View> implements IAntColumnContract.Presenter {

    private final IAntColumnApi mColumnApi;
    private PageObservable<AntColumnInfo> mPageObservable;

    public AntColumnPresenterImpl(IAntColumnContract.View view) {
        super(view);
        AntCodeSDK antCodeSDK = AntCodeSDK.getInstance();
        mColumnApi = antCodeSDK.getColumnApi();
        mPageObservable = new AntPageObservable<AntColumnInfo>(view, this) {
            @Override
            protected Observable<List<AntColumnInfo>> onCreateObserver(int page) {
                AndroidObservable<List<AntColumnInfo>> observable;
                if (getView().getType() == IAntColumnContract.TYPE_MY) {
                    observable = mColumnApi.getUserColumns(page);
                } else {
                    observable = mColumnApi.getColumns(page, null);
                }
                return observable.with(AntColumnPresenterImpl.this);
            }
        };

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void OnLoginChanged(AntUserInfo userInfo) {
        // 登录状态改变，重新加载数据
        onStart();
    }

    @Override
    protected void onStart() {
        mPageObservable.start();
    }

    @Override
    public void loadMore() {
        mPageObservable.loadMore();
    }
}
