package com.rae.cnblogs.discover.auth;

import android.support.annotation.Nullable;

import com.antcode.sdk.AntCodeSDK;
import com.antcode.sdk.IAntUserApi;
import com.antcode.sdk.model.AntAppConfigInfo;
import com.antcode.sdk.model.AntEmptyInfo;
import com.rae.cnblogs.discover.AntCodeBasicPresenter;
import com.rae.cnblogs.discover.AntSdkDefaultObserver;

public class AntUserAuthPresenterImpl extends AntCodeBasicPresenter<AntUserAuthContract.View> implements AntUserAuthContract.Presenter {

    private IAntUserApi mAntUserApi;
    @Nullable
    private AntAppConfigInfo mAntAppConfigInfo;

    public AntUserAuthPresenterImpl(AntUserAuthContract.View view) {
        super(view);
        mAntUserApi = AntCodeSDK.getInstance().getUserApi();
    }

    @Override
    protected void onStart() {

    }

    @Override
    public void send() {
        String phone = getView().getPhoneNumber();
        // 发送短信验证码
        mAntUserApi.sendSms(phone, IAntUserApi.SMS_TYPE_TOKEN).with(this).subscribe(new AntSdkDefaultObserver<AntEmptyInfo>() {
            @Override
            protected void onError(String message) {
                getView().onSendError(message);
            }

            @Override
            protected void accept(AntEmptyInfo antEmptyInfo) {
                getView().onSendSuccess();
            }
        });
    }

    @Nullable
    public AntAppConfigInfo getAntAppConfigInfo() {
        return mAntAppConfigInfo;
    }
}
