package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.JsonParser;
import com.rae.cnblogs.sdk.parser.ImagePostParser;
import com.rae.cnblogs.sdk.parser.SinaShotenParser;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 上传接口
 * Created by ChenRui on 2017/10/27 0027 15:09.
 */
public interface IPostApi {

    /**
     * 上传图片
     *
     * @param fileName       文件名称
     * @param headerFileName 文件名称
     * @param file           文件
     * @return 可访问的网络路径
     */
    @POST(ApiUrls.API_POST_IMAGE)
    @Headers({
            "X-Mime-Type: image/jpeg",
            "Content-Type: application/octet-stream",
            "Accept: */*",
            "Referer: https://www.cnblogs.com",
            "Origin: https://upload.cnblogs.com",
            "X-Requested-With: XMLHttpRequest"
    })
    @JsonParser(ImagePostParser.class)
    Observable<String> uploadImage(@Query("qqfile") String fileName, @Header("X-File-Name") String headerFileName, @Body RequestBody file);


    /**
     * 新浪短链接接口
     *
     * @param weiboAppId 微博APPID
     * @param url        长连接
     * @return
     */
    @GET(ApiUrls.API_SINA_SHORTEN)
    @JsonParser(SinaShotenParser.class)
    Observable<String> shotUrl(@Query("source") String weiboAppId, @Query("url_long") String url);
}
