package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.ApiErrorCode;
import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.Empty;

import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * 登录错误，通过博客园接口登录成功的都会返回JSON数据，所以这里处理返回HTML时候的逻辑，一般都是登录错误了
 * Created by ChenRui on 2017/7/20 0020 10:41.
 */
public class LoginParser implements IHtmlParser<Empty> {

    @Override
    public Empty parse(Document document, String html) throws IOException {

        if (document.title().contains("用户登录")) {
            throw new CnblogsApiException(ApiErrorCode.LOGIN_EXPIRED, "用户名或者密码错误");
        }

        throw new CnblogsApiException("未知的登录错误");
    }
}
