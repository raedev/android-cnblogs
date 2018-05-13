package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.Empty;

import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * 删除闪存
 * Created by ChenRui on 2017/11/3 0003 0:45.
 */
public class MomentDelParser implements IHtmlParser<Empty> {

    @Override
    public Empty parse(Document document, String html) throws IOException {
        if (html.contains("成功")) {
            return Empty.value();
        }

        throw new CnblogsApiException(html);
    }
}
