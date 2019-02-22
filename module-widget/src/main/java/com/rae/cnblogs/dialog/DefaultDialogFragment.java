package com.rae.cnblogs.dialog;

import android.content.DialogInterface;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.rae.cnblogs.theme.ThemeCompat;
import com.rae.cnblogs.widget.R;
import com.rae.cnblogs.widget.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 默认的弹出对话框
 */
public class DefaultDialogFragment extends BasicDialogFragment {


    public static class Builder {
        private DefaultDialogFragment mFragment;
        private Bundle mData;

        public Builder() {
            mFragment = new DefaultDialogFragment();
            mData = new Bundle();
        }

        public Builder message(String message) {
            mData.putString("message", message);
            return this;
        }

        public Builder confirm(DialogInterface.OnClickListener clickListener) {
            mFragment.setOnConfirmClickListener(clickListener);
            return this;
        }

        public Builder cancel(DialogInterface.OnCancelListener cancelListener) {
            mFragment.setOnCancelClickListener(cancelListener);
            return this;
        }

        public Builder canceledOnTouchOutside(boolean cancelable) {
            mData.putBoolean("canceledOnTouchOutside", cancelable);
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            mData.putBoolean("cancelable", cancelable);
            return this;
        }

        public Builder confirmText(String text) {
            mData.putString("confirmText", text);
            return this;
        }

        public Builder cancelText(String text) {
            mData.putString("cancelText", text);
            return this;
        }

        public Builder dismiss(DialogInterface.OnDismissListener listener) {
            mFragment.setOnDismissListener(listener);
            return this;
        }


        public DefaultDialogFragment show(FragmentManager fragmentManager, String tag) {
            mFragment.setArguments(mData);
            mFragment.show(fragmentManager, tag);
            return mFragment;
        }

        public DefaultDialogFragment show(FragmentManager fragmentManager) {
            return show(fragmentManager, "defaultDialog");
        }

        public DefaultDialogFragment build() {
            return mFragment;
        }
    }

    @BindView(R2.id.tv_message)
    TextView mTitleView;

//    @BindView(R2.id.view_divider)
//    View mDividerView;

    @BindView(R2.id.btn_ensure)
    Button mConfirmButton;

    @BindView(R2.id.btn_cancel)
    Button mCancelButton;

    private DialogInterface.OnClickListener mOnConfirmClickListener;
    private DialogInterface.OnCancelListener mOnCancelClickListener;

    private DialogInterface.OnDismissListener mOnDismissListener;


    @Override
    public int getLayoutId() {
        return R.layout.fm_dialog_default;
    }

    /**
     * 确定点击事件
     */
    public void setOnConfirmClickListener(DialogInterface.OnClickListener onConfirmClickListener) {
        mOnConfirmClickListener = onConfirmClickListener;
    }

    /**
     * 取消点击事件
     */
    public void setOnCancelClickListener(DialogInterface.OnCancelListener onCancelClickListener) {
        mOnCancelClickListener = onCancelClickListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    /**
     * 加载数据
     */
    @Override
    protected void onLoadData(@NonNull Bundle arguments) {
        String message = arguments.getString("message");
        String confirmText = arguments.getString("confirmText", getString(R.string.confirm));
        String cancelText = arguments.getString("cancelText", getString(R.string.cancel));
        boolean cancelable = arguments.getBoolean("cancelable");
        boolean canceledOnTouchOutside = arguments.getBoolean("canceledOnTouchOutside", true);
        mTitleView.setText(message);
        mConfirmButton.setText(confirmText);
        mCancelButton.setText(cancelText);

        if (arguments.getString("cancelText") != null || mOnCancelClickListener != null || cancelable) {
            mCancelButton.setVisibility(View.VISIBLE);
        } else {
            mCancelButton.setVisibility(View.GONE);
        }

        getDialog().setCanceledOnTouchOutside(canceledOnTouchOutside);

//        getDialog().setOnDismissListener(mOnDismissListener);

//        mDividerView.setVisibility(mCancelButton.getVisibility());
    }

    @OnClick(R2.id.btn_cancel)
    void onCancelClick() {
        if (mOnCancelClickListener != null) {
            mOnCancelClickListener.onCancel(getDialog());
            getDialog().cancel();
            return;
        }
        getDialog().cancel();
        dismiss();
    }

    @OnClick(R2.id.btn_ensure)
    void onConfirmClick() {
        if (mOnConfirmClickListener != null) {
            mOnConfirmClickListener.onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
            return;
        }
        dismiss();
    }

    @Override
    protected void onLoadWindowAttr(@NonNull Window window) {
//        super.onLoadWindowAttr(window);
        window.setDimAmount(0.45f);
        window.setGravity(Gravity.CENTER | Gravity.FILL_HORIZONTAL);
        window.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            window.setElevation(0);
        }
        int resId = R.drawable.bg_dialog_default;
        if (ThemeCompat.isNight()) {
            resId = R.drawable.bg_dialog_default_night;
        }
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, window.getContext().getResources().getDisplayMetrics());
        InsetDrawable drawable = new InsetDrawable(ContextCompat.getDrawable(window.getContext(), resId), margin, margin, margin, margin);
        window.setBackgroundDrawable(drawable);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null)
            mOnDismissListener.onDismiss(dialog);
    }
}
