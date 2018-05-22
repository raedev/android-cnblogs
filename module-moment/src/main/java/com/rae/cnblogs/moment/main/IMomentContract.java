package com.rae.cnblogs.moment.main;

import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.MomentBean;

/**
 * 闪存
 * Created by ChenRui on 2017/10/27 0027 10:54.
 */
public interface IMomentContract {

    interface Presenter extends IPresenter {
        void loadMore();
    }

    interface View extends IPresenterView, IPageView<MomentBean> {

        /**
         * 参考 {@link com.rae.cnblogs.sdk.api.IMomentApi#MOMENT_TYPE_ALL}
         */
        String getType();

        /**
         * 消息数量改变
         *
         * @param replyMeCount 有回复我的消息数量
         * @param atMeCount    有提到我的数量
         */
        void onMessageCountChanged(int replyMeCount, int atMeCount);
    }
}
