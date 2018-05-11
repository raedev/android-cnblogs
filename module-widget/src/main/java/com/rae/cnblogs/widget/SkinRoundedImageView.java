package com.rae.cnblogs.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;
import com.rae.cnblogs.theme.ThemeCompat;

import skin.support.widget.SkinCompatSupportable;

/**
 * 圆角图片
 * Created by ChenRui on 2017/9/6 0006 19:32.
 */
public class SkinRoundedImageView extends RoundedImageView implements SkinCompatSupportable {
    public SkinRoundedImageView(Context context) {
        super(context);
        init();
    }

    public SkinRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SkinRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 初始化的时候不用取反
        setAlpha(ThemeCompat.isNight() ? getResources().getInteger(R.integer.imageAlpha_night) / 100.0f : 1f);
    }

    @Override
    public void applySkin() {
        setAlpha(isNight() ? getResources().getInteger(R.integer.imageAlpha_night) / 100.0f : 1f);
    }

    public boolean isNight() {
        // 因为是先应用主题之后才会设置主题名称，所以这里取反。
        return !ThemeCompat.isNight();
    }
}
