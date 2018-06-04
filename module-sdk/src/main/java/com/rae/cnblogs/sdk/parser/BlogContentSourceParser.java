package com.rae.cnblogs.sdk.parser;

import org.jsoup.nodes.Document;

/**
 * 博客列表解析器
 * Created by ChenRui on 2016/11/30 00:13.
 */
public class BlogContentSourceParser implements IHtmlParser<String> {

    @Override
    public String parse(Document document, String html) {
        return document.select(".postBody").html();
    }
}
