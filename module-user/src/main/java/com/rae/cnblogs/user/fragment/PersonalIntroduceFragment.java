package com.rae.cnblogs.user.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.basic.rx.DefaultEmptyObserver;
import com.rae.cnblogs.sdk.event.UserInfoChangedEvent;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;
import com.rae.cnblogs.web.WebViewFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * 个性签名
 * Created by rae on 2018/12/20.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PersonalIntroduceFragment extends BasicFragment {
    private WebViewFragment mWebViewFragment;

    @BindView(R2.id.ll_content_comment)
    ViewGroup mTipsLayout;

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
        AndroidObservable.create(Observable.timer(2, TimeUnit.SECONDS))
                .with(this)
                .subscribe(new DefaultEmptyObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        mTipsLayout.setVisibility(View.VISIBLE);
                        UICompat.fadeIn(mTipsLayout);
                    }
                });

    }

    @OnClick(R2.id.btn_ensure)
    public void onKnowClick() {
        UICompat.fadeOut(mTipsLayout);
        mTipsLayout.setVisibility(View.GONE);
    }

}
