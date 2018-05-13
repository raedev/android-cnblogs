package com.rae.cnblogs.sdk.bean;

/**
 * 登录凭证
 * Created by ChenRui on 2017/10/7 0007 23:58.
 */
public class LoginToken {

    // 登录界面校验的TOKEN
    private String verificationToken;

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}
