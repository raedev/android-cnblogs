package com.rae.cnblogs.blog.job;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * 博文任务
 * Created by ChenRui on 2017/7/27 0027 15:36.
 */
public class BlogContentJob extends AsyncDownloadJob {


    private final IBlogApi mBlogApi;
    private final INewsApi mNewsApi;
    private final ConnectivityManager mConnectivityManager;
    private DbBlog mDbBlog;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            performJob();
            return false;
        }
    });

    public BlogContentJob(Context context) {
        mBlogApi = CnblogsApiFactory.getInstance(context).getBlogApi();
        mNewsApi = CnblogsApiFactory.getInstance(context).getNewsApi();
        mDbBlog = DbFactory.getInstance().getBlog();
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void run() {
        // 延期执行，避免过多的刷新操作
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 10000);
    }

    @Override
    public void cancel() {
        super.cancel();
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    private boolean isWIFI() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 执行任务
     */
    private void performJob() {
        // 检查是否处于WiFi网络
        if (!isWIFI()) {
            Log.w("rae", "非WIFI网络，不执行异步下载博文任务");
            return;
        }

        // 查询没有内容的博客
        Observable.fromIterable(mDbBlog.findAllWithoutBlogContent())
                .subscribe(new DisposableObserver<BlogBean>() {
                    @Override
                    public void onNext(BlogBean blog) {
                        // 开始执行任务
                        execute(new BlogContentTask(mConnectivityManager, mBlogApi, mNewsApi, mDbBlog, blog.getBlogId()));
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
