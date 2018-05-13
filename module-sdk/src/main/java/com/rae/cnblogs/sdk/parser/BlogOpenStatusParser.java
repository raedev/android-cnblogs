package com.rae.cnblogs.sdk.parser;

import org.jsoup.nodes.Document;

/**
 * 博客开通状态
 * Created by ChenRui on 2017/11/8 0008 16:57.
 */
public class BlogOpenStatusParser implements IHtmlParser<Boolean> {

    @Override
    public Boolean parse(Document document, String html) {
        return document.select(".topic_title:contains(开通博客)").size() <= 0;
    }
}
