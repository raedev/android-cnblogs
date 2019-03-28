package com.rae.cnblogs.discover.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.discover.presenter.AntUserAuthPresenterImpl;
import com.rae.cnblogs.discover.presenter.IAntUserAuthContract;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手机号码登录
 */
@Route(path = AppRoute.PATH_DISCOVER_USER_AUTH)
public class AntUserAuthActivity extends SwipeBackBasicActivity implements IAntUserAuthContract.View {

    @BindView(R2.id.tv_hello)
    TextView mHelloView;

    @BindView(R2.id.tv_bind_phone)
    TextView mBindPhoneView;

    @BindView(R2.id.et_phone)
    EditText mPhoneView;

    @BindView(R2.id.btn_send)
    Button mSendButton;

    private IAntUserAuthContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_user_auth);
        setTitle(" ");
        mPresenter = new AntUserAuthPresenterImpl(this);
        mPresenter.start();
        mPhoneView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSendButton.setEnabled(s.length() >= 11);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R2.id.btn_send)
    public void onSendClick() {
        UICompat.hideSoftInputFromWindow(this);
        new DefaultDialogFragment.Builder()
                .cancelable(true)
                .canceledOnTouchOutside(false)
                .message(getString(R.string.ant_user_contract_message))
                .confirmText("继续登录")
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mSendButton.setEnabled(false);
                        mSendButton.setText(R.string.loading);
                        mPresenter.send();
                    }
                })
                .show(getSupportFragmentManager());

    }

    @OnClick(R2.id.ll_contract)
    public void onContractClick() {

        AppRoute.routeToAntUserContract(AntUserAuthActivity.this);


    }

    @Override
    public String getPhoneNumber() {
        return mPhoneView.getText().toString().replace(" ", "");
    }

    @Override
    public void onSendSuccess() {
        dismissLoading();
        UICompat.toastInCenter(this, "短信验证码发送成功");
        AppRoute.routeToAntSmsCode(this, getPhoneNumber());
    }

    @Override
    public void onSendError(String message) {
        dismissLoading();
        UICompat.failed(this, message);
    }

    private void dismissLoading() {
        mSendButton.setEnabled(true);
        mSendButton.setText(R.string.send_sms);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppRoute.REQ_CODE_ANT_LOGIN && resultCode == RESULT_OK) {
            finish();
        }
    }
}
