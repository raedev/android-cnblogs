package com.rae.cnblogs.home.launcher;

import android.support.annotation.NonNull;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

/**
 * 启动页
 */
public interface LauncherContract {

    interface Presenter extends IPresenter {

        /**
         * 广告点击
         */
        void onAdClick();
    }

    interface View extends IPresenterView {

        /**
         * 加载图片
         *
         * @param name 图片标题
         * @param url  图片地址
         */
        void onLoadImage(String name, @NonNull String url);

        /**
         * 没有图片
         */
        void onEmptyImage();

        /**
         * 跳转到网页
         *
         * @param url 地址
         */
        void onRouteToWeb(@NonNull String url);

        /**
         * 跳转到首页
         */
        void onRouteToHome();

        /**
         * 图片发生改变，需要停止当前倒计时
         */
        void onImageChanged();
    }
}
