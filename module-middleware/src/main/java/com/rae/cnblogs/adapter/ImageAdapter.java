package com.rae.cnblogs.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rae.cnblogs.basic.GlideApp;
import com.rae.cnblogs.middleware.R;

import java.util.List;


/**
 * 图片适配器
 * Created by ChenRui on 2017/7/26 0026 22:28.
 */
public class ImageAdapter extends PagerAdapter implements View.OnClickListener {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<String> mUrls;
    private View.OnClickListener mOnItemClickListener;

    public ImageAdapter(Context context, List<String> urls) {
        mUrls = urls;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return mUrls == null ? 0 : mUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull View container, int position) {
        View view = mLayoutInflater.inflate(R.layout.item_image_preview, (ViewGroup) container, false);
        final ImageView imageView = view.findViewById(R.id.img_preview);
        final View loadingView = view.findViewById(R.id.pb_loading);
        String url = mUrls.get(position);
        imageView.setOnClickListener(this);
        loadingView.setVisibility(View.VISIBLE);
        // 显示
        GlideApp.with(imageView)
                .load(url)
                .error(R.drawable.picture_viewer_no_pic_icon)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER);
                        dismissLoading();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        dismissLoading();
                        return false;
                    }

                    private void dismissLoading() {
                        loadingView.setVisibility(View.GONE);
                    }
                })
                .dontAnimate()
                .into(imageView);

        ((ViewGroup) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewGroup) container).removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener == null)
            ((Activity) mContext).finish();
        else
            mOnItemClickListener.onClick(v);
    }

    public String getItem(int item) {
        return mUrls.get(item % mUrls.size());
    }
}
