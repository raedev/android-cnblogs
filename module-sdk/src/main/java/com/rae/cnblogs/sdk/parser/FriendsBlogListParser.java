package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客列表解析器
 * Created by ChenRui on 2016/11/30 00:13.
 */
public class FriendsBlogListParser extends BlogListParser {

    @Override
    public List<BlogBean> parse(Document document, String html) {

        // 解析HTML
        List<BlogBean> result = new ArrayList<>();
        Elements elements = document.select("entry");
        for (Element element : elements) {

            String id = ApiUtils.getNumber(element.select("id").text());
            String title = element.select("title").text(); // 标题
            String url = element.select("link").attr("href"); // 原文链接
            String avatar = document.select("logo").text().replace("file://", "http://"); // 头像地址
            String summary = element.select("summary").text(); // 摘要
            String author = element.select("author name").text(); // 作者
            String authorUrl = element.select("author uri").text(); // 作者博客地址
            String blogApp = ApiUtils.getBlogApp(authorUrl);
            String comment = element.select("comments").text(); // 评论
            String views = element.select("views").text(); // 阅读
            String likes = element.select("diggs").text(); // 点赞或者是推荐
            String date = ApiUtils.getDate(element.select(".updated").text()); // 发布时间

            // 博客ID为空不添加
            if (TextUtils.isEmpty(id)) {
                continue;
            }

            BlogBean m = new BlogBean();
            m.setBlogId(id);
            m.setTitle(title);
            m.setUrl(url);
            m.setAvatar(avatar);
            m.setSummary(summary);
            m.setAuthor(author);
            m.setAuthorUrl(authorUrl);
            m.setBlogApp(blogApp);
            m.setComment(comment);
            m.setViews(views);
            m.setPostDate(date);
            m.setLikes(likes);
            m.setBlogType(BlogType.BLOG.getTypeName());

            cacheThumbUrls(m);
            result.add(m);
        }

        return result;
    }

}
