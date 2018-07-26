package com.rae.cnblogs.home.search;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

import java.util.List;

/**
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface HotSearchContract {
    interface Presenter extends IPresenter {
        /**
         * 清除搜索记录
         */
        void clearSearchHistory();

        /**
         * 保存搜索记录
         *
         * @param keyword 搜索关键字
         */
        void saveHistory(String keyword);
    }

    interface View extends IPresenterView {
        void onEmptyHotSearchData();

        void onLoadHotSearchData(List<String> data);

        void onEmptySearchHistoryData();

        void onLoadSearchHistoryData(List<String> data);
    }
}
