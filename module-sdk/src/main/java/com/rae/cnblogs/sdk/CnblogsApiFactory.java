package com.rae.cnblogs.sdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;

import dalvik.system.DexClassLoader;

/**
 * 博客园接口实例化
 * Created by ChenRui on 2016/11/28 23:38.
 */
public final class CnblogsApiFactory {

    private static CnblogsApiProvider sProvider;
    /**
     * SDK CLASS 文件位置
     */
    private static final String CLASS_PATCH_NAME = "hotfix.patch";
    private static final String TAG = "CnblogsApiFactory";

    public static CnblogsApiProvider getInstance(Context context) {
        if (sProvider == null) {
            synchronized (CnblogsApiFactory.class) {
                if (sProvider == null) {
                    initProvider(context);
                }
            }
        }
        return sProvider;
    }

    /**
     * 实例化默认的接口工厂实例
     */
    private static CnblogsApiProvider createDefault(Context context) {
        return new DefaultCnblogsApiProvider(context);
    }

    /**
     * 初始化接口提供程序，提供动态更新sdk的功能。
     * 1、加载本地class文件，加载失败后默认加载系统的。把异常信息上报。
     * 2、对比系统版本号跟本地版本号，如果较新则加载。     *
     *
     * @param context 提供程序
     */
    private static void initProvider(Context context) {
        // 系统默认提供者
        CnblogsApiProvider defaultProvider = createDefault(context);

        Log.d(TAG, "==============================================");
        Log.d(TAG, "======== create cnblogs sdk provider =========");
        // 加载本地的热更新包
        CnblogsApiProvider patchProvider = createPatchProvider(context);
        // 如果本地的热更新包版本大于默认的版本，则提供者设置为热更新包的
        if (patchProvider != null && patchProvider.getApiVersion() > defaultProvider.getApiVersion()) {
            sProvider = patchProvider;
        } else {
            sProvider = defaultProvider;
        }
        Log.d(TAG, "======== provider is: " + sProvider.toString());
        Log.d(TAG, "==============================================");
    }

    private static File getPatchFile(Context context) {
        return new File(context.getExternalCacheDir(), CLASS_PATCH_NAME);
    }

    /**
     * 创建热补丁的接口提供者
     */
    @Nullable
    private static CnblogsApiProvider createPatchProvider(Context context) {
        try {
            File file = getPatchFile(context);
            String filePath = file.getAbsolutePath();
            if (!file.exists()) {
                Log.e(TAG, "======== 热补丁文件不存在：" + filePath);
                return null;
            }
            String outputPath = context.getFilesDir().getAbsolutePath();
            Log.d(TAG, "======== 补丁文件：" + filePath);
            DexClassLoader dexClassLoader = new DexClassLoader(filePath, outputPath, outputPath, context.getClassLoader());
            Class<?> cls = dexClassLoader.loadClass("com.rae.cnblogs.sdk.PatchCnblogsApiProvider");
            Constructor<?> constructor = cls.getConstructor(Context.class);
            return (CnblogsApiProvider) constructor.newInstance(context.getApplicationContext());
        } catch (Throwable e) {
            Log.e(TAG, "加载SDK异常", e);
            deletePatchFile(context); // 加载异常了，删除补丁包
            CrashReport.postCatchedException(new CnblogsApiException("加载SDK异常", e));
        }
        return null;
    }

    /**
     * 删除补丁包版本接口，并恢复默认的接口
     */
    public static void reset(Context context) {
        deletePatchFile(context);
        sProvider = createDefault(context.getApplicationContext());
    }

    public static int getVersion() {
        return sProvider == null ? 0 : sProvider.getApiVersion();
    }

    /**
     * 删除补丁文件
     */
    private static void deletePatchFile(Context context) {
        try {
            File patchFile = getPatchFile(context);
            if (patchFile.exists()) {
                boolean delete = patchFile.delete();
                Log.d(TAG, "删除文件：" + patchFile.getPath() + " --> " + delete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存补丁文件
     */
    public static void savePatchFile(Context context, InputStream stream) {
        try {
            // 保存到临时文件
            File tempFile = new File(context.getExternalCacheDir(), "hotfix.tmp");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            int len = 128;
            byte[] buffer = new byte[len];
            while ((len = stream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            File patchFile = getPatchFile(context);
            if (patchFile.exists()) patchFile.delete();
            if (tempFile.renameTo(patchFile)) {
                // 重新加载提供者
                initProvider(context.getApplicationContext());
                Log.i(TAG, "下载补丁包成功，当前接口为：" + sProvider.toString());
            } else {
                Log.w(TAG, "下载补丁包失败，路径：" + tempFile.getPath());
            }
        } catch (Exception e) {
            Log.e(TAG, "SDK补丁包保存失败", e);
        }

    }
}
