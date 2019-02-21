package com.rae.cnblogs.discover.presenter;

import android.support.annotation.Nullable;

import com.antcode.sdk.AntCodeSDK;
import com.antcode.sdk.IAntColumnApi;
import com.antcode.sdk.model.AntColumnInfo;
import com.antcode.sdk.model.AntEmptyInfo;
import com.rae.cnblogs.discover.AntCodeBasicPresenter;
import com.rae.cnblogs.discover.AntSdkDefaultObserver;

public class AntColumnDetailPresenterImpl extends AntCodeBasicPresenter<IAntColumnDetailContract.View> implements IAntColumnDetailContract.Presenter {

    private final IAntColumnApi mColumnApi;
    @Nullable
    private AntColumnInfo mColumnInfo;

    public AntColumnDetailPresenterImpl(IAntColumnDetailContract.View view) {
        super(view);
        AntCodeSDK antCodeSDK = AntCodeSDK.getInstance();
        mColumnApi = antCodeSDK.getColumnApi();
    }

    @Override
    protected void onStart() {
        String columnId = getView().getColumnId();
        mColumnApi.getColumnDetail(columnId).with(this).subscribe(new AntSdkDefaultObserver<AntColumnInfo>() {
            @Override
            protected void onError(String message) {
                getView().onLoadDataError(message);
            }

            @Override
            protected void accept(AntColumnInfo columnInfo) {
                mColumnInfo = columnInfo;
                getView().onLoadColumnDetail(columnInfo);
            }
        });

        // 查询是否已经订阅
        mColumnApi.getUserColumnDetail(columnId)
                .with(this)
                .subscribe(new AntSdkDefaultObserver<AntColumnInfo>() {
                    @Override
                    protected void onError(String message) {
                        // 不处理
                        getView().onColumnSubscribe(false);
                    }

                    @Override
                    protected void accept(AntColumnInfo antColumnInfo) {
                        getView().onColumnSubscribe(true);
                    }
                });
    }

    @Override
    public void subscribe() {
        if (mColumnInfo == null) {
            getView().onSubscribeError("数据尚未加载完毕，请稍后再试");
            return;
        }
        mColumnApi.subscribe(String.valueOf(mColumnInfo.getId()))
                .with(this)
                .subscribe(new AntSdkDefaultObserver<AntEmptyInfo>() {
                    @Override
                    protected void onError(String message) {
                        getView().onSubscribeError(message);
                    }

                    @Override
                    protected void accept(AntEmptyInfo antEmptyInfo) {
                        getView().onSubscribeSuccess();
                    }
                });
    }
}
