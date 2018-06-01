package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.TagBean;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏标签
 * Created by ChenRui on 2017/6/11 0011 1:28.
 */
public class BookmarksTagParser implements IHtmlParser<List<TagBean>> {

    @Override
    public List<TagBean> parse(Document document, String html) throws IOException {
        List<TagBean> result = new ArrayList<>();
        Elements elements = document.select("li[data-tagname]");
        for (Element element : elements) {
            result.add(new TagBean(element.attr("data-tagname")));
        }
        return result;
    }
}
