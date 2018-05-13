package com.rae.cnblogs.sdk.parser;

import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 博文解析
 * Created by ChenRui on 2016/11/30 0030 17:00.
 */
public class BlogContentParser implements IHtmlParser<String> {

    @Override
    public String parse(org.jsoup.nodes.Document document, String html) {
        // 解析XML
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            return doc.getDocumentElement().getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
