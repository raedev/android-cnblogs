package com.rae.cnblogs.discover.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.antcode.sdk.AntCodeSDK;
import com.antcode.sdk.model.AntAppConfigInfo;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.discover.AntSdkDefaultObserver;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.web.WebViewFragment;

import butterknife.OnClick;

/**
 * 用户协议界面
 */
@Route(path = AppRoute.PATH_DISCOVER_USER_CONTRACT)
public class AntUserContractActivity extends SwipeBackBasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_user_contract);

        AntCodeSDK.getInstance()
                .getUserApi()
                .getAppConfig()
                .with(this)
                .subscribe(new AntSdkDefaultObserver<AntAppConfigInfo>() {
                    @Override
                    protected void onError(String message) {

                    }

                    @Override
                    protected void accept(AntAppConfigInfo antAppConfigInfo) {
                        onLoadSuccess(antAppConfigInfo);
                    }
                });

    }

    private void onLoadSuccess(AntAppConfigInfo config) {
        if (TextUtils.isEmpty(config.getUserContractUrl())) {
            UICompat.failed(this, "用户协议加载错误");
            return;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, WebViewFragment.newInstance(config.getUserContractUrl(), null)).commitNow();
    }

    @OnClick(R2.id.btn_save)
    public void onConfirmClick() {
        finish();
    }
}
