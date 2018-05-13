package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.Empty;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * 发布新闻评论
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsAddCommentParser implements IHtmlParser<Empty> {

    @Override
    public Empty parse(Document document, String json) throws IOException {
        if (!TextUtils.isEmpty(json) && json.startsWith("<table")) {
            return Empty.value();
        }
        if (json.startsWith("<span")) {
            throw new CnblogsApiException(Jsoup.parse(json).text());
        }
        return null;
    }
}
