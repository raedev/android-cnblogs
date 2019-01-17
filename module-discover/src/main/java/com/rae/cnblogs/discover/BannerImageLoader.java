package com.rae.cnblogs.discover;

import android.content.Context;
import android.widget.ImageView;

import com.antcode.sdk.model.AntAdInfo;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.rae.cnblogs.basic.GlideApp;
import com.youth.banner.loader.ImageLoader;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        AntAdInfo adInfo = (AntAdInfo) path;
        String url = adInfo.getImageUrl();

        GlideApp.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(45, 0)))
                .placeholder(R.drawable.default_placeholder_normal)
                .error(R.drawable.default_placeholder_error)
                .into(imageView);
    }

}
