package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户动态
 * Created by ChenRui on 2017/2/7 0007 14:00.
 */
public class UserTimelineParser implements IHtmlParser<List<UserFeedBean>> {

    @Override
    public List<UserFeedBean> parse(Document document, String html) {
        List<UserFeedBean> result = new ArrayList<>();
        Elements elements = document.select(".feed_item");

        for (Element element : elements) {
            UserFeedBean m = new UserFeedBean();
            m.setAvatar(ApiUtils.getUrl(element.select(".feed_avatar a img").attr("src")));
            m.setAuthor(element.select(".feed_author").text());
            m.setBlogApp(element.select(".feed_author").attr("href").replace("/u/", "").replace("/", ""));
            m.setAction(getAction(element.select(".feed_title").html()));
            m.setTitle(element.select(".feed_title a").eq(1).text());
            m.setUrl(element.select(".feed_title a").eq(1).attr("href"));
            m.setFeedDate(ApiUtils.getDate(element.select(".feed_date").text()));
            m.setContent(element.select(".feed_desc").text());
            // 内容为空
            if (TextUtils.isEmpty(m.getContent())) {
                // 只读取标题内容
                Elements titleElement = element.select(".feed_title").clone();
                titleElement.select("a").remove();
                m.setContent(titleElement.text().replace("：", ""));
            }
            result.add(m);
        }
        return result;
    }


    private String getAction(String text) {
        Pattern regx = Pattern.compile(">\\s.+\\n");
        Matcher matcher = regx.matcher(text);
        if (matcher.find()) {
            return matcher.group().replace(">", "").replace("：", "").trim();
        }
        if (text.contains("ing_reply")) {
            return "发表评论";
        }
        return "未知类型";
    }

}
