package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.JsonParser;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.parser.BlogCommentParser;
import com.rae.cnblogs.sdk.parser.BlogContentParser;
import com.rae.cnblogs.sdk.parser.BlogListParser;
import com.rae.cnblogs.sdk.parser.BlogOpenStatusParser;
import com.rae.cnblogs.sdk.parser.KBContentParser;
import com.rae.cnblogs.sdk.parser.KBListParser;

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
 * 博客相关API
 * Created by ChenRui on 2016/11/28 23:34.
 */
public interface IBlogApi {

    /**
     * 获取分类博客列表
     *
     * @param page       页码
     * @param parentId   父ID
     * @param categoryId 分类ID
     */
    @POST(ApiUrls.API_BLOG_LIST)
    @FormUrlEncoded
    @Parser(BlogListParser.class)
    Observable<List<BlogBean>> getBlogList(@Field("PageIndex") int page, @Field("CategoryType") String type, @Field("ParentCategoryId") String parentId, @Field("CategoryId") String categoryId);

    /**
     * 获取博客文章内容
     *
     * @param id 博客ID
     */
    @GET(ApiUrls.API_BLOG_CONTENT)
    @Parser(BlogContentParser.class)
    Observable<String> getBlogContent(@Path("id") String id);


    /**
     * [同步方式]获取博客文章内容
     *
     * @param id 博客ID
     */
    @GET(ApiUrls.API_BLOG_CONTENT)
    @Parser(BlogContentParser.class)
    Call<String> syncGetBlogContent(@Path("id") String id);

    /**
     * 获取评论列表
     *
     * @param id      博客ID
     * @param blogApp 博主ID
     */
    @GET(ApiUrls.API_BLOG_COMMENT_LIST)
    @JsonParser(BlogCommentParser.class)
    Observable<List<BlogCommentBean>> getBlogComments(@Query("pageIndex") int page, @Query("postId") String id, @Query("blogApp") String blogApp);

    /**
     * 分页获取知识库
     */
    @GET(ApiUrls.API_KB_LIST)
    @Parser(KBListParser.class)
    Observable<List<BlogBean>> getKbArticles(@Path("page") int page);

    /**
     * 获取知识库内容
     *
     * @param id 知识库ID
     */
    @GET(ApiUrls.API_KB_CONTENT)
    @Parser(KBContentParser.class)
    Observable<String> getKbContent(@Path("id") String id);

    /**
     * [同步方式] 获取知识库内容
     *
     * @param id 知识库ID
     */
    @GET(ApiUrls.API_KB_CONTENT)
    @Parser(KBContentParser.class)
    Call<String> syncGetKbContent(@Path("id") String id);

    /**
     * 推荐/喜欢 博客
     *
     * @param id      博客ID
     * @param blogApp 该文的博主ID
     */
    @POST(ApiUrls.API_BLOG_LIKE)
    @FormUrlEncoded
    @Headers({JsonBody.CONTENT_TYPE})
    Observable<Empty> likeBlog(@Field("postId") String id, @Field("blogApp") String blogApp);

    /**
     * 取消推荐博客
     *
     * @param id      博客ID
     * @param blogApp 该文的博主ID
     */
    @POST(ApiUrls.API_BLOG_UNLIKE)
    @FormUrlEncoded
    @Headers({JsonBody.CONTENT_TYPE})
    Observable<Empty> unLikeBlog(@Field("postId") String id, @Field("blogApp") String blogApp);

    /**
     * 知识库点赞
     *
     * @param id 知识库ID
     */
    @POST(ApiUrls.API_KB_LIKE)
    @FormUrlEncoded
    @Headers({JsonBody.CONTENT_TYPE, JsonBody.XHR})
    Observable<Empty> likeKb(@Field("contentId") String id);

    /**
     * 发表博客评论
     * <p>如果要引用评论，则content参数取值为： </p>
     * {@link com.rae.cnblogs.sdk.utils.ApiUtils#getCommentContent(BlogCommentBean, String)} 来获取转换的内容
     *
     * @param id              博客ID
     * @param blogApp         该文的博主ID
     * @param parentCommentId 引用回复评论ID，为空则发表新评论
     * @param content         评论内容
     */
    @POST(ApiUrls.API_BLOG_COMMENT_ADD)
    @FormUrlEncoded
    @Headers({JsonBody.CONTENT_TYPE})
    Observable<Empty> addBlogComment(@Field("postId") String id, @Field("blogApp") String blogApp, @Field("parentCommentId") String parentCommentId, @Field("body") String content);


    /**
     * 删除博客评论
     *
     * @param commentId 评论ID
     */
    @POST(ApiUrls.API_BLOG_COMMENT_DELETE)
    @FormUrlEncoded
    @Headers({JsonBody.CONTENT_TYPE})
    Observable<Empty> deleteBlogComment(@Field("commentId") String commentId);


    /**
     * 博客开通状态，如果已开通返回真
     */
    @POST(ApiUrls.API_BLOG_CHECK_OPEN)
    @Parser(BlogOpenStatusParser.class)
    Observable<Boolean> checkBlogIsOpen();

}
