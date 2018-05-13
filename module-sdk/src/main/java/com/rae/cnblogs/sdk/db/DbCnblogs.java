package com.rae.cnblogs.sdk.db;

import android.content.Context;

import com.rae.cnblogs.sdk.bean.DaoMaster;
import com.rae.cnblogs.sdk.bean.DaoSession;

import org.greenrobot.greendao.database.Database;


/**
 * 本地数据库
 * Created by ChenRui on 2016/12/1 00:24.
 */
public class DbCnblogs {

    private static DbCnblogs sDbCnblogs;

    public static void init(Context context) {
        if (sDbCnblogs == null)
            sDbCnblogs = new DbCnblogs(context);
    }

    public static DaoSession getSession() {
        return sDbCnblogs.getDaoSession();
    }


    private final DaoSession mDaoSession;

    public DbCnblogs() {
        this(null);
    }

    public DbCnblogs(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "cnblogs-dao.db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }


    protected void executeTransaction(Runnable runnable) {
//        try {
//            ActiveAndroid.beginTransaction();
//            runnable.run();
//            ActiveAndroid.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                ActiveAndroid.endTransaction();
//            } catch (SQLiteFullException e) {
//                // 数据库缓存已经满了，清除缓存
//                // fix bug #690
//                new Delete().from(UserBlogInfo.class).execute();
//                new Delete().from(BlogBean.class).execute();
//                ActiveAndroid.clearCache();
//            }
//        }
    }


}
