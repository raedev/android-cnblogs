package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户信息解析
 * Created by ChenRui on 2017/2/7 0007 15:31.
 */
public abstract class AbsUserInfoParser<T> implements IHtmlParser<T> {


    protected void onParseUserInfo(UserInfoBean result, Document document) {
        Elements scripts = document.select("script");
        for (Element script : scripts) {
            String text = script.html();

            /*
             *  <script type="text/javascript">
             *       var currentUserId = "bac2687c-5679-e111-aa3f-842b2b196315";
             *       var currentUserName = "chenXiaorui";
             *       var isLogined = true;
             *  </script>
             * */
            if (text.contains("currentUserId")) {
                Matcher matcher = Pattern.compile("=.*;").matcher(text);
                if (matcher.find()) {
                    String group = matcher.group();
                    String value = group.replace("=", "").replace("\"", "").replace(";", "").replace("\n", "").trim();
                    // 解析用户ID userId
                    result.setUserId(value);
                }
            }

        }
        Element joinDateElement = document.selectFirst("span[title]");
        if (joinDateElement != null && joinDateElement.attr("title").contains("入园")) {
            result.setJoinDate(joinDateElement.text());
        }

        result.setAvatar(ApiUtils.getUrl(document.select(".img_avatar").attr("src")));
        // 解析blogApp
        result.setBlogApp(parseBlogApp(document.select(".link_account").attr("href")));
        if (TextUtils.isEmpty(result.getBlogApp())) {
            result.setBlogApp(ApiUtils.getBlogApp(document.select(".gray").text()));
        }
        result.setDisplayName(document.select(".display_name").text());
        result.setRemarkName(document.select("#remarkId").text());

        // 解析园龄
        Elements profileLi = document.select("#user_profile li");
        for (Element element : profileLi) {
            String text = element.text();
            if (text.contains("自我介绍")) {
                result.setIntroduce(text.replace("自我介绍：", "").trim());
            }
//            if (text.contains("园龄")) {
//                result.setJoinDate(text.replace("园龄：", "").trim());
//            }
        }
    }

    /**
     * 解析blogApp
     *
     * @param url 格式：/u/ztfjs/detail/
     */
    private String parseBlogApp(String url) {
        if (TextUtils.isEmpty(url)) return url;
        int len = 1;
        if (url.startsWith("/")) {
            len = 2;
        }
        String[] split = url.split("/");
        if (split.length > len) {
            return split[len].trim();
        }
        return null;
    }
}
