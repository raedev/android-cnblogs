package com.rae.cnblogs.sdk.parser;

import android.text.Html;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 新闻内容解析器
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsContentParser implements IHtmlParser<String> {

    @Override
    public String parse(Document document, String html) {
        Elements elements = document.select("Content");
        String text = Html.fromHtml(elements.html()).toString().replace("src=\"//", "src=\"http://");
        return text;
    }
}
