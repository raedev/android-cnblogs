package com.rae.cnblogs.sdk.api;

import android.support.annotation.NonNull;

import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.parser.NewsAddCommentParser;
import com.rae.cnblogs.sdk.parser.NewsCommentParser;
import com.rae.cnblogs.sdk.parser.NewsContentParser;
import com.rae.cnblogs.sdk.parser.NewsDelCommentParser;
import com.rae.cnblogs.sdk.parser.NewsListParser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 新闻接口
 * Created by ChenRui on 2017/1/18 0018 18:24.
 */
public interface INewsApi {

    /**
     * 获取新闻列表
     *
     * @param page 页码
     */
    @GET(ApiUrls.API_NEWS_LIST)
    @Parser(NewsListParser.class)
    Observable<List<BlogBean>> getNews(@Path("page") int page);


    /**
     * 获取新闻内容
     *
     * @param newsId 新闻ID
     */
    @GET(ApiUrls.API_NEWS_CONTENT)
    @Parser(NewsContentParser.class)
    Observable<String> getNewsContent(@Path("id") String newsId);

    /**
     * [同步方式]获取新闻内容
     *
     * @param newsId 新闻ID
     */
    @GET(ApiUrls.API_NEWS_CONTENT)
    @Parser(NewsContentParser.class)
    Call<String> syncGetNewsContent(@Path("id") String newsId);


    /**
     * 获取评论列表
     *
     * @param newsId 新闻ID
     * @param page   页码
     */
    @GET(ApiUrls.API_NEWS_COMMENT)
    @Parser(NewsCommentParser.class)
    Observable<List<BlogCommentBean>> getNewsComment(@Query("contentId") String newsId, @Query("page") int page);

    /**
     * 发表新闻评论
     * <p>如果要引用评论，则content参数取值为： </p>
     * {@link com.rae.cnblogs.sdk.utils.ApiUtils#getCommentContent(BlogCommentBean, String)} 来获取转换的内容
     *
     * @param newsId          新闻ID
     * @param parentCommentId 引用回复评论ID，不能为空，或者空字符串，为0则发表新评论
     * @param content         评论内容
     */
    @POST(ApiUrls.API_NEWS_COMMENT_ADD)
    @FormUrlEncoded
    @Headers({JsonBody.XHR, JsonBody.CONTENT_TYPE})
    @Parser(NewsAddCommentParser.class)
    Observable<Empty> addNewsComment(@Field("ContentID") String newsId, @NonNull @Field("parentCommentId") String parentCommentId, @Field("Content") String content);


    /**
     * 删除新闻评论
     *
     * @param newsId    新闻ID
     * @param commentId 评论ID
     */
    @POST(ApiUrls.API_NEWS_COMMENT_DELETE)
    @FormUrlEncoded
    @Headers({JsonBody.XHR, JsonBody.CONTENT_TYPE})
    @Parser(NewsDelCommentParser.class)
    Observable<Empty> deleteNewsComment(@Field("contentID") String newsId, @Field("commentId") String commentId);


    /**
     * 点赞
     */
    @POST(ApiUrls.API_NEWS_LIKE)
    @FormUrlEncoded
    @Headers({JsonBody.XHR, JsonBody.CONTENT_TYPE})
    Observable<Empty> like(@Field("contentId") String newsId);

}
