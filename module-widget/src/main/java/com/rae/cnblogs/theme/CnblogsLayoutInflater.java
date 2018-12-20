package com.rae.cnblogs.theme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.rae.cnblogs.widget.SkinCardView;
import com.rae.cnblogs.widget.SkinFitWindowsFrameLayout;
import com.rae.cnblogs.widget.SkinFitWindowsLinearLayout;
import com.rae.cnblogs.widget.SkinNestedScrollView;
import com.rae.cnblogs.widget.SkinRoundedImageView;

import skin.support.app.SkinLayoutInflater;

/**
 * 自定义View不支持的皮肤扩展
 * Created by ChenRui on 2017/8/30 0030 17:25.
 */
public class CnblogsLayoutInflater implements SkinLayoutInflater {

    @Override
    public View createView(@NonNull Context context, String name, @NonNull AttributeSet attrs) {
        switch (name) {
            case "com.makeramen.roundedimageview.RoundedImageView":
                return new SkinRoundedImageView(context, attrs);
            case "android.support.v7.widget.CardView":
                return new SkinCardView(context, attrs);
            case "android.support.v7.widget.FitWindowsFrameLayout":
                return new SkinFitWindowsFrameLayout(context, attrs);
            case "android.support.v7.widget.FitWindowsLinearLayout":
                return new SkinFitWindowsLinearLayout(context, attrs);
            case "android.support.v4.widget.NestedScrollView":
                return new SkinNestedScrollView(context, attrs);
        }
        return null;
    }
}
