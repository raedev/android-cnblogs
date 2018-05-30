package com.rae.cnblogs.blog.home;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

/**
 * 首页
 * Created by ChenRui on 2016/12/2 00:21.
 */
public interface BlogHomeContract {

    interface Presenter extends IPresenter {

    }

    interface View extends IPresenterView {

        /**
         * 加载分类
         */
        void onLoadCategory(List<CategoryBean> data);

        /**
         * 加载热搜
         */
        void onLoadHotSearchData(String keyword);
    }
}
