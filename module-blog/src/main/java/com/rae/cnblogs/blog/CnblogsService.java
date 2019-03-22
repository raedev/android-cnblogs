package com.rae.cnblogs.blog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rae.cnblogs.basic.AppDataManager;
import com.rae.cnblogs.blog.job.BlogContentJob;
import com.rae.cnblogs.blog.job.IJob;
import com.rae.cnblogs.blog.job.JobEvent;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.db.DbFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 博客园服务
 * 为博客园提供离线缓存服务，数据库自动清理服务
 * Created by rae on 2018/5/31.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class CnblogsService extends Service {

    @Nullable
    IJob mBlogContentJob;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("rae", "博客园服务启动了");
        EventBus.getDefault().register(this);
        checkCacheSize(); // 检查缓存空间大小
        // 下载补丁包
        downloadSdkPatch();
    }

    private void downloadSdkPatch() {
        OkHttpClient client = new OkHttpClient
                .Builder()
//                .connectTimeout(5, TimeUnit.MINUTES)
//                .readTimeout(5, TimeUnit.MINUTES)
//                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        // 默认补丁包下载地址
        String downloadUrl = CnblogsApiFactory.getInstance(this).getDownloadUrl();
        Request request = new Request.Builder().url(downloadUrl).build();
        Log.d("CnblogsService", "下载SDK补丁包：" + downloadUrl);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("CnblogsService", "下载sdk补丁包失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) {
                    Log.e("CnblogsService", "下载sdk补丁包失败，响应内容为空！");
                    return;
                }
                InputStream inputStream = body.byteStream();
                CnblogsApiFactory.savePatchFile(CnblogsService.this, inputStream);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mBlogContentJob != null)
            mBlogContentJob.cancel();
    }

    @Subscribe
    public void onEvent(JobEvent event) {
        int action = event.getAction();
        if (action == JobEvent.ACTION_JOB_BLOG_CONTENT) {
            if (mBlogContentJob == null) {
                mBlogContentJob = new BlogContentJob(this);
            } else {
                mBlogContentJob.run();
            }
        }
    }

    /**
     * 检查缓存大小，超过大小自动清理
     */
    private void checkCacheSize() {
        try {
            AppDataManager appDataManager = new AppDataManager(this);
            boolean isInsufficient = appDataManager.isInsufficient(); // 是否空间不足
            double dbSize = appDataManager.getDatabaseTotalSize();
            Log.i("rae", "是否空间不够：" + isInsufficient + "; 数据库缓存大小：" + dbSize);
            // 当数据大于30MB，清空博客缓存数据
            if (dbSize > 120 || isInsufficient) {
                Log.i("rae-service", "清除数据！" + dbSize);
                DbFactory.getInstance().clearCache();
            }

            // 清除缓存目录
            long cacheSize = appDataManager.getCacheSize();
            // 缓存大小超过600mb，或者空间不足的时候清除缓存目录
            if (cacheSize > 600 || isInsufficient) {
                Log.i("rae-service", "清除缓存！" + cacheSize);
                appDataManager.clearCache();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
