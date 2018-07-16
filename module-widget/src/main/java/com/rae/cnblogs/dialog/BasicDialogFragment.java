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

import com.rae.cnblogs.widget.R;

import butterknife.ButterKnife;

/**
 * 弹出对话框
 */
public abstract class BasicDialogFragment extends AppCompatDialogFragment {

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
            int margin = (int) getContext().getResources().getDimension(R.dimen.default_dialog_margin);
            InsetDrawable drawable = new InsetDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_dialog_default), margin, margin, margin, margin);
            window.setWindowAnimations(R.style.SlideAnimation);
            window.setBackgroundDrawable(drawable);
        }
    }
}
