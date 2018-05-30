package com.rae.cnblogs.sdk.event;

/**
 * 搜索事件
 * Created by ChenRui on 2017/8/29 0029 12:46.
 */
public class SearchEvent {
    private String searchText;

    public SearchEvent(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }
}
