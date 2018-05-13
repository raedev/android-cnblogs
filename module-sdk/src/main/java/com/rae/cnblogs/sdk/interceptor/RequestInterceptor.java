package com.rae.cnblogs.sdk.interceptor;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.rae.cnblogs.sdk.BuildConfig;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Set;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 请求拦截器，用来处理请求参数
 * Created by ChenRui on 2017/5/25 0025 0:12.
 */
public class RequestInterceptor implements Interceptor {

    private final ConnectivityManager mConnectivityManager;
    private String versionName;
    private String packageName;
    private int versionCode;

    public static RequestInterceptor create(Context context) {
        return new RequestInterceptor(context);
    }

    RequestInterceptor(Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        CookieSyncManager.createInstance(context.getApplicationContext());
        try {
            this.packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            this.versionName = packageInfo.versionName == null ? "0.0.0" : packageInfo.versionName;
            this.versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            this.versionName = "1.0.0";
            e.printStackTrace();
        }
    }

    private boolean isConnected() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && (networkInfo.isConnected() && networkInfo.isAvailable());
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        // 请求处理参数处理
        Request request = chain.request();

        Request.Builder newBuilder = request.newBuilder();

        // 添加版本号
        if (request.url().host().contains("rae")) {
            newBuilder.addHeader("APP-PACKAGE-NAME", this.packageName);
            newBuilder.addHeader("APP-VERSION-NAME", this.versionName);
            newBuilder.addHeader("App-CHANNEL", CnblogAppConfig.APP_CHANNEL);
            newBuilder.addHeader("App-ENV", BuildConfig.BUILD_TYPE);
            newBuilder.addHeader("APP-VERSION-CODE", String.valueOf(this.versionCode));
        }

        // 使用Chrome的User-Agent
        newBuilder
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                .removeHeader("Accept-Language")
                .addHeader("Accept-Language", " zh-CN,zh;q=0.8");

        // [重要] 带上COOKIE，保持登录需要用到
        String cookie = CookieManager.getInstance().getCookie("http://www.cnblogs.com");
        if (!TextUtils.isEmpty(cookie)) {
            // 同步WebKit Cookie 到 JavaNetCookieJar
            UserProvider.getInstance().cookieManager2CookieJar();
        }

        // 同步手动设置的cookie
        cookie = request.header("Cookie");
        if (!TextUtils.isEmpty(cookie)) {
            UserProvider.getInstance().cookieManager2CookieJar(cookie);
        }

        // 首页列表缓存特殊处理
        if (request.url().toString().contains("PostList") && !this.isConnected()) {
            newBuilder.cacheControl(CacheControl.FORCE_CACHE).build();
        }

        if ("post".equalsIgnoreCase(request.method())) {
            // 将URL的参数转换到body里面去
            newBuilder = convertParamToBody(request, newBuilder);
        }

        // 发起请求
        request = newBuilder.build();
        return chain.proceed(request);
    }

    /**
     * 转换请求参数
     *
     * @param request    请求
     * @param newBuilder 新的请求构建者
     */
    private Request.Builder convertParamToBody(Request request, Request.Builder newBuilder) {
        HttpUrl url = request.url();
        RequestBody body = request.body();
        Set<String> names = url.queryParameterNames();
        if (names == null) {
            return newBuilder;
        }

        if (body == null) {
            body = new FormBody.Builder().build();
        }

        // 这里有两种类型：formBody jsonBody
        MediaType mediaType = body.contentType();
        String contentType = mediaType == null ? null : mediaType.toString();

        // 表单类型
        if (body instanceof FormBody) {
            FormBody.Builder builder = copyFormBody((FormBody) body);
            HttpUrl.Builder httUrlBuilder = url.newBuilder();
            for (String name : names) {
                String value = url.queryParameter(name);
                builder.add(name, value);
                httUrlBuilder.removeAllQueryParameters(name);
            }
            FormBody formBody = builder.build();
            return newBuilder.url(httUrlBuilder.build()).post(formBody);
        }

        // JSON BODY
        if (TextUtils.equals(contentType, JsonBody.MEDIA_TYPE)) {
            JsonBody.Builder jsonBody = new JsonBody.Builder();
            HttpUrl.Builder httpUrlBuilder = url.newBuilder();

            // 加上原来的参数
            Buffer sink = new Buffer();
            try {
                body.writeTo(sink);
                String params = sink.readString(Charset.defaultCharset());
                HttpUrl oldUrl = httpUrlBuilder.query(URLDecoder.decode(params)).build();
                Set<String> oldNames = oldUrl.queryParameterNames();
                for (String name : oldNames) {
                    String value = oldUrl.queryParameter(name);
                    jsonBody.add(name, value);
                    // 移除参数
                    httpUrlBuilder.removeAllQueryParameters(name);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            // URL的参数
            for (String name : names) {
                String value = url.queryParameter(name);
                jsonBody.add(name, value);
                //httpUrlBuilder.removeAllQueryParameters(name);
            }

            return newBuilder.url(httpUrlBuilder.build()).post(jsonBody.build());
        }

        return newBuilder;
    }

    /**
     * 复制表单数据
     */
    private FormBody.Builder copyFormBody(FormBody body) {
        FormBody.Builder builder = new FormBody.Builder();
        int size = body.size();
        for (int i = 0; i < size; i++) {
            builder.add(body.name(i), body.value(i));
        }
        return builder;
    }
}
