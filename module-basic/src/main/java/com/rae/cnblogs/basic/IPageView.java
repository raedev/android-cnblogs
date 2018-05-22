package com.rae.cnblogs.basic;


import java.util.List;

public interface IPageView<T> {
    void onNoMoreData();

    void onEmptyData(String msg);

    void onLoadData(List<T> data);

    void onLoginExpired();
}
