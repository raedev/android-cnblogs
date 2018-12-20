package com.rae.cnblogs.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PersonalNickNameFragment extends BasicFragment implements View.OnClickListener {

    @BindView(R2.id.et_text)
    EditText mTextView;

    TextView mSaveView;

    private IUserApi mUserApi;
    private String mOldName;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_personal_account;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserApi = CnblogsApiFactory.getInstance(getContext()).getUserApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        UserInfoBean userInfo = UserProvider.getInstance().getLoginUserInfo();
        if (userInfo != null) {
            mOldName = userInfo.getDisplayName();
            mTextView.setText(userInfo.getDisplayName());
            mTextView.setSelection(mTextView.length());
            mTextView.setHint("请输入新的昵称");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        mSaveView = activity.findViewById(R.id.btn_save);
        mSaveView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String text = mTextView.getText().toString();
        if (TextUtils.isEmpty(text)) return;
        mSaveView.setEnabled(false);
        mSaveView.setText("保存中");
        AndroidObservable.create(mUserApi.updateNickName(mOldName, text))
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        mSaveView.setEnabled(true);
                        mSaveView.setText(R.string.save);
                        UICompat.toastInCenter(getContext(), message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        EventBus.getDefault().post(new UserInfoChangedEvent());
                        UICompat.toastInCenter(getContext(), "昵称修改成功");
                        if (getActivity() != null)
                            getActivity().finish();
                    }
                });
    }
}
