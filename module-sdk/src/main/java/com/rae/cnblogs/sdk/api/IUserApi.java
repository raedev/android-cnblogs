package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.ApiOptions;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.JsonParser;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.LoginToken;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.parser.LoginPageParser;
import com.rae.cnblogs.sdk.parser.LoginParser;
import com.rae.cnblogs.sdk.parser.SimpleUserInfoParser;
import com.rae.cnblogs.sdk.parser.UserInfoParser;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 用户接口，用户管理对应{@link UserProvider} 类
 * Created by ChenRui on 2017/1/14 01:00.
 */
public interface IUserApi {

    /**
     * 登录，登录成功返回cookie，api已经自动处理cookie持久化，所以这里返回的是空对象
     *
     * @param verificationToken 登录凭证
     * @param userName          用户名
     * @param password          密码
     * @param remember          是否记住登录，建议传TRUE
     */
    @POST(ApiUrls.API_SIGN_IN)
    @FormUrlEncoded
    @Headers({
            JsonBody.CONTENT_TYPE,
            JsonBody.XHR,
            "Cookie:AspxAutoDetectCookieSupport=1; _gat=1;",
            "Accept: application/json, text/javascript, */*; q=0.01",
            "Accept-Encoding: gzip, deflate, br",
            "Accept-Language: zh-CN,zh;q=0.8",
            "Referer: https://passport.cnblogs.com/user/signin"
    })
    @Parser(LoginParser.class)
    Observable<Empty> login(@Header("VerificationToken") String verificationToken,
                            @Field("input1") String userName,
                            @Field("input2") String password,
                            @Field("remember") boolean remember);

    /**
     * 请求登录页面，获取登录凭证
     */
    @GET(ApiUrls.API_SIGN_IN)
    @Headers({
            "Cookie:AspxAutoDetectCookieSupport=1"
    })
    @Parser(LoginPageParser.class)
    @ApiOptions(ignoreLogin = true)
    Observable<LoginToken> loadSignInPage();


    /**
     * 登录后获取blogApp信息，然后需要再次调用{@link #getUserInfo(String)} 来获取真正的用户信息。
     * <p>
     * 登录后的用户信息可通过{@link UserProvider#getLoginUserInfo()}来获取
     * </p>
     */
    @POST(ApiUrls.API_USER_INFO)
    @Headers({JsonBody.XHR})
    @JsonParser(SimpleUserInfoParser.class)
    Observable<UserInfoBean> getUserBlogAppInfo();

    /**
     * 获取用户信息，不需再保存登录信息了。你可以通过{@link UserProvider} 来管理用户
     */
    @POST(ApiUrls.API_USER_CENTER)
    @Headers({JsonBody.XHR})
    @Parser(UserInfoParser.class)
    Observable<UserInfoBean> getUserInfo(@Path("blogApp") String blogApp);

}
