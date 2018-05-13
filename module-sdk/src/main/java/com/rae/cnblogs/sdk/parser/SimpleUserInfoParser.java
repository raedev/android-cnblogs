package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

/**
 * 简单的用户信息解析
 * Created by ChenRui on 2017/6/7 0007 19:15.
 */
public class SimpleUserInfoParser implements IJsonParser<UserInfoBean> {


    @Override
    public UserInfoBean parse(String json) {
        UserInfoBean u = new UserInfoBean();
        try {
            JSONObject object = new JSONObject(json);
            String src = Jsoup.parse(object.getString("Avatar")).select("img").attr("src");
            Elements userName = Jsoup.parse(object.getString("Username")).select("a");
            u.setAvatar(ApiUtils.getUrl(src));
            u.setBlogApp(userName.attr("href").replace("/u/", "").replace("/", ""));
            u.setDisplayName(userName.text());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}
