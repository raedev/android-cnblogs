package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.JsonParser;
import com.rae.cnblogs.sdk.bean.BlogQuestionBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
/**
 * 博问接口
 */
public interface IBlogQuestionApi {

    /**
     * 未解决的博问
     */
    @GET(ApiUrls.RAE_CNBLOGS_QUESTION_UNSOLVED)
    @JsonParser
    Observable<List<BlogQuestionBean>> getUnsolvedQuestions(@Query("page") int page);

    /**
     * 高分的博问
     */
    @GET(ApiUrls.RAE_CNBLOGS_QUESTION_HIGH_SCORE)
    @JsonParser
    Observable<List<BlogQuestionBean>> getHighScoreQuestions(@Query("page") int page);

    /**
     * 我的博问
     */
    @GET(ApiUrls.RAE_CNBLOGS_QUESTION_MY)
    @JsonParser
    Observable<List<BlogQuestionBean>> getMyQuestions(@Query("page") int page);
}
