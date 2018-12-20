package com.rae.cnblogs.user.personal;

import android.util.Log;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.UUID;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class UserAvatarPresenterImpl extends BasicPresenter<UserAvatarContract.View> implements UserAvatarContract.Presenter {

    private IUserApi mUserApi;

    public UserAvatarPresenterImpl(UserAvatarContract.View view) {
        super(view);
        mUserApi = CnblogsApiFactory.getInstance(getContext()).getUserApi();
    }

    @Override
    protected void onStart() {
    }

    @Override
    public void upload() {
        File file = new File(getView().getUploadPath());
        String fileName = UUID.randomUUID().toString() + ".jpg";
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        // 上传图片
        AndroidObservable.create(mUserApi.uploadAvatarImage(fileName, fileName, fileReqBody))
                .with(this)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String url) throws Exception {
                        Log.i("rae", "图片上传成功：" + url);
                        return AndroidObservable.create(mUserApi.updateAvatar(0, 0, 300, 300, url)).with(UserAvatarPresenterImpl.this);
                    }
                })
                .subscribe(new ApiDefaultObserver<String>() {
                    @Override
                    protected void onError(String message) {
                        getView().onUploadFailed(message);
                    }

                    @Override
                    protected void accept(String s) {
                        // 头像上传成功，通知更新用户信息
                        EventBus.getDefault().post(new UserInfoChangedEvent());
                        getView().onUploadSuccess();
                    }
                });
    }
}
