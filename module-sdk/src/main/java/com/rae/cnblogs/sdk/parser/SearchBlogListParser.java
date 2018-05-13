package com.rae.cnblogs.sdk.parser;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 搜索博客列表解析
 * Created by ChenRui on 2017/2/8 0008 10:05.
 */
public class SearchBlogListParser extends BlogListParser {

    protected BlogType mBlogType;

    public SearchBlogListParser() {
        this(BlogType.BLOG);
    }

    public SearchBlogListParser(BlogType type) {
        mBlogType = type;
    }

    @Override
    public List<BlogBean> parse(Document document, String html) {
        // 解析HTML
        List<BlogBean> result = new ArrayList<>();
        Elements elements = document.select(".searchItem");


        // 可能是个人搜索
        if (elements.size() <= 0) {
            return parsePersonal(document, result);
        }


        for (Element element : elements) {

            String id = getId(element.select(".searchURL").text());

            String title = element.select(".searchItemTitle a").html(); // 标题
            if (TextUtils.isEmpty(title)) {
                title = element.select(".searchItemTitle").html(); // 标题
            }
            String url = element.select(".searchURL").text(); // 原文链接
//            String avatar = getAvatar(element.select(".pfs").attr("src")); // 头像地址
            String summary = element.select(".searchCon").html(); // 摘要
            String author = element.select(".searchItemInfo-userName").text(); // 作者
            String authorUrl = element.select(".searchItemInfo-userName a").attr("href"); // 作者博客地址
            String blogApp = ApiUtils.getBlogApp(authorUrl);
            String comment = ApiUtils.getCount(ApiUtils.getNumber(element.select(".searchItemInfo-comments").text())); // 评论
            String views = ApiUtils.getCount(ApiUtils.getNumber(element.select(".searchItemInfo-views").text())); // 阅读
            String likes = ApiUtils.getCount(ApiUtils.getNumber(element.select(".searchItemInfo-good").text())); // 点赞或者是推荐
            String date = ApiUtils.getDate(element.select(".searchItemInfo-publishDate").text()); // 发布时间

            // 博客ID为空不添加
            if (TextUtils.isEmpty(id)) {
                continue;
            }

            BlogBean m = new BlogBean();
            m.setBlogId(id);
            m.setTitle(title);
            m.setUrl(url);
//            m.setAvatar(avatar);
            m.setSummary(summary);
            m.setAuthor(author);
            m.setAuthorUrl(authorUrl);
            m.setBlogApp(blogApp);
            m.setComment(comment);
            m.setViews(views);
            m.setPostDate(date);
            m.setLikes(likes);
            m.setBlogType(mBlogType.getTypeName());

            cacheThumbUrls(m);
            result.add(m);
        }

        return result;
    }

    private String getId(String text) {
        if (TextUtils.isEmpty(text)) return null;

        switch (mBlogType) {
            case BLOG:
                Matcher matcher = Pattern.compile("/\\d+\\.html").matcher(text);
                if (matcher.find()) {
                    return matcher.group().replace(".html", "").replace("/", "");
                }
                break;
            default:
                return ApiUtils.getNumber(text);
        }

        return null;
    }


    /**
     * 个人搜索
     */
    private List<BlogBean> parsePersonal(Document document, List<BlogBean> result) {

        @Nullable UserInfoBean user = UserProvider.getInstance().getLoginUserInfo();

        Elements elements = document.select(".result-item");
        for (Element element : elements) {
            String url = element.select(".result-url").text();
            String id = getId(url);
            String title = element.select(".result-title a").html(); // 标题
            String summary = element.select(".result-content").html(); // 摘要
//            String author = element.select(".searchItemInfo-userName").text(); // 作者
//            String authorUrl = element.select(".searchItemInfo-userName a").attr("href"); // 作者博客地址
//            String blogApp = ApiUtils.getBlogApp(authorUrl);
            String comment = ApiUtils.getCount(ApiUtils.getNumber(element.select(".icon-pinglun").text())); // 评论
            String views = ApiUtils.getCount(ApiUtils.getNumber(element.select(".icon-liulan").text())); // 阅读
            String likes = ApiUtils.getCount(ApiUtils.getNumber(element.select(".icon-dianzan").text())); // 点赞或者是推荐
            String date = ApiUtils.getDate(element.select(".icon-shijiane").text()); // 发布时间

            // 博客ID为空不添加
            if (TextUtils.isEmpty(id)) {
                continue;
            }

            BlogBean m = new BlogBean();
            if (user != null) {
                m.setAuthor(user.getDisplayName());
                m.setAvatar(user.getAvatar());
                m.setBlogApp(user.getBlogApp());
            }
            m.setBlogId(id);
            m.setTitle(title);
            m.setUrl(url);
            m.setSummary(summary);
            m.setComment(comment);
            m.setViews(views);
            m.setPostDate(date);
            m.setLikes(likes);
            m.setBlogType(mBlogType.getTypeName());

            cacheThumbUrls(m);
            result.add(m);
        }

        return result;
    }
}
