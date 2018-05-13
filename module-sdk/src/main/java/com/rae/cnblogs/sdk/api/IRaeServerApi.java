package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.cnblogs.sdk.bean.SystemMessageBean;
import com.rae.cnblogs.sdk.bean.VersionInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 个人接口
 * Created by ChenRui on 2016/12/22 22:57.
 */
public interface IRaeServerApi {

    /**
     * 获取启动页内容
     */
    @GET(ApiUrls.RAE_API_AD_LAUNCHER)
    Observable<AdvertBean> getLauncherAd();


    /**
     * 获取系统消息
     */
    @GET(ApiUrls.RAE_API_MESSAGE)
    Observable<List<SystemMessageBean>> getMessages();

    /**
     * 获取系统消息个数
     */
    @GET(ApiUrls.RAE_API_MESSAGE_COUNT)
    Observable<Integer> getMessageCount();

    /**
     * 检查更新
     *
     * @param versionCode 版本代码
     * @param channel     渠道
     * @param env         环境，这里使用buildType
     */
    @GET(ApiUrls.RAE_API_CHECK_VERSION)
    Observable<VersionInfo> versionInfo(@Path("versionCode") int versionCode, @Query("versionName") String versionName, @Query("channel") String channel, @Query("env") String env);
}
