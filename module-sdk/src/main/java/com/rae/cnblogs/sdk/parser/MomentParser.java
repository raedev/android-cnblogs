package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 闪存解析器
 * Created by ChenRui on 2017/9/25 0025 17:16.
 */
public class MomentParser implements IHtmlParser<List<MomentBean>> {

    private final MomentCommentHelper mMomentCommentHelper = new MomentCommentHelper();


    @Override
    public List<MomentBean> parse(Document doc, String html) {
        List<MomentBean> result = new ArrayList<>();

        Elements elements = doc.select("#feed_list li");

        for (Element element : elements) {
            MomentBean m = new MomentBean();
            String id = ApiUtils.getNumber(element.select(".feed_body").attr("id"));
            if (TextUtils.isEmpty(id)) continue; // 跳过ID为空的

            m.setId(id); // 主键
            m.setAuthorName(element.select(".ing-author").text()); // 作者名称
            m.setAvatar(ApiUtils.getUrl(element.select(".feed_avatar a img").attr("src"))); //头像地址
            m.setContent(element.select(".ing_body").html()); // 内容
            m.setPostTime(element.select(".ing_time").text()); // 发布时间
            m.setCommentCount(ApiUtils.getCount(element.select(".ing_reply").text().replace("回应", ""))); // 评论数量
            m.setUserAlias(ApiUtils.getUserAlias(element.select(".ing_reply").attr("onclick"))); // 用户域
            m.setBlogApp(ApiUtils.getBlogApp(element.select(".ing-author").attr("href"))); // blogApp
            m.setSourceUrl(ApiUtils.getUrl(element.select(".ing-author").attr("href")).replace("home", "ing") + "status/" + id); // blogApp

            // 解析评论
            m.setCommentList(mMomentCommentHelper.parseCommentInList(element));
            // 解析图片
            mMomentCommentHelper.parseImageList(m, element.select(".ing_body a"));
            result.add(m);
        }


        return result;
    }
}
