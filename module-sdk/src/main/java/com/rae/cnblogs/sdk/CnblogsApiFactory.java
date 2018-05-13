package com.rae.cnblogs.sdk;

import android.content.Context;

/**
 * 博客园接口实例化
 * Created by ChenRui on 2016/11/28 23:38.
 */
public final class CnblogsApiFactory {

    private static CnblogsApiProvider sProvider;

    /**
     * SDK CLASS 文件位置
     */
    private static final String CLASS_PATCH_NAME = "sdk-hotfix.patch";
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
        sProvider = createDefault(context);
//
//
//        debug("======================================");
//        debug("======== create sdk provider =========");
//
//        // 没有class文件
//        if (!hasClassFile(context)) {
//            sProvider = sysProvider;
//            error("class patch is not exist!");
//            debug("this provider is system!");
//            return;
//        }
//
//        debug("start load class patch.");
//        // 开始动态加载 class
//        CnblogsApiProvider localProvider = createLocalProvider(context, sysProvider);
//        if (localProvider == null) {
//            debug("this provider is system!");
//            sProvider = sysProvider;
//        } else {
//            debug("this provider is local patch! ");
//            debug(localProvider.toString());
//            sProvider = localProvider;
//        }
//        Log.d(TAG, "======================================");
    }

//    @Nullable
//    private static CnblogsApiProvider createLocalProvider(Context context, CnblogsApiProvider sysProvider) {
//        try {
//            File dir = new File(context.getExternalCacheDir(), CLASS_PATCH_NAME);
//            String filePath = dir.getPath();
//            String outputPath = context.getCacheDir().getPath();
//            DexClassLoader dexClassLoader = new DexClassLoader(filePath, outputPath, null, context.getClassLoader());
//            Class<?> cls = dexClassLoader.loadClass(PatchCnblogsApiProvider.class.getName());
//            Constructor<?> constructor = cls.getConstructor(Context.class);
//            return (CnblogsApiProvider) constructor.newInstance(context);
//        } catch (Throwable e) {
//            error("load class exception! ");
//            error(Log.getStackTraceString(e));
////            CrashReport.postCatchedException(new CnblogsApiException("加载SDK异常", e));
//        }
//        return null;
//    }
//

//
//    /**
//     * 本地是否有class包文件
//     */
//    private static boolean hasClassFile(Context context) {
//        File dir = new File(context.getExternalCacheDir(), CLASS_PATCH_NAME);
//        debug("find class patch is: ", dir.getPath());
//        return dir.exists() & dir.canRead();
//    }
//
//    private static void error(String... msg) {
//        log(1, msg);
//    }
//
//    private static void debug(String... msg) {
//        log(0, msg);
//    }
//
//    private static void log(int level, String... msg) {
//        StringBuilder sb = new StringBuilder();
//        for (String txt : msg) {
//            sb.append(txt);
//        }
//        if (level == 1)
//            Log.e(TAG, sb.toString());
//        else
//            Log.d(TAG, sb.toString());
//    }
}
