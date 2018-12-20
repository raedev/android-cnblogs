package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.FriendsInfoBean;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 博主信息解析器
 * Created by ChenRui on 2017/2/7 0007 15:31.
 */
public class FriendsInfoParser extends AbsUserInfoParser<FriendsInfoBean> {

    @Override
    public FriendsInfoBean parse(Document document, String html) {
        FriendsInfoBean result = new FriendsInfoBean();
        onParseUserInfo(result, document);
        result.setFollows(document.select("#following_count").text());
        result.setFans(document.select("#follower_count").text());
        result.setFollowed(document.select("#unFollowedPanel[style*=none]").size() > 0);




        return result;
    }
}
