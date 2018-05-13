package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.parser.FriendsBlogListParser;
import com.rae.cnblogs.sdk.parser.FriendsInfoParser;
import com.rae.cnblogs.sdk.parser.FriendsListParser;
import com.rae.cnblogs.sdk.parser.UserTimelineParser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 朋友圈/社交圈接口
 * Created by ChenRui on 2017/2/7 0007 15:24.
 */
public interface IFriendsApi {

    /**
     * 获取博主的博客列表
     *
     * @param page    页码
     * @param blogApp 博主ID
     */
    @GET(ApiUrls.API_FRIENDS_BLOG_LIST)
    @Parser(FriendsBlogListParser.class)
    Observable<List<BlogBean>> getBlogList(@Path("page") int page, @Path("blogApp") String blogApp);

    /**
     * 获取关注和粉丝个数
     */
    @GET(ApiUrls.API_USER_CENTER)
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
    })
    @Parser(FriendsInfoParser.class)
    Observable<FriendsInfoBean> getFriendsInfo(@Path("blogApp") String blogApp);


    /**
     * 获取用户动态
     */
    @GET(ApiUrls.API_USER_FEED)
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
    })
    @Parser(UserTimelineParser.class)
    Observable<List<UserFeedBean>> getFeeds(@Path("page") int page, @Path("blogApp") String blogApp);

    /**
     * 关注博主
     *
     * @param userId 博主
     */
    @POST(ApiUrls.API_FRIENDS_FOLLOW)
    @FormUrlEncoded
//    @Headers(JsonBody.CONTENT_TYPE)
    Observable<Empty> follow(@Field("userId") String userId);

    /**
     * 取消关注
     *
     * @param userId
     */
    @POST(ApiUrls.API_FRIENDS_UN_FOLLOW)
    @FormUrlEncoded
//    @Headers(JsonBody.CONTENT_TYPE)
    Observable<Empty> unFollow(@Field("userId") String userId);


    /**
     * 获取我的关注列表
     *
     * @param blogApp
     * @param page    页码
     */
    @GET(ApiUrls.API_FRIENDS_FOLLOW_LIST)
    @Parser(FriendsListParser.class)
    Observable<List<UserInfoBean>> getFollowList(@Path("blogApp") String blogApp, @Query("page") int page);

    /**
     * 获取我的关注列表
     *
     * @param blogApp
     * @param page    页码
     */
    @GET(ApiUrls.API_FRIENDS_FANS_LIST)
    @Parser(FriendsListParser.class)
    Observable<List<UserInfoBean>> getFansList(@Path("blogApp") String blogApp, @Query("page") int page);
}
