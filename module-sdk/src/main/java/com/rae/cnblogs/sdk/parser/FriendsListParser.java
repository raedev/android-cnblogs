package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝以及关注用户列表解析器
 * Created by ChenRui on 2017/2/7 0007 17:41.
 */
public class FriendsListParser implements IHtmlParser<List<UserInfoBean>> {

    @Override
    public List<UserInfoBean> parse(Document document, String html) {
        List<UserInfoBean> result = new ArrayList<>();
        Elements elements = document.select(".avatar_list li");
        for (Element li : elements) {
            UserInfoBean m = new UserInfoBean();
            result.add(m);

            // 头像
            String avatarPath = ".avatar48 img"; // 搜索的
            if (li.select(avatarPath).size() <= 0) {
                avatarPath = ".avatar_pic img"; // 默认的
            }
            m.setAvatar(ApiUtils.getUrl(li.select(avatarPath).attr("src")));
            m.setUserId(li.attr("id"));
            m.setBlogApp(ApiUtils.getBlogApp(li.select("a[title]").attr("href")));
            m.setDisplayName(li.select("a[title]").attr("title"));
            m.setRemarkName(li.select(".remark_name a").eq(1).text());
            m.setHasFollow(li.select(".edit").hasText());
        }
        return result;
    }
}
