package com.rae.cnblogs.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;

import com.rae.cnblogs.theme.ThemeCompat;


/**
 * 弹出窗口
 * Created by ChenRui on 2017/1/24 0024 14:09.
 */
public class DefaultDialog extends AppCompatDialog {

    public DefaultDialog(Context context) {
        super(context);
        init();
    }

    public DefaultDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public DefaultDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected void init() {
        // 夜间模式不显示遮罩层
        if (ThemeCompat.isNight() && getWindow() != null) {
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getWindow().setDimAmount(0.6f);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 设置透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                getWindow().setElevation(0);
            }
        }
    }

}
