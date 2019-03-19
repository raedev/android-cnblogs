package com.rae.cnblogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rae.cnblogs.dialog.LoadingDialog;
import com.rae.cnblogs.widget.R;

import java.lang.ref.WeakReference;

public final class UICompat {

    // 当前的弱引用
    private static WeakReference<LoadingDialog> sDialogWeakReference;


    @NonNull
    @SuppressLint("ShowToast")
    private static Toast makeToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        if (context == null) {
            return toast;
        }
        toast.getView().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_toast));
        TextView msgView = toast.getView().findViewById(android.R.id.message);
        if (msgView != null) {
            msgView.setTextSize(14);
            msgView.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        return toast;
    }

    public static Toast toast(Context context, String msg) {
        Toast toast = makeToast(context, msg);
        toast.show();
        return toast;

    }

    public static Toast toastInCenter(Context context, String msg) {
        Toast toast = makeToast(context, msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    public static void success(Context context, int resId) {
        Toast toast = makeToast(context, context.getString(resId));
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView msgView = toast.getView().findViewById(android.R.id.message);
        if (msgView != null) {
            msgView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.toast_success, 0, 0);
            msgView.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        }
        toast.show();
    }

    public static Toast failed(Context context, String msg) {
        Toast toast = makeToast(context, msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    public static void setVisibility(@Nullable View view, boolean visibility) {
        if (view == null) return;
        view.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    public static void loading(Context context) {
        loading(context, context.getString(R.string.loading));
    }

    public static void loading(Context context, String msg) {
        try {
            LoadingDialog dialog;
            if (sDialogWeakReference == null || sDialogWeakReference.get() == null) {
                sDialogWeakReference = new WeakReference<>(new LoadingDialog(context));
            }
            dialog = sDialogWeakReference.get();
            // 不是当前的处理
            if (!((ContextWrapper) dialog.getContext()).getBaseContext().equals(context)) {
                if (dialog.getWindow() != null) {
                    dialog.dismiss();
                }
                sDialogWeakReference.clear();
                sDialogWeakReference = null;
                sDialogWeakReference = new WeakReference<>(new LoadingDialog(context));
            }

            dialog.setMessage(msg);
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void dismiss() {
        if (sDialogWeakReference != null && sDialogWeakReference.get() != null) {
            try {
                LoadingDialog dialog = sDialogWeakReference.get();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            sDialogWeakReference.clear();
        }
        sDialogWeakReference = null;
    }


    /**
     * 滚动到顶部
     */
    public static void scrollToTop(final RecyclerView recyclerView) {
        if (recyclerView == null) return;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager == null) return;
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        int visibleCount = lastItem - firstItem;

//        // 已经处于顶部
//        if (firstItem <= 1) {
//            return;
//        }

        // 超过一屏
        if (lastItem > visibleCount) {
            recyclerView.scrollToPosition(visibleCount + 3);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(0);
                }
            }, 50);// 避免发生错位
            return;
        }

        recyclerView.smoothScrollToPosition(0);
    }

    public static void fadeIn(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in));
    }

    public static void fadeIn(View view, long duration) {
        Animation anim = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
        anim.setDuration(duration);
        anim.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(anim);
    }

    public static void fadeOut(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_out));
    }

    public static void fadeOut(View view, long duration) {
        Animation anim = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_out);
        anim.setDuration(duration);
        anim.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(anim);
    }

    public static void scaleIn(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_in));
    }

    @Nullable
    public static Fragment findCurrentFragment(FragmentManager fm, ViewPager viewPager) {
        return fm.findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + viewPager.getCurrentItem());
    }

    @Nullable
    public static Fragment findFragment(FragmentManager fm, ViewPager viewPager, int index) {
        return fm.findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + index);
    }

    public static void hideSoftInputFromWindow(@NonNull Context context, @NonNull View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftInputFromWindow(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    public static void hideSoftInputFromWindow(@NonNull Activity context) {
        hideSoftInputFromWindow(context, context.getWindow().getDecorView());
    }

    /**
     * 设置文本，并自动滚动到文本末端
     */
    public static void setText(@NonNull EditText view, @NonNull String text) {
        view.setText(text);
        view.setSelection(view.length());
    }
}
