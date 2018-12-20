package com.rae.cnblogs.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.utils.ApiUtils;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PersonalPasswordFragment extends BasicFragment {

    @BindView(R2.id.et_old_pwd)
    EditText mOldPwdView;

    @BindView(R2.id.et_new_pwd)
    EditText mNewPwdView;

    @BindView(R2.id.btn_save_view)
    Button mSaveButton;

    private IUserApi mUserApi;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_personal_pwd;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserApi = CnblogsApiFactory.getInstance(getContext()).getUserApi();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        activity.findViewById(R.id.btn_save).setVisibility(View.GONE);
    }

    private String getOldPassword() {
        return mOldPwdView.getText().toString();
    }

    private String getNewPassword() {
        return mNewPwdView.getText().toString();
    }


    @OnClick(R2.id.btn_save_view)
    public void onClick() {
        String oldPwd = getOldPassword();
        String newPwd = getNewPassword();
        String[] passwords = new String[]{oldPwd, newPwd};
        String[] names = new String[]{"旧密码", "新密码"};
        View[] views = new View[]{mOldPwdView, mNewPwdView};
        for (int i = 0; i < passwords.length; i++) {
            String item = passwords[i];
            if (TextUtils.isEmpty(item)) {
                UICompat.toastInCenter(getContext(), "请输入" + names[i]);
                views[i].requestFocus();
                return;
            }
            if (i != 0 && item.length() < 8) {
                UICompat.toastInCenter(getContext(), names[i] + "长度不少于8位");
                views[i].requestFocus();
                return;
            }
            if (i != 0 && item.length() > 30) {
                UICompat.toastInCenter(getContext(), names[i] + "最大长度不能操作30位");
                views[i].requestFocus();
                return;
            }
        }

        mSaveButton.setEnabled(false);
        mSaveButton.setText("请稍后...");

        // 公钥加密
        oldPwd = ApiUtils.encrypt(oldPwd);
        newPwd = ApiUtils.encrypt(newPwd);
        if (getActivity() != null)
            UICompat.hideSoftInputFromWindow(getActivity());

        AndroidObservable.create(mUserApi.changePassword(oldPwd, newPwd))
                .with(this)
                .subscribe(new ApiDefaultObserver<String>() {
                    @Override
                    protected void onError(String message) {
                        mSaveButton.setEnabled(true);
                        mSaveButton.setText(R.string.reset_password);
                        UICompat.failed(getContext(), message);
                    }

                    @Override
                    protected void accept(String message) {
                        if (!message.contains("成功")) {
                            onError(message);
                            return;
                        }

                        // 跳转登录
                        UserProvider.getInstance().logout();
                        AppRoute.finish();
                        AppRoute.routeToActionResult(getContext(), "密码修改成功");
                    }
                });
    }
}
