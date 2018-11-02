package com.rae.cnblogs.blog.feed;

import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.UserFeedBean;

import java.util.List;

/**
 * Created by dengmaohua on 2018/11/2 16:23.
 */
public interface FeedContract {

    interface View extends IPresenterView, IPageView<BlogCommentBean> {
        String getBlogApp();

        void onLoadFeedFailed(String msg);

        void onLoadMoreFeedFailed(String msg);

        void onLoadFeedSuccess(List<UserFeedBean> dataList);

        void onLoadMoreFinish();
    }

    interface Presenter extends IPresenter {
    }
}
