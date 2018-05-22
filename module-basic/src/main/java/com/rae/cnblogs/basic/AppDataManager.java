package com.rae.cnblogs.basic;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 应用程序数据管理器
 * Created by ChenRui on 2017/7/28 0028 21:57.
 */
public final class AppDataManager {

    private Context mContext;

    public AppDataManager(Context context) {
        mContext = context;
    }

    /**
     * 清除应用缓存
     */
    public void clearCache() {
        // 清除文件缓存
        deleteDir(mContext.getExternalCacheDir());
        deleteDir(mContext.getCacheDir());
        clearWebViewCache();
    }

    //清理WebView缓存数据库
    public void clearWebViewCache() {
        try {

            // 兼容高版本：/data/data/com.rae.cnblogs/app_webview
            deleteDir(new File(mContext.getCacheDir().getParent(), "app_webview"));

            mContext.deleteDatabase("webview.db");
            mContext.deleteDatabase("webviewCache.db");
            // 清除文件缓存
            //WebView 缓存文件
            File appCacheDir = new File(mContext.getFilesDir().getAbsolutePath() + "/webcache");
            File webviewCacheDir = new File(mContext.getCacheDir().getAbsolutePath() + "/webviewCache");
            deleteDir(appCacheDir);
            deleteDir(webviewCacheDir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SDCard总大小
     *
     * @return 单位MB
     */
    public long getSDCardTotalSize() {
        File file = Environment.getExternalStorageDirectory();
        if (!file.exists() || !file.canRead()) return -1;
        StatFs stat = new StatFs(file.getPath());
        if (Build.VERSION.SDK_INT > 18) {
            return (stat.getBlockSizeLong() * stat.getBlockCountLong()) / 1048576;
        } else {
            return (stat.getBlockSize() * stat.getBlockCount()) / 1048576;
        }
    }

    /**
     * 获取SDCard可用空间
     *
     * @return 单位MB
     */
    public long getSDCardFreeSpace() {
        File file = Environment.getExternalStorageDirectory();
        if (!file.exists() || !file.canRead()) return -1;
        StatFs stat = new StatFs(file.getPath());
        if (Build.VERSION.SDK_INT > 18) {
            return (stat.getAvailableBlocksLong() * stat.getBlockSizeLong()) / 1048576;
        } else {
            return (stat.getAvailableBlocks() * stat.getBlockSize()) / 1048576;
        }
    }

    /**
     * 获取缓存大小
     *
     * @return 返回单位MB
     */
    public long getCacheSize() {
        File cacheDir = mContext.getCacheDir();
        File extCacheDir = mContext.getExternalCacheDir();
        return (getDirectorySize(cacheDir) + getDirectorySize(extCacheDir)) / 1048576;
    }

    /**
     * 是否空间不足，条件：可用空间小于1GB
     */
    public boolean isInsufficient() {
        long size = getSDCardFreeSpace();
        return size < 1024 && size != -1;
    }

    /**
     * 数据库总大小
     *
     * @return 返回单位MB
     */
    public double getDatabaseTotalSize() {
        File dbFile = mContext.getDatabasePath("cnblogs").getParentFile();
        return getDirectorySize(dbFile) / 1048576.0f; // MB
    }


    /**
     * 获取文件夹大小
     *
     * @param file 文件
     */
    public long getDirectorySize(File file) {
        if (!file.exists() || !file.canRead()) {
            return 0;
        }

        if (!file.isDirectory()) {
            return file.length();
        }

        long size = 0;
        File[] files = file.listFiles();
        if (files == null) return 0;
        for (File item : files) {
            if (item.isDirectory()) {
                size += getDirectorySize(item);
            } else {
                size += item.length();
            }
        }
        return size;
    }

    /**
     * 递归删除文件夹
     *
     * @param file
     * @return
     */
    public void deleteDir(File file) {
        if (file != null && file.exists()) {
            try {
                if (file.isDirectory()) {
                    // 删除文件夹
                    File[] files = file.listFiles();
                    if (files.length <= 0) {
                        file.delete(); // 文件夹类型
                        return;
                    }
                    for (File item : files) {
                        deleteDir(item); // 递归删除子文件夹
                    }
                } else {
                    file.delete(); // 文件类型
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
