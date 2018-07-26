package com.rae.cnblogs.sdk.db;

import com.rae.cnblogs.sdk.db.model.DbSearchInfo;
import com.rae.cnblogs.sdk.db.model.DbSearchInfoDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class DbSearch {
    private final DbSearchInfoDao mSession;

    public DbSearch() {
        mSession = DbCnblogs.getSession().getDbSearchInfoDao();
    }

    public List<DbSearchInfo> getSearchHistory() {
        return mSession
                .queryBuilder()
                .where(DbSearchInfoDao.Properties.Type.eq("H"))
                .orderDesc(DbSearchInfoDao.Properties.CreateAt)
                .limit(100)
                .list();
    }


    public List<DbSearchInfo> getHotSearch() {
        return mSession
                .queryBuilder()
                .where(DbSearchInfoDao.Properties.Type.eq("C"))
                .list();
    }


    // 添加到搜索记录中
    public void addSearchHistory(String text) {
        DbSearchInfo m = new DbSearchInfo();
        m.setCreateAt(System.currentTimeMillis());
        m.setKeyword(text);
        m.setType("H"); // history
        mSession.insert(m);
    }


    /**
     * 删除历史记录
     */
    public void deleteSearchHistory(String text) {
        mSession.queryBuilder()
                .where(DbSearchInfoDao.Properties.Keyword.eq(text), DbSearchInfoDao.Properties.Type.eq("H"))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 清除历史记录
     */
    public void clearSearchHistory() {
        mSession.deleteAll();
    }


    /**
     * 缓存热门搜索
     */
    public void cacheHotSearch(List<String> data) {
        ArrayList<DbSearchInfo> models = new ArrayList<>();
        for (String item : data) {
            DbSearchInfo m = new DbSearchInfo();
            m.setKeyword(item);
            m.setType("C");
            m.setCreateAt(System.currentTimeMillis());
            models.add(m);
        }
        mSession.insertInTx(models);
    }

    /**
     * 清除热门搜索缓存
     */
    public void clearHotSearchCache() {
        mSession.queryBuilder()
                .where(DbSearchInfoDao.Properties.Type.eq("C"))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
