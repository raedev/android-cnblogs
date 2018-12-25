package com.rae.cnblogs.dialog;

import android.app.Dialog;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.rae.cnblogs.theme.ThemeCompat;
import com.rae.cnblogs.widget.R;

import butterknife.ButterKnife;
import skin.support.SkinCompatManager;
import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

/**
 * 弹出对话框
 */
public abstract class BasicDialogFragment extends AppCompatDialogFragment {

    private SkinObserver mSkinObserver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    public abstract int getLayoutId();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null)
            onLoadWindowAttr(window);

        // 初始化数据
        Bundle arguments = getArguments();
        if (arguments != null) {
            onLoadData(arguments);
        }

        // 皮肤切换监听
        mSkinObserver = new SkinObserver() {
            @Override
            public void updateSkin(SkinObservable observable, Object o) {
                if (getContext() == null) return;
                int margin = (int) getContext().getResources().getDimension(R.dimen.default_dialog_margin);
                int resId = R.drawable.bg_dialog_default;
                if (ThemeCompat.isNight()) {
                    resId = R.drawable.bg_dialog_default_night;
                }
                InsetDrawable drawable = new InsetDrawable(ContextCompat.getDrawable(getContext(), resId), margin, margin, margin, margin);
                getDialog().getWindow().setBackgroundDrawable(drawable);
            }
        };
        SkinCompatManager.getInstance().addObserver(mSkinObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSkinObserver != null)
            SkinCompatManager.getInstance().deleteObserver(mSkinObserver);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DefaultDialog(getContext());
    }

    protected void onLoadData(Bundle arguments) {

    }

    protected void onLoadWindowAttr(@NonNull Window window) {
        if (getContext() != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setDimAmount(0.3f);
            window.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
            int resId = R.drawable.bg_dialog_default;
            if (ThemeCompat.isNight()) {
                resId = R.drawable.bg_dialog_default_night;
            }
            int margin = (int) getContext().getResources().getDimension(R.dimen.default_dialog_margin);
            InsetDrawable drawable = new InsetDrawable(ContextCompat.getDrawable(getContext(), resId), margin, margin, margin, margin);
            window.setWindowAnimations(R.style.SlideOverShootAnimation);
            window.setBackgroundDrawable(drawable);
        }
    }

}
