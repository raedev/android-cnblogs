package com.rae.cnblogs.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.rae.cnblogs.widget.R;


/**
 * 底部滑动上来的对话框
 * Created by ChenRui on 2016/12/7 22:15.
 */
public class SlideDialog extends DefaultDialog {

    public SlideDialog(Context context) {
        this(context, R.style.SlideDialog);
    }

    public SlideDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    @Override
    protected void init() {
        super.init();
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.gravity = Gravity.LEFT | Gravity.RIGHT | Gravity.BOTTOM;
        attr.horizontalMargin = 0;
        attr.verticalMargin = 0;
        attr.width = WindowManager.LayoutParams.MATCH_PARENT;
        attr.height = WindowManager.LayoutParams.WRAP_CONTENT;
        onWindowLayout(getWindow(), attr);
        getWindow().setAttributes(attr);
    }

    protected void onWindowLayout(Window window, WindowManager.LayoutParams attr) {
    }

}
