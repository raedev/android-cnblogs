package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻评论
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsCommentParser implements IHtmlParser<List<BlogCommentBean>> {

    @Override
    public List<BlogCommentBean> parse(Document document, String json) {
        // 解析HTML
        List<BlogCommentBean> result = new ArrayList<>();
        Elements elements = document.select(".user_comment");
        for (Element element : elements) {


            BlogCommentBean m = new BlogCommentBean();
            m.setId(ApiUtils.getNumber(element.select(".comment_main").attr("id")));
            m.setAuthorName(element.select(".comment-author").text());
            m.setLike(ApiUtils.getCount(element.select(".agree_" + m.getId()).text()));
            m.setQuote(element.select(".comment_quote").text().replace("引用", ""));


            Elements bodyElement = element.select(".comment_main");
            if (!TextUtils.isEmpty(m.getQuote()) && bodyElement.size() > 0 && bodyElement.get(0).childNodes().size() > 1) {
                List<Node> nodes = bodyElement.get(0).childNodes();
                m.setQuoteBlogApp(nodes.get(1).outerHtml());
                nodes.get(0).remove();
                nodes.get(0).remove();
                nodes.get(0).remove();
            }
            // 内容移掉引用
            bodyElement.select("fieldset").remove();
            m.setBody(bodyElement.text());
            m.setDate(ApiUtils.getDate(element.select(".time").text().replace("发表于 ", "").trim()));
            result.add(m);
        }
        return result;
    }
}
