package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻列表解析器
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsListParser implements IHtmlParser<List<BlogBean>> {

    private DbBlog mDbBlog;

    public NewsListParser() {
        mDbBlog = DbFactory.getInstance().getBlog();
    }

    @Override
    public List<BlogBean> parse(Document document, String html) {
        // 解析HTML
        List<BlogBean> result = new ArrayList<>();
        Elements elements = document.select("entry");
        for (Element element : elements) {
            BlogBean m = new BlogBean();
            m.setBlogId(element.select("id").text());
            m.setTitle(element.select("title").text());
            m.setSummary(element.select("summary").text());
            m.setPostDate(ApiUtils.getDate(element.select("updated").text().replace("T", " ").replace("Z", " ")));
            m.setUrl(element.select("link").attr("href"));
            m.setLikes(element.select("diggs").text());
            m.setViews(element.select("views").text());
            m.setComment(element.select("comments").text());
            m.setAuthor(element.select("sourceName").text());
            m.setAvatar(element.select("topicIcon").text().replace("images0.cnblogs.com/news_topic///", ""));
            m.setBlogType(BlogType.NEWS.getTypeName());

            UserBlogInfo blogInfo = mDbBlog.get(m.getBlogId());
            if (blogInfo != null) {
                m.setReaded(blogInfo.isRead());
            }

            result.add(m);
        }

        return result;
    }
}
