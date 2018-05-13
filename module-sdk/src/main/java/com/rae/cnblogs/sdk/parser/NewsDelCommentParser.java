package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.Empty;

import org.jsoup.nodes.Document;

/**
 * 删除新闻评论
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsDelCommentParser implements IHtmlParser<Empty> {

    @Override
    public Empty parse(Document document, String json) {
        if (!TextUtils.isEmpty(json) && json.startsWith("0")) {
            return null;
        }
        return Empty.value();
    }
}
