package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.bean.BlogCommentModel;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import com.alibaba.fastjson.JSON;

/**
 * 博文解析
 * Created by ChenRui on 2016/11/30 0030 17:00.
 */
public class BlogCommentParser implements IJsonParser<List<BlogCommentBean>> {

    private Gson mGson = new Gson();

    @Override
    public List<BlogCommentBean> parse(String json) {

        List<BlogCommentBean> result = Collections.emptyList();
        if (TextUtils.isEmpty(json)) {
            return result;
        }
        BlogCommentModel model = mGson.fromJson(json, BlogCommentModel.class);
        if (model == null) {
            return result;
        }
        String html = model.getCommentsHtml();
        if (TextUtils.isEmpty(html)) {
            return result;
        }

        result = new ArrayList<>();
        // 解析XML
        Document document = Jsoup.parse(html);

        Elements bodys = document.select(".blog_comment_body");
        if (bodys.size() <= 0) return result; // 没有评论
        for (Element body : bodys) {
            BlogCommentBean m = new BlogCommentBean();
            result.add(m);
            String id = ApiUtils.getNumber(body.attr("id"));
            m.setId(id);
            // 作者节点
            Element author = document.select("#a_comment_author_" + id).first();
            if (author != null) {
                m.setAuthorName(author.text());
                m.setBlogApp(ApiUtils.getBlogApp(author.attr("href")));
            }
            m.setAvatar(document.select("#comment_" + id + "_avatar").text());

            // 解析引用的评论
            m.setQuote(body.select(".comment_quote").text().replace("引用", ""));
            if (!TextUtils.isEmpty(m.getQuote()) && body.childNodes().size() > 1) {
                List<Node> nodes = body.childNodes();
                m.setQuoteBlogApp(nodes.get(1).outerHtml());
                nodes.get(0).remove();
                nodes.get(0).remove();
                nodes.get(0).remove();
            }
            // 内容移掉引用
            body.select("fieldset").remove();
            m.setBody(body.text());
        }

        // 找时间
        Elements dateElements = document.select(".comment_date");
        if (dateElements.size() == bodys.size()) {
            for (int i = 0; i < dateElements.size(); i++) {
                Element element = dateElements.get(i);
                BlogCommentBean m = result.get(i);
                m.setDate(element.text());
            }
        }
        return result;
    }
}
