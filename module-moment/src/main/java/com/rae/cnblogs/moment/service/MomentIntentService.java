package com.rae.cnblogs.moment.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BitmapCompressor;
import com.rae.cnblogs.moment.PostMomentActivity;
import com.rae.cnblogs.moment.R;
import com.rae.cnblogs.resource.BuildConfig;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.ApiErrorCode;
import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.api.IPostApi;
import com.rae.cnblogs.sdk.event.PostMomentEvent;
import com.rae.cnblogs.sdk.model.ImageMetaData;
import com.rae.cnblogs.sdk.model.MomentMetaData;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.HttpException;

/**
 * 闪存上传
 * Created by ChenRui on 2017/10/31 0031 14:37.
 */
public class MomentIntentService extends IntentService {

    private static final String TAG = "MomentIntentService";
    // 线程同步
    private final CountDownLatch mCountDownLatch = new CountDownLatch(1);

    IMomentApi mMomentApi;
    IPostApi mPostApi;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private MomentMetaData mMomentMetaData;
    private final int mNotificationId = 10891;

    public MomentIntentService() {
        super("MomentIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        mMomentMetaData = null;
        if (intent != null) {
            mMomentMetaData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }

        // 没有数据
        if (mMomentMetaData == null) {
            Log.e(TAG, "没有上传数据");
            stopSelf();
            return;
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mPostApi = CnblogsApiFactory.getInstance(getApplicationContext()).getPostApi();
        mMomentApi = CnblogsApiFactory.getInstance(getApplicationContext()).getMomentApi();

        mNotification = createNotificationBuilder().build();


        sendNotification(mNotification);

        Log.d(TAG, "发布闪存服务：\n" + mMomentMetaData.content);

        // 开始上传图片
        Observable.fromIterable(mMomentMetaData.images)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                // 压缩图片
                .map(new Function<ImageMetaData, ImageMetaData>() {

                    @Override
                    public ImageMetaData apply(ImageMetaData imageMetaData) throws Exception {
                        // 压缩图片
                        imageMetaData.setLocalPath(composeImage(imageMetaData.localPath));
                        return imageMetaData;
                    }

                    /**
                     * 压缩图片小于2M
                     */
                    private String composeImage(String filePath) throws IOException {
                        // 复制图片
                        String output = new File(getExternalCacheDir(), "temp" + System.currentTimeMillis() + ".jpg").getAbsolutePath();
                        FileOutputStream fileOutputStream = new FileOutputStream(output);
                        FileInputStream fileInputStream = new FileInputStream(filePath);
                        int len;
                        byte[] buffer = new byte[128];
                        while ((len = fileInputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.flush();
                        fileInputStream.close();
                        fileOutputStream.close();

                        Log.d(TAG, "复制图片：" + filePath + " 到 " + output);

                        BitmapFactory.Options opt = new BitmapFactory.Options();
                        opt.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(output, opt);

//                        if (opt.outWidth > opt.outHeight) {
//                            opt.inSampleSize = opt.outWidth / 320;
//                        } else {
//                            opt.inSampleSize = opt.outHeight / 680;
//                        }

                        final int reqWidth = 720;
                        final int reqHeight = 1280;

                        if (opt.outHeight > reqHeight || opt.outWidth > reqWidth) {
                            final int heightRatio = Math.round((float) opt.outHeight / (float) reqHeight);
                            final int widthRatio = Math.round((float) opt.outWidth / (float) reqHeight);
                            opt.inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;//用最大
                        }


                        //避免出现内存溢出的情况，进行相应的属性设置。
                        opt.inPreferredConfig = Bitmap.Config.RGB_565;
                        opt.inJustDecodeBounds = false;
                        opt.inDither = true;
                        opt.inScaled = true;

                        Bitmap bmp = BitmapFactory.decodeFile(output, opt);
                        if (bmp == null) {
                            throw new IOException("图片加载失败，可能由于图片太大了");
                        }
                        Log.d(TAG, "图片压缩前大小：" + output + "--> " + bmp.getByteCount());
                        Bitmap result = BitmapCompressor.compressBitmap(bmp, 4096);
                        File file = new File(output);
                        FileOutputStream stream = new FileOutputStream(file);
                        result.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        stream.close();
                        stream.flush();

                        Log.d(TAG, "图片压缩后大小：" + output + "--> " + result.getByteCount());

                        return output;
                    }
                })
                // 上传图片
                .flatMap(new Function<ImageMetaData, ObservableSource<ImageMetaData>>() {
                    @Override
                    public ObservableSource<ImageMetaData> apply(final ImageMetaData image) {
                        File file = new File(image.localPath);
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                        sendNotification(createNotificationBuilder().setContentText("正在上传：" + file.getPath()).build());
                        Log.d(TAG, "正在上传图片：" + image.localPath);
                        // 上传图片
                        return mPostApi
                                .uploadImage(file.getName(), file.getName(), fileReqBody)
                                .subscribeOn(Schedulers.io())
                                .map(new Function<String, ImageMetaData>() {
                                    @Override
                                    public ImageMetaData apply(String url) {
                                        image.remoteUrl = url;
                                        return image;
                                    }
                                });
                    }
                })
                // 转换短连接
                .flatMap(new Function<ImageMetaData, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final ImageMetaData data) {
                        Log.d(TAG, "正在转换短连接：" + data.remoteUrl);

                        // 到这里图片上传成功，删除临时图片
                        boolean delete = new File(data.localPath).delete();
                        Log.d(TAG, "删除图片：[" + delete + "] " + data.localPath);

                        return mPostApi.shotUrl(BuildConfig.WEIBO_APP_ID, data.remoteUrl)
                                .map(new Function<String, String>() {
                                    @Override
                                    public String apply(String url) {
                                        Log.d(TAG, "短连接生成成功：" + data.remoteUrl + " --> " + url);
                                        data.remoteUrl = url; // 保存远程路径
                                        return data.remoteUrl;
                                    }
                                });
                    }
                })
                .toSortedList()
                // 到这里，图片已经处理成功，调用接口发布
                .subscribe(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> urls) {
                        sendNotification(createNotificationBuilder().setContentText("图片上传成功，发布中...").build());
                        // 闪存文本的图片处理
                        StringBuilder sb = new StringBuilder(mMomentMetaData.content);
                        sb.append("#img");
                        int size = urls.size();
                        for (int i = 0; i < size; i++) {
                            sb.append(urls.get(i));
                            sb.append(" ");
                        }
                        sb.append("#end");

                        // 发布闪存

                        mMomentApi.publish(sb.toString(), mMomentMetaData.flag)
                                .observeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ApiDefaultObserver<Empty>() {
                                    @Override
                                    protected void onError(String message) {
                                        notifyUploadFailed(message);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        // 统计错误
                                        AppMobclickAgent.onClickEvent(getApplicationContext(), "PostMoment_Error");
                                        // 上报错误信息
                                        CrashReport.postCatchedException(new CnblogsApiException("闪存发布失败！[publish]", e));
                                    }

                                    @Override
                                    protected void accept(Empty empty) {
                                        // 统计成功
                                        AppMobclickAgent.onClickEvent(getApplicationContext(), "PostMoment_Success");

                                        // 成功，跳转到首页
                                        Intent intent = new Intent();
                                        intent.setClassName(getApplicationContext(), "com.rae.cnblogs.home.MainActivity");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        intent.putExtra("event", PostMomentEvent.class.getName());
                                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                                .setContentTitle("闪存发布成功")
                                                .setContentText("点击查看")
                                                .setAutoCancel(true)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setTicker("闪存发布成功")
                                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                                .setContentIntent(pendingIntent)
                                                .build();

                                        sendNotification(notification);

                                        // 发送应用内事件
                                        EventBus.getDefault().post(new PostMomentEvent(mNotificationId, true, null));
                                        mCountDownLatch.countDown();
                                        stopSelf();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "发布闪存失败", e);
                        // 统计错误
                        AppMobclickAgent.onClickEvent(getApplicationContext(), "PostMoment_Error");

                        // 上报错误信息
                        CrashReport.postCatchedException(new CnblogsApiException("闪存发布失败！", e));

                        if (e instanceof CnblogsApiException) {
                            if (((CnblogsApiException) e).getCode() == ApiErrorCode.LOGIN_EXPIRED) {
                                onError("登录过期");
                                return;
                            }
                        } else if (e instanceof FileNotFoundException) {
                            // 权限问题
                            String message;
                            if (e.getMessage() != null && e.getMessage().contains("Permission")) {
                                message = "没有权限访问图片，请检查是否授权访问照相机/相册/存储卡权限。";
                            } else {
                                message = "没找到上传的图片";
                            }
                            onError(message);
                            return;
                        } else if (e instanceof HttpException) {
                            HttpException ex = (HttpException) e;
                            onError("服务器发生错误0x" + ex.code());
                            return;
                        } else if (e instanceof UnknownHostException) {
                            onError("网络连接错误，请检查网络连接");
                            return;
                        }


                        String message = com.rae.cnblogs.sdk.BuildConfig.DEBUG ? e.getMessage() : "数据加载失败，请重试";
                        if (TextUtils.isEmpty(message)) {
                            message = "接口信息异常";
                        }
                        onError(message);
                    }

                    public void onError(String msg) {
                        notifyUploadFailed(msg);
                    }

                    public void notifyUploadFailed(String msg) {

                        Intent intent = new Intent(getApplicationContext(), PostMomentActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, mMomentMetaData);
                        intent.putExtra(Intent.EXTRA_HTML_TEXT, msg);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        mNotification = new NotificationCompat.Builder(getApplicationContext())
                                .setAutoCancel(true)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("闪存发布失败")
                                .setTicker("闪存发布失败")
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setContentText("点击重试")
                                .setContentIntent(pendingIntent)
                                .build();
                        sendNotification(mNotification);

                        // 发送应用内事件
                        PostMomentEvent event = new PostMomentEvent(mNotificationId, false, msg);
                        event.setMomentMetaData(mMomentMetaData);
                        EventBus.getDefault().post(event);

                        mCountDownLatch.countDown();
                        stopSelf();
                    }
                });


        try {
            mCountDownLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Log.e(TAG, "上传超时！", e);
        }
    }

    private android.support.v4.app.NotificationCompat.Builder createNotificationBuilder() {
        return new NotificationCompat.Builder(this)
                .setContentTitle("正在发布闪存")
                .setContentText("准备中")
                .setTicker("正在进入后台发布闪存")
                .setProgress(0, 100, true)
                .setSmallIcon(R.mipmap.ic_launcher);
    }

    private void sendNotification(Notification notification) {
        if (mNotificationManager != null && notification != null)
            mNotificationManager.notify(mNotificationId, notification);
    }
}
