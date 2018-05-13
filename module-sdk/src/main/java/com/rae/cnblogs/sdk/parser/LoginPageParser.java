package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.bean.LoginToken;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录页面HTML解析
 * Created by ChenRui on 2017/7/20 0020 10:41.
 */
public class LoginPageParser implements IHtmlParser<LoginToken> {

    @Override
    public LoginToken parse(Document document, String html) throws IOException {
        Matcher matcher = Pattern.compile("'VerificationToken.+'").matcher(html);
        if (matcher.find()) {
            String text = matcher.group();
            String[] groups = text.split("'");

            if (groups.length < 3) {
                throw new CnblogsApiException("VerificationToken正则表达式匹配长度不对，实际为：" + groups.length);
            }

            LoginToken token = new LoginToken();
            token.setVerificationToken(groups[3]);

            return token;
        }

        throw new CnblogsApiException("VerificationToken正则表达式匹配失败");
    }
}
