package com.rae.cnblogs.user.friends;

/**
 * Created by rae on 2018/7/24.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface ISearchListener {
    /**
     * 出发搜索
     *
     * @param text 搜索文本
     */
    void onSearch(CharSequence text);
}
