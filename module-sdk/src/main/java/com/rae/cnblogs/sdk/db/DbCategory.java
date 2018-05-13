package com.rae.cnblogs.sdk.db;

import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;


/**
 * 分类表
 * Created by ChenRui on 2016/12/1 00:24.
 */
public class DbCategory extends DbCnblogs {

    DbCategory() {
    }

//    public void clear() {
//        new Delete().from(CategoryBean.class).execute();
//    }

    /**
     * 重置分类
     *
     * @param list 数据
     */
    public void reset(final List<CategoryBean> list) {

        executeTransaction(new Runnable() {
            @Override
            public void run() {
                for (CategoryBean category : list) {
//                    category.save();
                }
            }
        });

    }

    public List<CategoryBean> list() {
        return null;
//        return new Select().from(CategoryBean.class).orderBy("orderNo").execute();
    }

}
