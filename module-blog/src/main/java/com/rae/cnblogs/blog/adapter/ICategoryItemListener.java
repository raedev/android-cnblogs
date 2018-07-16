package com.rae.cnblogs.blog.adapter;

import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 分类单个Item操作监听
 * Created by rae on 2018/6/29.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface ICategoryItemListener {

    /**
     * 当分类移除按钮点击时候触发
     */
    void onItemRemoveClick(int position, CategoryBean item);

    /**
     * 点击触发
     */
    void onItemClick(int position, CategoryBean item);


    /**
     * 长按触发
     */
    void onItemLongClick(int position, CategoryBean item);
}
