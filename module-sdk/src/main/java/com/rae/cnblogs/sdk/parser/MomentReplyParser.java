package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 回复我的闪存解析器
 * Created by ChenRui on 2017/9/25 0025 17:16.
 */
public class MomentReplyParser implements IHtmlParser<List<MomentCommentBean>> {

    @Override
    public List<MomentCommentBean> parse(Document doc, String html) {
        List<MomentCommentBean> result = new ArrayList<>();
        Elements elements = doc.select("#feed_list li");

        for (Element element : elements) {
            MomentCommentBean m = new MomentCommentBean();
            // 评论ID
            String id = ApiUtils.getNumber(element.select(".feed_body").attr("id"));
            if (TextUtils.isEmpty(id)) continue; // 跳过ID为空的

            m.setId(id); // 主键
            m.setAuthorName(element.select("#comment_author_" + id).text()); // 作者名称
            m.setBlogApp(ApiUtils.getBlogApp(element.select("#comment_author_" + id).attr("href"))); // blogApp
            m.setAvatar(ApiUtils.getUrl(element.select(".feed_avatar a img").attr("src"))); //头像地址
            m.setContent(getContentText(element)); // 内容
            m.setReferenceContent(getReferenceContent(element, m.getAuthorName()));
            m.setPostTime(element.select(".ing_time").text()); // 发布时间

            // 解析ingId
            Matcher matcher = Pattern.compile("\\d+").matcher(element.select(".ing_reply").attr("onclick"));
            int index = 0;
            while (matcher.find()) {
                String text = matcher.group();
                if (index == 0) {
                    m.setIngId(text); // 闪存ID
                }
                if (index == 2) {
                    m.setUserAlias(text);
                }
                index++;
            }

            result.add(m);
        }


        return result;
    }

    private String getContentText(Element element) {
        Elements elements = element.select(".ing_body");
        if (elements.size() > 1) return elements.text();

        Element e = elements.get(0);
        Elements links = e.select("a");

        // 去掉第一个@自己的文本
        if (links.size() > 0 && links.get(0).text().contains("@")) {
            links.get(0).remove();
            return e.text().substring(1); // 去掉冒号
        }

        return e.text();

    }

    public String getReferenceContent(Element element, String authorName) {
        String text = element.select(".comment-body-topline").text();
        String content = element.select(".comment-body-gray").text();
        int startIndex = text.indexOf(content);

        if (startIndex >= 0) {
            String type = text.substring(0, startIndex).replace("对", "").replace("“", "").replace(authorName, "").trim();
            return String.format("回复%s：%s", type, content.replace("@" + authorName + "：", ""));
        }

        return text;
    }
}
