package com.rae.cnblogs.home.system;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IRaeServerApi;
import com.rae.cnblogs.sdk.bean.SystemMessageBean;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;

import java.util.List;

/**
 * Created by rae on 2018/5/15.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SystemMessagePresenterImpl extends BasicPresenter<SystemMessageContract.View> implements SystemMessageContract.Presenter {
    private final IRaeServerApi mRaeServerApi;
    private final CnblogAppConfig mConfig;

    public SystemMessagePresenterImpl(SystemMessageContract.View view) {
        super(view);
        mRaeServerApi = CnblogsApiFactory.getInstance(getContext()).getRaeServerApi();
        mConfig = CnblogAppConfig.getInstance(getContext());
    }

    @Override
    protected void onStart() {
        AndroidObservable.create(mRaeServerApi.getMessages())
                .with(this)
                .subscribe(new ApiDefaultObserver<List<SystemMessageBean>>() {
                    @Override
                    protected void onError(String message) {
                        getView().onEmptyData(message);
                    }

                    @Override
                    protected void accept(List<SystemMessageBean> data) {
                        getView().onLoadData(data);

                    }
                });

        // 更新消息数量
        AndroidObservable.create(mRaeServerApi.getMessageCount())
                .with(this)
                .subscribe(new ApiDefaultObserver<Integer>() {
                    @Override
                    protected void onError(String message) {

                    }

                    @Override
                    protected void accept(Integer integer) {
                        mConfig.setMessageCount(integer);
                    }
                });
    }
}
