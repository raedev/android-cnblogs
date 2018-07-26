package com.rae.cnblogs.sdk.event;

/**
 * 搜索事件
 * Created by ChenRui on 2017/8/29 0029 12:46.
 */
public class SearchEvent {
    private String searchText;

    private boolean isFromUser; // 是否来自用户输入的，true 表示从热门搜索或者搜索历史中过来，应该立即执行搜索

    public SearchEvent(String searchText) {
        this.searchText = searchText;
    }

    public SearchEvent(String searchText, boolean isFromUser) {
        this.searchText = searchText;
        this.isFromUser = isFromUser;
    }

    public boolean isFromUser() {
        return isFromUser;
    }

    public String getSearchText() {
        return searchText;
    }
}
