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

        Elements posts = document.select(".post");
        Elements feeds = document.select(".feedbackItem");
        Elements lis = document.select(".commentlist li");
        if (feeds.size() <= 0 && posts.size() > 0) {
            feeds = posts;
        }
        if (lis.size() > 0 && feeds.size() <= 0) {
            feeds = lis;
        }

        for (Element feed : feeds) {
            BlogCommentBean m = new BlogCommentBean();
            String id = ApiUtils.getNumber(feed.select(".layer").attr("href"));
            String authorName = feed.select("#a_comment_author_" + id).text();
            String blogApp = ApiUtils.getBlogApp(feed.select("#a_comment_author_" + id).attr("href"));
            String date = ApiUtils.getDate(feed.select(".comment_date").text());

            m.setQuote(feed.select(".comment_quote").text().replace("引用", ""));


            Elements bodyElement = feed.select(".blog_comment_body");
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

//            String body = feed.select(".blog_comment_body").text();
            String like = ApiUtils.getNumber(feed.select(".comment_digg").text());
            String unlike = ApiUtils.getNumber(feed.select(".comment_bury").text());
            String avatar = feed.select(".comment_" + id + "_avatar").text();


            m.setId(id);
            m.setAuthorName(authorName);
            m.setAvatar(avatar);
            m.setBlogApp(blogApp);
            m.setDate(date);
//            m.setBody(body);
            m.setLike(like);
            m.setUnlike(unlike);

            result.add(m);
        }
        return result;
    }
}
