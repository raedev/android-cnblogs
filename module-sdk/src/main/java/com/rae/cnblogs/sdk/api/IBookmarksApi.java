package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.cnblogs.sdk.parser.BookmarksDelParser;
import com.rae.cnblogs.sdk.parser.BookmarksParser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 收藏接口
 * Created by ChenRui on 2017/1/14 13:53.
 */
public interface IBookmarksApi {

    /**
     * 添加到收藏夹
     *
     * @param title   收藏标题
     * @param summary 收藏摘要
     * @param url     路径
     */
    @POST(ApiUrls.API_BOOK_MARKS_ADD)
    @FormUrlEncoded
    @Headers({JsonBody.CONTENT_TYPE})
    Observable<Empty> addBookmarks(@Field("title") String title, @Field("summary") String summary, @Field("url") String url);

    /**
     * 获取收藏列表
     *
     * @param page 页码
     */
    @GET(ApiUrls.API_BOOK_MARKS_LIST)
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Upgrade-Insecure-Requests: 1"
    })
    @Parser(BookmarksParser.class)
    Observable<List<BookmarksBean>> getBookmarks(@Path("page") int page);

    /**
     * 删除收藏
     *
     * @param linkId 收藏ID
     */
    @DELETE(ApiUrls.API_BOOK_MARKS_DELETE)
    @Headers({JsonBody.CONTENT_TYPE})
    @Parser(BookmarksDelParser.class)
    Observable<Empty> delBookmarks(@Path("id") String linkId);
}
