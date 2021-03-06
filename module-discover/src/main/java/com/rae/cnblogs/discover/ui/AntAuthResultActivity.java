package com.rae.cnblogs.discover.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 授权结果界面
 */
@Route(path = AppRoute.PATH_DISCOVER_AUTH_RESULT)
public class AntAuthResultActivity extends SwipeBackBasicActivity {

    @BindView(R2.id.tv_hello)
    TextView mMessageView;
    @BindView(R2.id.btn_send)
    Button mButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_auth_result);
        setTitle(" ");
        String phone = getIntent().getStringExtra("phone");
        mMessageView.setText(String.format("欢迎 %s", phone));
        UICompat.scaleIn(mMessageView);
        UICompat.scaleIn(mButton);
    }

    @OnClick(R2.id.btn_send)
    public void onClick() {
        setResult(RESULT_OK);
        finish();
    }

}
