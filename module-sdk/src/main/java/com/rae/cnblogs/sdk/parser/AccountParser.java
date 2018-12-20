package com.rae.cnblogs.sdk.parser;

import org.jsoup.nodes.Document;

import java.io.IOException;

public class AccountParser implements IHtmlParser<String> {

    @Override
    public String parse(Document document, String html) throws IOException {
        return document.select("#loginName_display_block .account_right_info").text();
    }
}
