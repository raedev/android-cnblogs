package com.rae.cnblogs.theme;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

import com.rae.cnblogs.widget.RaeSkinImageView;
import com.rae.cnblogs.widget.RaeSkinImageViewV4;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import skin.support.SkinCompatManager;
import skin.support.app.SkinLayoutInflater;

/**
 * 主题资源扩展类
 * Created by ChenRui on 2017/8/30 0030 16:46.
 */
public final class ThemeCompat {

//    static String currentSkinName;

    public static class CnblogsThemeHookInflater implements SkinLayoutInflater {
        @Override
        public View createView(@NonNull Context context, String name, @NonNull AttributeSet attributeSet) {
            if (TextUtils.equals("ImageView", name)) {
                try {
                    // 解决compat包使用了vector，导致5.0以下低版本文章复制产生崩溃。
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
                        return new RaeSkinImageViewV4(context, attributeSet);
                    }
                    return new RaeSkinImageView(context, attributeSet);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return null;
            }
            return null;
        }
    }


    private static int getResourceId(Context context, String name, String type) {
        Resources resources = context.getResources();
        String themeName = name;
        if (isNight()) {
            themeName += "_night";
        }
        try {
            return resources.getIdentifier(themeName, type, context.getPackageName());
        } catch (Exception ignored) {

        }
        return resources.getIdentifier(name, type, context.getPackageName());
    }

    /**
     * 是否为夜间模式
     */
    public static boolean isNight() {
        return "night".equalsIgnoreCase(SkinCompatManager.getInstance().getCurSkinName());
    }

//    /**
//     * 在{@link #isNight()} 调用之前，用于判断资源文件
//     */
//    public static boolean isNightInResource() {
//        return !TextUtils.isEmpty(currentSkinName);
//    }


    /**
     * 获取颜色，根据不通的模式返回不通的资源
     *
     * @param name 名称
     */
    public static int getColor(Context context, String name) {
        return getResourceId(context, name, "color");
    }

    /**
     * 获取图片
     *
     * @param name 名称
     */
    public static int getDrawableId(Context context, String name) {
        return getResourceId(context, name, "drawable");
    }

    /**
     * 获取图片
     *
     * @param name 名称
     */
    public static Drawable getDrawable(Context context, String name) {
        return context.getResources().getDrawable(getDrawableId(context, name));
    }

    /**
     * 切换夜间模式
     */
    public static void switchNightMode() {
        switchNightMode(isNight());
    }

    /**
     * 切换夜间模式
     *
     * @param isNight 是否为夜间模式
     */
    public static void switchNightMode(boolean isNight) {
        if (isNight) {
            // 切换正常模式
            SkinCompatManager.getInstance().loadSkin("");
        } else {
            SkinCompatManager.getInstance().loadSkin("night", null, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
        }
    }

    /**
     * 刷新状态栏颜色
     *
     * @param nightMode 是否为深色模式
     */
    public static void refreshStatusColor(Activity context, boolean nightMode) {
        if (context == null || context.getWindow() == null) return;
        changeMiUIStatusMode(context.getWindow(), nightMode);

        // 改变系统状态颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getWindow().setStatusBarColor(nightMode ?
                    ContextCompat.getColor(context, android.R.color.black) :
                    ContextCompat.getColor(context, android.R.color.white));
        }

    }

    /**
     * 修改小米手机系统的状态栏字体颜色
     *
     * @param dark 状态栏黑色字体
     */
    private static void changeMiUIStatusMode(Window window, boolean dark) {
        if (!Build.BRAND.toLowerCase().contains("mi")) {
            return;
        }

        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
            } catch (Exception ignored) {
            }
        }
    }

}
