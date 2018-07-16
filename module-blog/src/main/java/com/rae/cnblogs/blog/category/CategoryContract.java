package com.rae.cnblogs.blog.category;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

/**
 * 分类
 * Created by rae on 2018/6/29.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface CategoryContract {
    interface Presenter extends IPresenter {

        /**
         * 保存分类信息
         *
         * @param mycategories        我的分类
         * @param recommendcategories 推荐分类
         */
        void save(List<CategoryBean> mycategories, List<CategoryBean> recommendcategories);
    }

    interface View extends IPresenterView {


        /**
         * 加载分类失败
         *
         * @param message 错误消息
         */
        void onLoadCategoryFailed(String message);

        /**
         * 加载分类数据
         *
         * @param myCategories        我的分类数据
         * @param recommendCategories 推荐的分类数据
         */
        void onLoadCategory(List<CategoryBean> myCategories, List<CategoryBean> recommendCategories);
    }
}
