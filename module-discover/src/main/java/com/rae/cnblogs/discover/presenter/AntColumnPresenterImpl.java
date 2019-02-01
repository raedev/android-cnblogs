package com.rae.cnblogs.discover.presenter;

import com.antcode.sdk.AntCodeSDK;
import com.antcode.sdk.IAntColumnApi;
import com.antcode.sdk.model.AntColumnInfo;
import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.discover.AntCodeBasicPresenter;

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
        mPageObservable = new PageObservable<AntColumnInfo>(view, this) {
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
