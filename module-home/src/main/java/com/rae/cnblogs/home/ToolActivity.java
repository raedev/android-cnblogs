package com.rae.cnblogs.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;

import butterknife.BindView;
import butterknife.OnClick;

public class ToolActivity extends SwipeBackBasicActivity {

    @BindView(R2.id.tv_sdk_version)
    TextView mSdkVersionView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool);
        initData();
    }

    private void initData() {
        CnblogsApiProvider apiProvider = CnblogsApiFactory.getInstance(this);
        String sdkVersion = String.format("%s-%s", apiProvider.getClass().getSimpleName(), apiProvider.getApiVersion());
        mSdkVersionView.setText(sdkVersion);
    }


    @OnClick(R2.id.btn_reset_sdk)
    public void onResetSdkClick() {
        CnblogsApiFactory.reset(this);
        initData();
    }
}
