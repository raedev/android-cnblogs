package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.parser.BaiduSuggestionParser;
import com.rae.cnblogs.sdk.parser.SearchBlogListParser;
import com.rae.cnblogs.sdk.parser.SearchBloggerParser;
import com.rae.cnblogs.sdk.parser.SearchKbListParser;
import com.rae.cnblogs.sdk.parser.SearchNewsListParser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 搜索接口
 * Created by ChenRui on 2017/2/8 0008 9:22.
 */
public interface ISearchApi {

    /**
     * 搜索建议
     *
     * @param keyWord 关键字
     */
    @GET(ApiUrls.API_BAIDU_SUGGESTION)
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Cookie: AspxAutoDetectCookieSupport=1"
    })
    @Parser(BaiduSuggestionParser.class)
    Observable<List<String>> getSuggestion(@Query("wd") String keyWord);

    /**
     * 搜索博主
     *
     * @param keyword 关键字
     */
    @GET(ApiUrls.API_SEARCH_BLOGGER)
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Cookie: AspxAutoDetectCookieSupport=1"
    })
    @Parser(SearchBloggerParser.class)
    Observable<List<UserInfoBean>> searchBlogAuthor(@Query("t") String keyword);

    /**
     * 搜索博客
     *
     * @param keyword 关键字
     */
    @GET(ApiUrls.API_SEARCH_BLOG_LIST)
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Cookie: AspxAutoDetectCookieSupport=1"
    })
    @Parser(SearchBlogListParser.class)
    Observable<List<BlogBean>> searchBlogList(@Query("Keywords") String keyword, @Query("pageindex") int page);

    /**
     * 搜索个人博客
     *
     * @param keyword 关键字
     */
    @GET(ApiUrls.API_SEARCH_BLOG_APP)
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Cookie: AspxAutoDetectCookieSupport=1"
    })
    @Parser(SearchBlogListParser.class)
    Observable<List<BlogBean>> searchBlogAppList(@Query("Keywords") String keyword, @Query("pageindex") int page);

    /**
     * 搜索知识库
     */
    @GET(ApiUrls.API_SEARCH_KB_LIST)
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Cookie: AspxAutoDetectCookieSupport=1"
    })
    @Parser(SearchKbListParser.class)
    Observable<List<BlogBean>> searchKbList(@Query("Keywords") String keyword, @Query("pageindex") int page);

    /**
     * 搜索新闻
     */
    @GET(ApiUrls.API_SEARCH_NEWS_LIST)
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Cookie: AspxAutoDetectCookieSupport=1"
    })
    @Parser(SearchNewsListParser.class)
    Observable<List<BlogBean>> searchNewsList(@Query("Keywords") String keyword, @Query("pageindex") int page);


    /**
     * 热门搜索
     */
    @GET(ApiUrls.API_SEARCH_HOT)
    Observable<List<String>> hotSearch();

    /**
     * 添加到热门搜索
     *
     * @param keyword 搜索关键字
     */
    @POST(ApiUrls.API_SEARCH_HOT)
    Observable<List<String>> search(@Field("keyword") String keyword);
}
