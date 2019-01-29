package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.DefaultJsonParser;
import com.rae.cnblogs.sdk.JsonParser;
import com.rae.cnblogs.sdk.bean.HotSearchBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface IRankingApi {


    @GET(ApiUrls.RAE_CNBLOGS_RANKING_SEARCH)
    @JsonParser
    Observable<List<HotSearchBean>> hotSearch();

    @GET(ApiUrls.RAE_CNBLOGS_RANKING_READ)
    @JsonParser
    Observable<List<HotSearchBean>> topRead(@Query("page") int page);

    @GET(ApiUrls.RAE_CNBLOGS_RANKING_FAVORITE)
    @JsonParser
    Observable<List<HotSearchBean>> topFavorite(@Query("page") int page);


    @GET(ApiUrls.RAE_CNBLOGS_RANKING_AUTHOR)
    @JsonParser
    Observable<List<HotSearchBean>> topAuthor(@Query("page") int page);


}
