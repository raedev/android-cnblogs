package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.MomentCommentBean;

import org.jsoup.nodes.Document;

import java.util.List;

/**
 * 闪存解析器
 * Created by ChenRui on 2017/9/25 0025 17:16.
 */
public class MomentCommentParser implements IHtmlParser<List<MomentCommentBean>> {

    private final MomentCommentHelper mMomentCommentHelper = new MomentCommentHelper();

    @Override
    public List<MomentCommentBean> parse(Document doc, String html) {
        return mMomentCommentHelper.parseCommentInList(doc);
    }
}
