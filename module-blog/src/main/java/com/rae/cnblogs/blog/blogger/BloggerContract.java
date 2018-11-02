package com.rae.cnblogs.blog.blogger;

import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;

/**
 * Created by dengmaohua on 2018/11/2 14:32.
 */
public interface BloggerContract {
    interface Presenter extends IPresenter {
        void doFollow();

        boolean isFollowed();
    }

    interface View extends IPresenterView, IPageView<BlogCommentBean> {

        String getBlogApp();

        void onLoadBloggerInfoFailed(String msg);

        void onLoadBloggerInfo(FriendsInfoBean friendsInfoBean);

        void onFollowSuccess();

        void onFollowFailed(String message);
    }
}
