package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import org.jsoup.nodes.Document;

/**
 * 用户信息解析
 * Created by ChenRui on 2017/2/7 0007 15:31.
 */
public class UserInfoParser extends AbsUserInfoParser<UserInfoBean> {

    @Override
    public UserInfoBean parse(Document document, String html) {
        UserInfoBean result = new UserInfoBean();
        onParseUserInfo(result, document);
        // 保存登录信息
        if (!TextUtils.isEmpty(result.getUserId())) {
            UserProvider.getInstance().setLoginUserInfo(result);
        }
        return result;
    }
}
