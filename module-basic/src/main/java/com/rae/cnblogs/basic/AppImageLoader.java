package com.rae.cnblogs.basic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

/**
 * APP 图片加载器
 */
public final class AppImageLoader {

    /**
     * 替代图资源ID
     */
    private static int DEFAULT_PLACE_HOLDER_ID = -1;

    /**
     * 错误替代图资源ID
     */
    private static int DEFAULT_PLACE_HOLDER_ERROR_ID = -1;

    /**
     * 头像替代图资源ID
     */
    private static int DEFAULT_AVATAR_PLACE_HOLDER_ID = -1;


    private static void checkPlaceHolder(Context context) {
        if (DEFAULT_PLACE_HOLDER_ID == -1) {
            DEFAULT_PLACE_HOLDER_ID = context.getResources().getIdentifier("default_placeholder_normal", "drawable", context.getPackageName());
        }
        if (DEFAULT_PLACE_HOLDER_ERROR_ID == -1) {
            DEFAULT_PLACE_HOLDER_ERROR_ID = context.getResources().getIdentifier("default_placeholder_error", "drawable", context.getPackageName());
        }
    }

    /**
     * 加载头像
     */
    public static void displayAvatar(@Nullable String url, @Nullable ImageView view) {
        displayAvatar(url, view, DEFAULT_AVATAR_PLACE_HOLDER_ID);
    }

    public static void displayAvatar(@Nullable String url, @Nullable ImageView view, int resId) {
        if (view == null)
            return;
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(resId);
            return;
        }

        Context context = view.getContext();
        if (resId == -1) {
            resId = context.getResources().getIdentifier("default_avatar_placeholder", "drawable", context.getPackageName());
        }

        GlideApp.with(view)
                .load(url)
//                .centerCrop()
                .placeholder(resId)
                .error(resId)
                // 渐变动画
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(view);
    }

    /**
     * 加载图片
     */
    public static void display(@Nullable String url, @Nullable ImageView view) {
        if (view == null)
            return;
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(DEFAULT_PLACE_HOLDER_ID);
            return;
        }

        checkPlaceHolder(view.getContext());
        GlideApp.with(view)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(DEFAULT_PLACE_HOLDER_ID)
                .error(DEFAULT_PLACE_HOLDER_ERROR_ID)
                .into(view);
    }

    /**
     * 没有默认的替代图加载图片方式
     */
    public static void displayWithoutPlaceHolder(@Nullable String url, @Nullable ImageView view) {
        if (TextUtils.isEmpty(url) || view == null) {
            return;
        }
        GlideApp.with(view)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(view);
    }

    /**
     * 清除缓存
     */
    public static void clearCache(final Context applicationContext) {
        clearMemoryCache(applicationContext); // 在主线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 在线程中
                GlideApp.get(applicationContext).clearDiskCache();
            }
        }).start();
    }

    /**
     * 清除缓存
     */
    public static void clearMemoryCache(Context applicationContext) {
        GlideApp.get(applicationContext).clearMemory();
    }
}
