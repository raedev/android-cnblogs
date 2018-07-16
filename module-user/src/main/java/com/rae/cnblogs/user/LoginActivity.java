package com.rae.cnblogs.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.user.login.LoginContract;
import com.rae.cnblogs.user.login.LoginPresenterImpl;

import butterknife.OnClick;

/**
 * 登录
 * Created by ChenRui on 2017/1/19 0019 9:59.
 */
@Route(path = AppRoute.PATH_LOGIN)
public class LoginActivity extends BasicActivity implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setElevation(0);
        }
        mPresenter = new LoginPresenterImpl(this);
    }

    @Override
    public void onUserLicense() {
        new DefaultDialogFragment
                .Builder()
                .message(getString(R.string.login_contract_content))
                .confirmText(getString(R.string.agree))
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.license();
                    }
                })
                .show(getSupportFragmentManager());
    }

    @Override
    public void onRouteToWebLogin() {
        // 跳转网页登录
        AppRoute.routeToWebLogin(this);
    }

    @Override
    public void onLoginSuccess(UserInfoBean data) {
        // 不处理
    }

    @Override
    public void onLoginFailed(String message) {
        // 不处理
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    /**
     * 忘记密码
     */
    @OnClick(R2.id.tv_forget_password)
    public void onForgetPasswordClick() {
        AppMobclickAgent.onClickEvent(this, "ForgetPassword");
        AppRoute.routeToWeb(this, getString(R.string.forget_password_url));
    }

    /**
     * 注册账号
     */
    @OnClick(R2.id.btn_reg)
    public void onRegClick() {
        AppMobclickAgent.onClickEvent(this, "Reg");
        AppRoute.routeToWeb(this, getString(R.string.reg_url));
    }

    /**
     * 登录点击
     */
    @OnClick(R2.id.ll_login)
    public void onLoginClick() {
        AppMobclickAgent.onClickEvent(this, "ForgetPassword");
        mPresenter.login();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 登录成功返回
        if (resultCode == RESULT_OK && requestCode == AppRoute.REQ_CODE_WEB_LOGIN) {
            UICompat.success(this, R.string.login_success);
            setResult(RESULT_OK);
            finish();
        }
    }
}
