package com.rae.cnblogs.discover.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.antcode.sdk.AntCodeSDK;
import com.antcode.sdk.AntSessionManager;
import com.antcode.sdk.IAntUserApi;
import com.antcode.sdk.model.AntTokenInfo;
import com.antcode.sdk.model.AntUserInfo;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.discover.AntSdkDefaultObserver;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.widget.CodeEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 输入验证码界面
 */
@Route(path = AppRoute.PATH_DISCOVER_SMS_CODE)
public class AntUserSmsCodeActivity extends SwipeBackBasicActivity implements CodeEditText.OnTextFinishListener {

    @BindView(R2.id.tv_hello)
    TextView mMessageView;

    @BindView(R2.id.et_sms_code)
    CodeEditText mCodeEditText;
    IAntUserApi mUserApi;
    private String mPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_user_smscode);
        setTitle(" ");
        mPhoneNumber = getIntent().getStringExtra("phone");
        mUserApi = AntCodeSDK.getInstance().getUserApi();
        mCodeEditText.setOnTextFinishListener(this);
    }


    @OnClick(R2.id.ll_contract)
    public void onContractClick() {
        AppRoute.routeToAntUserContract(this);
    }

    @Override
    public void onTextFinish(CharSequence text, int length) {
        mMessageView.setText("正在校验，请稍后...");
        mCodeEditText.setEnabled(false);

        // 校验Token
        mUserApi.getToken(mPhoneNumber, text.toString())
                .with(this)
                .flatMap(new Function<AntTokenInfo, ObservableSource<AntUserInfo>>() {
                    @Override
                    public ObservableSource<AntUserInfo> apply(AntTokenInfo antTokenInfo) throws Exception {
                        AntSessionManager.getDefault().setUserToken(antTokenInfo);
                        return mUserApi.getUserInfo().with(AntUserSmsCodeActivity.this);
                    }
                })
                .subscribe(new AntSdkDefaultObserver<AntUserInfo>() {
                    private void dismiss() {
                        mMessageView.setText(R.string.input_sms_code);
                        mCodeEditText.setEnabled(true);
                    }

                    @Override
                    protected void onError(String message) {
                        dismiss();
                        UICompat.failed(getContext(), message);
                        mCodeEditText.setText("");
                        UICompat.showSoftInputFromWindow(mCodeEditText);
                    }

                    @Override
                    protected void accept(AntUserInfo antUserInfo) {
                        dismiss();
                        AntSessionManager.getDefault().setUser(antUserInfo);
                        AppRoute.routeToAntAuthResult(AntUserSmsCodeActivity.this, antUserInfo.getMobile());
                        setResult(Activity.RESULT_OK);
                        // 通知
                        EventBus.getDefault().post(antUserInfo);
                        finish();
                    }
                });
    }

}
