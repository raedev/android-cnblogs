package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.BlogType;

/**
 * 搜索知识库列表解析
 * Created by ChenRui on 2017/2/8 0008 10:05.
 */
public class SearchKbListParser extends SearchBlogListParser {

    public SearchKbListParser() {
        super(BlogType.KB);
    }
}
