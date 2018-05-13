package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.sdk.parser.MomentCommentParser;
import com.rae.cnblogs.sdk.parser.MomentDelParser;
import com.rae.cnblogs.sdk.parser.MomentDetailParser;
import com.rae.cnblogs.sdk.parser.MomentParser;
import com.rae.cnblogs.sdk.parser.MomentReplyParser;

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
 * 闪存接口
 * Created by ChenRui on 2017/9/25 0025 17:01.
 */
public interface IMomentApi {

    /**
     * 全站闪存
     */
    String MOMENT_TYPE_ALL = "All";

    /**
     * 关注的闪存
     */
    String MOMENT_TYPE_FOLLOWING = "following";

    /**
     * 回复我的
     */
    String MOMENT_TYPE_REPLY_ME = "comment";

    /**
     * 提到我的
     */
    String MOMENT_TYPE_AT_ME = "mention";

    /**
     * 我自己的闪存
     */
    String MOMENT_TYPE_MY = "My";

    /**
     * 发布闪存
     * [官方不支持图文模式]
     *
     * @param content    发表内容
     * @param publicFlag 取值：1为公开，0为私有
     * @return
     */

    @POST(ApiUrls.API_MOMENT_PUBLISH)
    @FormUrlEncoded
    @Headers({JsonBody.XHR})
    Observable<Empty> publish(@Field("content") String content, @Field("publicFlag") int publicFlag);

    /**
     * 删除闪存
     */
    @POST(ApiUrls.API_MOMENT_DEL)
    @FormUrlEncoded
    @Headers({JsonBody.XHR})
    @Parser(MomentDelParser.class)
    Observable<Empty> deleteMoment(@Field("ingId") String ingId);

    /**
     * 获取回复我的数量
     */
    @POST(ApiUrls.API_MOMENT_REPLY_COUNT)
    @FormUrlEncoded
    @Headers({JsonBody.XHR})
    Observable<String> queryReplyCount(@Field("_") long timestamp);

    /**
     * 提到我的数量
     */
    @POST(ApiUrls.API_MOMENT_AT_COUNT)
    @FormUrlEncoded
    @Headers({JsonBody.XHR})
    Observable<String> queryAtMeCount(@Field("_") long timestamp);

    /**
     * 发表闪存评论
     *
     * @param ingId     闪存ID
     * @param userId    回复的用户ID
     * @param commentId 回复的评论ID
     * @param content   回复内容
     * @return
     */
    @POST(ApiUrls.API_MOMENT_POST_COMMENT)
    @FormUrlEncoded
    @Headers({JsonBody.CONTENT_TYPE, JsonBody.XHR})
    Observable<Empty> postComment(@Field("ingId") String ingId, @Field("ReplyToUserId") String userId, @Field("ParentCommentId") String commentId, @Field("Content") String content);

    /**
     * 获取闪存列表
     *
     * @param type      类型，参考静态变量MOMENT_TYPE_*，如：{@link #MOMENT_TYPE_ALL}
     * @param page      页码
     * @param timestamp 传当前的时间戳
     */
    @GET(ApiUrls.API_MOMENT_LIST)
    @Headers({JsonBody.XHR})
    @Parser(MomentParser.class)
    Observable<List<MomentBean>> getMoments(@Query("IngListType") String type, @Query("PageIndex") int page, @Query("_") long timestamp);

    /**
     * 获取回复我的闪存
     *
     * @param type      默认：{@link #MOMENT_TYPE_REPLY_ME}
     * @param page      页码
     * @param timestamp 传当前的时间戳
     */
    @GET(ApiUrls.API_MOMENT_LIST)
    @Headers({JsonBody.XHR})
    @Parser(MomentReplyParser.class)
    Observable<List<MomentCommentBean>> getReplyMeMoments(@Query("IngListType") String type, @Query("PageIndex") int page, @Query("_") long timestamp);

    /**
     * 获取闪存评论
     *
     * @param ingId     闪存ID
     * @param userAlias 用户ID
     * @param timestamp 传当前的时间戳
     * @return
     */
    @GET(ApiUrls.API_MOMENT_SINGLE_COMMENTS)
    @Headers({JsonBody.XHR})
    @Parser(MomentCommentParser.class)
    Observable<List<MomentCommentBean>> getMomentSingleComments(@Query("ingId") String ingId, @Query("userAlias") String userAlias, @Query("_") long timestamp);


    /**
     * 删除闪存评论
     */
    @POST(ApiUrls.API_MOMENT_COMMENT_DEL)
    @FormUrlEncoded
    @Headers({JsonBody.XHR})
    Observable<Empty> deleteMomentComment(@Field("commentId") String commentId);

    /**
     * 更新回复我的为已阅读
     *
     * @param commentId 最新的评论ID
     * @param timestamp 时间戳
     * @return
     */
    @POST(ApiUrls.API_MOMENT_COMMENT_UPDATE_READ)
    @FormUrlEncoded
    @Headers({JsonBody.XHR})
    Observable<Empty> updateRelyMeToRead(@Field("commentId") String commentId, @Query("_") long timestamp);

    /**
     * 更新提到我的为已阅读
     *
     * @param timestamp 时间戳
     * @return
     */
    @POST(ApiUrls.API_MOMENT_AT_UPDATE_READ)
    @Headers({JsonBody.XHR})
    Observable<Empty> updateAtMeToRead(@Query("_") long timestamp);

    /**
     * 闪存详情
     *
     * @param userAlias 用户
     * @param ingId     闪存ID
     * @param timestamp 时间戳
     * @return
     */
    @GET(ApiUrls.API_MOMENT_DETAIL)
    @Parser(MomentDetailParser.class)
    Observable<MomentBean> getMomentDetail(@Path("user") String userAlias, @Path("ingId") String ingId, @Query("_") long timestamp);
}
