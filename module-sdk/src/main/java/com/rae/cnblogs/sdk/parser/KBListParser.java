package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 知识库列表解析器
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class KBListParser implements IHtmlParser<List<BlogBean>> {

    private DbBlog mDbBlog;
    private Gson mGson = new Gson();

    public KBListParser() {
        mDbBlog = DbFactory.getInstance().getBlog();
    }

    /**
     * 缓存小图
     *
     * @param m
     */
    protected void cacheThumbUrls(BlogBean m) {
        // 小图处理：从数据库中获取
        BlogBean dbBlog = mDbBlog.getBlog(m.getBlogId()); // 获取小图
        UserBlogInfo blogInfo = mDbBlog.get(m.getBlogId());

        if (blogInfo != null) {
            m.setReaded(blogInfo.isRead());
        }

        if (dbBlog != null && !TextUtils.isEmpty(dbBlog.getThumbUrls())) {
            m.setThumbUrls(dbBlog.getThumbUrls()); // 存在有小图
        } else {
            if (dbBlog != null && blogInfo != null && !TextUtils.isEmpty(blogInfo.getContent())) {
                m.setThumbUrls(createThumbUrls(blogInfo.getContent()));
                dbBlog.setThumbUrls(m.getThumbUrls());
                // 更新小图
                mDbBlog.updateBlog(dbBlog);
            }
        }
    }

    /**
     * 获取小图
     *
     * @param content 博文
     */
    private String createThumbUrls(String content) {
        try {
            List<String> result = new ArrayList<>();
            Elements elements = Jsoup.parse(content).select("img");
            for (Element element : elements) {
                String src = element.attr("src");

                // 过滤一些没有用的图片
                if (TextUtils.isEmpty(src) || src.contains(".gif")) {
                    continue;
                }

                result.add(ApiUtils.getUrl(src));
            }

            return mGson.toJson(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取点赞数量
     *
     * @param text 浏览(8312) 推荐(94) 程序员 学习 发布于2017-01-09 14:10
     */
    private String getLikeCount(String text) {
        if (TextUtils.isEmpty(text)) return "0";
        // 找到推荐
        Matcher matcher = Pattern.compile("推荐\\(\\d+\\)").matcher(text);
        if (matcher.find()) {
            return ApiUtils.getNumber(matcher.group());
        }
        return "0";
    }

    @Override
    public List<BlogBean> parse(Document document, String html) {
        // 解析HTML
        List<BlogBean> result = new ArrayList<>();
        Elements elements = document.select(".kb_item");
        for (Element element : elements) {
            BlogBean m = new BlogBean();
            m.setBlogId(ApiUtils.getNumber(element.attr("id")));
            m.setTitle(element.select(".kb_entry .kb-title").text());
            m.setTag(element.select(".kb_entry .deepred").text());
            m.setSummary(element.select(".kb_summary").text() + "...");
            m.setPostDate(ApiUtils.getDate(element.select(".kb_footer .green").text()));
            m.setUrl("http:" + element.select(".kb_entry .kb-title").attr("href"));
            m.setViews(ApiUtils.getNumber(element.select(".kb_footer .view").text()));
            m.setLikes(getLikeCount(element.select(".kb_footer").text()));
            m.setBlogType(BlogType.KB.getTypeName());


            UserBlogInfo blogInfo = mDbBlog.get(m.getBlogId());
            if (blogInfo != null) {
                m.setReaded(blogInfo.isRead());
            }
            cacheThumbUrls(m);
            result.add(m);
        }
        return result;
    }
}
