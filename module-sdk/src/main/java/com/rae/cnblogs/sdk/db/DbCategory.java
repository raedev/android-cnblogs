package com.rae.cnblogs.sdk.db;

import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.bean.CategoryBeanDao;
import com.rae.cnblogs.sdk.bean.DaoSession;

import java.util.List;


/**
 * 分类表
 * Created by ChenRui on 2016/12/1 00:24.
 */
public class DbCategory {

    private final DaoSession mSession;

    DbCategory() {
        mSession = DbCnblogs.getSession();
    }

    /**
     * 重置分类
     *
     * @param list 数据
     */
    public void reset(final List<CategoryBean> list) {
        mSession.getCategoryBeanDao().deleteAll();
        mSession.getCategoryBeanDao().insertInTx(list);

    }

    public List<CategoryBean> list() {
        return mSession.getCategoryBeanDao().queryBuilder().orderAsc(CategoryBeanDao.Properties.OrderNo).list();
    }

}
