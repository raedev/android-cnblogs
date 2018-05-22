package com.rae.cnblogs.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.rae.cnblogs.widget.R;

/**
 * 正在加载中
 * Created by ChenRui on 2017/1/26 0026 17:13.
 */
public class LoadingDialog extends DefaultDialog {

    private TextView mLoadingTextView;

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        setContentView(R.layout.dialog_loading);
        mLoadingTextView = findViewById(R.id.tv_view_loading_title);
        super.init();
        Window window = getWindow();
        if (window != null) {
            window.setDimAmount(0);
        }
    }


    public void setTitle(String title) {
        mLoadingTextView.setVisibility(View.VISIBLE);
        mLoadingTextView.setText(title);
    }

    public void setMessage(String message) {
        setTitle(message);
    }
}
