package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * 分类接口
 * Created by ChenRui on 2016/11/30 0030 17:19.
 */
public interface ICategoryApi {

    /**
     * 获取分类
     */
    Observable<List<CategoryBean>> getCategories();

    /**
     * 获取分类
     */
    Observable<List<CategoryBean>> getHomeCategories();

    /**
     * 更新分类
     */
    Observable<Empty> updateCategories(List<CategoryBean> categoryList);
}
