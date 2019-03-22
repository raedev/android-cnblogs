package com.rae.cnblogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rae.cnblogs.basic.BasicActivity;
import com.rae.cnblogs.sdk.CnblogsApiFactory;

import butterknife.OnClick;

public class TestActivity extends BasicActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @OnClick(R.id.btn_test)
    public void onClick() {
        String text = CnblogsApiFactory.getInstance(this).toString();
        Log.i("rae", text);
    }
}
