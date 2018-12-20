package com.rae.cnblogs.user.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;
import com.rae.cnblogs.web.WebViewFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个性签名
 * Created by rae on 2018/12/20.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PersonalIntroduceFragment extends BasicFragment {
    private WebViewFragment mWebViewFragment;

    @BindView(R2.id.ll_content_comment)
    ViewGroup mTipsLayout;

    //    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        FragmentActivity activity = getActivity();
//        if (activity == null) return;
//        activity.findViewById(R.id.btn_save).setVisibility(View.GONE);
//        UICompat.hideSoftInputFromWindow(activity);
//        loadUrl("https://home.cnblogs.com/set/intro/");
//        enablePullToRefresh(false);
//    }
//
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new UserInfoChangedEvent());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_personal_introduce;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebViewFragment = WebViewFragment.newInstance("https://home.cnblogs.com/set/intro/", null);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_web_content, mWebViewFragment).commit();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mWebViewFragment != null) {
            mWebViewFragment.enablePullToRefresh(false);
        }
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        activity.findViewById(R.id.btn_save).setVisibility(View.GONE);
        mTipsLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTipsLayout.setVisibility(View.VISIBLE);
                UICompat.fadeIn(mTipsLayout);
            }
        }, 2000);
    }

    @OnClick(R2.id.btn_ensure)
    public void onKnowClick() {
        UICompat.fadeOut(mTipsLayout);
        mTipsLayout.setVisibility(View.GONE);
    }

    //    @BindView(R2.id.et_text)
//    EditText mTextView;
//
//    TextView mSaveView;
//
//    private IUserApi mUserApi;
//    private String mOldName;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fm_personal_account;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mUserApi = CnblogsApiFactory.getInstance(getContext()).getUserApi();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        UserInfoBean userInfo = UserProvider.getInstance().getLoginUserInfo();
//        if (userInfo != null) {
//            mOldName = userInfo.getDisplayName();
//            mTextView.setText(userInfo.getDisplayName());
//            mTextView.setSelection(mTextView.length());
//            mTextView.setHint("写下关于你的说明");
//            mTextView.setMinHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics()));
//        }
//    }
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        FragmentActivity activity = getActivity();
//        if (activity == null) return;
//        mSaveView = activity.findViewById(R.id.btn_save);
//        mSaveView.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        String text = mTextView.getText().toString();
//        if (TextUtils.isEmpty(text)) return;
//        mSaveView.setEnabled(false);
//        mSaveView.setText("保存中");
//        AndroidObservable.create(mUserApi.updateNickName(mOldName, text))
//                .with(this)
//                .subscribe(new ApiDefaultObserver<String>() {
//                    @Override
//                    protected void onError(String message) {
//                        mSaveView.setEnabled(true);
//                        mSaveView.setText(R.string.save);
//                        UICompat.toastInCenter(getContext(), message);
//                    }
//
//                    @Override
//                    protected void accept(String message) {
//                        if (!message.contains("成功")) {
//                            onError(message);
//                            return;
//                        }
//                        EventBus.getDefault().post(new UserInfoChangedEvent());
//                        UICompat.toastInCenter(getContext(), "昵称修改成功");
//                        if (getActivity() != null)
//                            getActivity().finish();
//                    }
//                });
//    }
}
