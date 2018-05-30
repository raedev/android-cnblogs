package com.rae.cnblogs.blog.fragment;

/**
 * 博客详情Fragment交互接口
 * Created by rae on 2018/5/29.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface IBlogDetailFragment {
    /**
     * Web滚动状态发生改变
     */
    void onScrollChange(int x, int y, int oldX, int oldY);
}
