package com.rae.cnblogs.discover.fragment;

import android.os.Bundle;

import com.antcode.sdk.model.AntUserInfo;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

/**
 * 登录替代页面
 */
public class AntLoginPlaceHolderFragment extends BasicFragment {

    public static AntLoginPlaceHolderFragment newInstance() {

        Bundle args = new Bundle();

        AntLoginPlaceHolderFragment fragment = new AntLoginPlaceHolderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_ant_login_placeholder;
    }

    @OnClick(R2.id.btn_placeholder_login)
    public void OnLoginClick() {
//        AntUserInfo m = new AntUserInfo();
//        m.setNickName("test user info");
//        EventBus.getDefault().post(m);
        AppRoute.routeToAntUserAuth(getContext());
    }
}
