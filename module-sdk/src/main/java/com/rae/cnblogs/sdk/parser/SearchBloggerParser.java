package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.UserInfoBean;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 博主搜索解析
 * Created by ChenRui on 2017/2/8 0008 9:47.
 */
public class SearchBloggerParser implements IHtmlParser<List<UserInfoBean>> {

    @Override
    public List<UserInfoBean> parse(Document document, String html) {
        List<UserInfoBean> result = new ArrayList<>();
        Elements elements = document.select("entry");
        for (Element element : elements) {
            UserInfoBean m = new UserInfoBean();
            m.setBlogApp(element.select("blogapp").text());
            m.setDisplayName(element.select("title").text());
            m.setAvatar(element.select("avatar").text());
//            m.setJoinDate(ApiUtils.getDate(element.select("updated").text()));
            result.add(m);
        }
        return result;
    }
}
