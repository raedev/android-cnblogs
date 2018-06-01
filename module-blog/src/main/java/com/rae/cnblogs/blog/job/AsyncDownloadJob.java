package com.rae.cnblogs.blog.job;

import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步下载任务
 * Created by ChenRui on 2017/7/27 0027 15:59.
 */
abstract class AsyncDownloadJob implements IJob {

    private final ScheduledThreadPoolExecutor mExecutorService;

    AsyncDownloadJob() {
        mExecutorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
    }

    void execute(Runnable runnable) {
        if (mExecutorService == null || mExecutorService.isTerminated()) {
            Log.e("rae", "线程池已经结束！");
            return;
        }
        try {
            // 延迟周期运行，避免过多的CPU消耗
            mExecutorService.schedule(runnable, 2000, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            // 上传异常
            CrashReport.postCatchedException(e);
        }
    }

    @Override
    public void cancel() {
        mExecutorService.shutdown();
    }
}
