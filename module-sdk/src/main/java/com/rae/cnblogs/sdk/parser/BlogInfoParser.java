package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;

/**
 * 博客列表解析器
 * Created by ChenRui on 2016/11/30 00:13.
 */
public class BlogInfoParser implements IHtmlParser<BlogBean> {

    DbBlog mDbBlog;

    public BlogInfoParser() {
        mDbBlog = DbFactory.getInstance().getBlog();
    }

    @Override
    public BlogBean parse(Document document, String html) {

        // 找blogApp
        BlogBean blog = new BlogBean();
        // 首页的链接就是blogApp
        String blogApp = document.select(".headermaintitle").attr("href");
        // 原文链接，提取博客Id
        String linkUrl = document.select("#cb_post_title_url").attr("href");
        // 作者
        String author = document.select(".headermaintitle").text();
        String authorUrl = document.select(".headermaintitle").attr("href");
        String summary = ApiUtils.subString(document.select(".postBody").text(), 360);
        String title = document.select("#cb_post_title_url").text();
        String date = document.select("#post-date").text();
        String viewCount = document.select("#post_view_count").text();
        String commentCount = document.select("#post_comment_count").text();
        blog.setBlogApp(ApiUtils.getBlogApp(blogApp));
        blog.setBlogId(ApiUtils.getNumber(linkUrl));
        blog.setBlogType(BlogType.BLOG.getTypeName());
        blog.setAuthor(author);
        blog.setAuthorUrl(authorUrl);
        blog.setUrl(linkUrl);
        blog.setTitle(title);
        blog.setSummary(summary);
        blog.setPostDate(date);
        blog.setViews(TextUtils.equals("...", viewCount) ? null : viewCount);
        blog.setComment(TextUtils.equals("...", commentCount) ? null : commentCount);

        // 不是博客
        if (TextUtils.isEmpty(blog.getBlogApp()) || TextUtils.isEmpty(blog.getBlogId())) {
            return null;
        }

        mDbBlog.add(blog);


        return blog;
    }
}
