package com.rae.cnblogs.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.antcode.sdk.model.AntAdInfo;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rae.cnblogs.basic.GlideApp;
import com.youth.banner.loader.ImageLoaderInterface;

public class BannerImageLoader implements ImageLoaderInterface<View> {

    private LayoutInflater mLayoutInflater;

    @Override
    public void displayImage(Context context, Object path, View view) {
        AntAdInfo adInfo = (AntAdInfo) path;
        String url = adInfo.getImageUrl();
        ImageView imageView = view.findViewById(R.id.img_logo);

        GlideApp.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(300))
//                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(20, 20)))
                .placeholder(R.drawable.default_placeholder_normal)
                .error(R.drawable.default_placeholder_error)
                .into(imageView);
    }

    @Override
    public View createImageView(Context context) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(context);
        return mLayoutInflater.inflate(R.layout.item_banner, null);
    }
}
