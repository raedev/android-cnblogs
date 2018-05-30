package com.rae.cnblogs.sdk.db;

import android.content.Context;
import android.os.Environment;

import com.rae.cnblogs.sdk.bean.DaoMaster;
import com.rae.cnblogs.sdk.bean.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.io.File;


/**
 * 本地数据库
 * Created by ChenRui on 2016/12/1 00:24.
 */
public final class DbCnblogs {

    private static DbCnblogs sDbCnblogs;
    private Context mContext;

    public static void init(Context context) {
        if (sDbCnblogs == null) {
            synchronized (DbCnblogs.class) {
                if (sDbCnblogs == null) {
                    sDbCnblogs = new DbCnblogs(context);
                }
            }
        }
    }

    public static DaoSession getSession() {
        return sDbCnblogs.getDaoSession();
    }

    public static DbCnblogs getInstance() {
        return sDbCnblogs;
    }


    private final DaoSession mDaoSession;

    public DbCnblogs() {
        this(null);
    }

    public DbCnblogs(Context context) {
        mContext = context;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "cnblogs_v2.db", null);
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
        transfer(context); // 数据迁移
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    /**
     * 数据库升级做的迁移操作
     */
    public void transfer(Context context) {
        //  把用户的分类移到新的数据库，并删除旧的数据库
//        SQLiteOpenHelper helper = new SQLiteOpenHelper(context,"") {
//            @Override
//            public void onCreate(SQLiteDatabase db) {
//
//            }
//
//            @Override
//            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//            }
//        };
    }


    /**
     * 获取缓存目录
     */
    public File getCacheDir() {
        File dir = mContext.getExternalFilesDir("blog");
        if (dir != null && dir.exists() && dir.canWrite()) {
            return dir;
        }

        // 如果没有，获取系统默认的
        dir = mContext.getCacheDir();
        if (dir != null && dir.exists() && dir.canWrite()) {
            return dir;
        }

        // 再者没有，获取根目录的
        return Environment.getExternalStorageDirectory();
    }

}
