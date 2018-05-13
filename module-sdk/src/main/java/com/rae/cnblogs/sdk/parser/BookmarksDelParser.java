package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.Empty;

import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * 删除收藏
 * Created by ChenRui on 2017/6/11 0011 1:28.
 */
public class BookmarksDelParser implements IHtmlParser<Empty> {

    @Override
    public Empty parse(Document document, String html) throws IOException {
        if (TextUtils.equals(html, "1")) {
            // 成功
            return Empty.value();
        }
        throw new CnblogsApiException(html);
    }
}
