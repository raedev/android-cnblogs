package com.rae.cnblogs.moment.post;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

import java.util.List;

/**
 * 发布闪存
 * Created by ChenRui on 2017/10/27 0027 14:37.
 */
public interface IPostMomentContract {

    interface Presenter extends IPresenter {
        boolean post();

        boolean isBlogOpened();
    }

    interface View extends IPresenterView {

        String getContent();

        void onPostMomentFailed(String msg);

        void onPostMomentSuccess();

        List<String> getImageUrls();

        /**
         * 后台发送
         *
         * @param showTips 进入后台发布提示
         */
        void onPostMomentInProgress(boolean showTips);

        /**
         * 博客开通状态
         */
        void onLoadBlogOpenStatus(Boolean value);
    }
}
