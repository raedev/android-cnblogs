package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 闪存解析器
 * Created by ChenRui on 2017/9/25 0025 17:16.
 */
public class MomentDetailParser implements IHtmlParser<MomentBean> {

    private final MomentCommentHelper mMomentCommentHelper = new MomentCommentHelper();

    @Override
    public MomentBean parse(Document element, String html) {
        // https://ing.cnblogs.com/u/393130/status/1244962/

        MomentBean m = new MomentBean();
        String id = ApiUtils.getNumber(element.select(".comment_list_block ul").attr("id"));
        m.setId(id); // 主键
        m.setAuthorName(element.select(".ing_item_author").text()); // 作者名称
        m.setBlogApp(ApiUtils.getBlogApp(element.select(".ing_item_author").attr("href"))); // blogApp
        m.setAvatar(ApiUtils.getUrl(element.select(".ing_item_face").attr("src"))); //头像地址
        m.setContent(element.select("#ing_detail_body").html()); // 内容
        m.setPostTime(element.select(".ing_detail_title").text().replace("：", "").replace(m.getAuthorName(), "")); // 发布时间
        m.setCommentCount(ApiUtils.getCount(element.select(".ing_comment_count").text().replace("回应", ""))); // 评论数量

        m.setSourceUrl(ApiUtils.getUrl(element.select(".ing_item_author").attr("href")).replace("home", "ing") + "status/" + id); // blogApp

        // 找用户ID
        Elements scripts = element.select("script");
        for (Element script : scripts) {
            String text = script.html();
            // 匹配脚本出现的第一组数字
            if (text.contains("replyToSpaceUserId")) {
                Matcher matcher = Pattern.compile("\\d+").matcher(text);
                if (matcher.find()) {
                    m.setUserAlias(matcher.group());
                }
                break;
            }
        }

        // 解析评论
        mMomentCommentHelper.parseCommentInDetail(element, m);

        // 解析图片
        mMomentCommentHelper.parseImageList(m, element.select(".ing_body a"));

        return m;
    }
}
