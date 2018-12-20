package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.ApiOptions;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.JsonParser;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.LoginToken;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.parser.AccountParser;
import com.rae.cnblogs.sdk.parser.LoginPageParser;
import com.rae.cnblogs.sdk.parser.LoginParser;
import com.rae.cnblogs.sdk.parser.SimpleUserInfoParser;
import com.rae.cnblogs.sdk.parser.UploadAvatarParser;
import com.rae.cnblogs.sdk.parser.UserInfoParser;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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


    /**
     * 上传头像图片
     *
     * @param fileName       文件名称
     * @param headerFileName 文件名称
     * @param file           文件
     * @return 可访问的网络路径
     */
    @POST(ApiUrls.API_UPLOAD_AVATAR_IMAGE)
    @Headers({
            "X-Mime-Type: image/jpeg",
            "Content-Type: application/octet-stream",
            "Accept: */*",
            "Referer: https://www.cnblogs.com",
            "Origin: https://upload.cnblogs.com",
            "X-Requested-With: XMLHttpRequest"
    })
    @JsonParser(UploadAvatarParser.class)
    Observable<String> uploadAvatarImage(@Query("qqfile") String fileName, @Header("X-File-Name") String headerFileName, @Body RequestBody file);


    /**
     * 更新头像
     *
     * @return
     */
    @POST(ApiUrls.API_USER_AVATAR)
    @FormUrlEncoded
    @JsonParser(UploadAvatarParser.class)
    Observable<String> updateAvatar(@Field("x") int x,
                                    @Field("y") int y,
                                    @Field("w") int w,
                                    @Field("h") int h,
                                    @Field("imgsrc") String url);

    /**
     * 更新昵称
     *
     * @return
     */
    @POST(ApiUrls.API_USER_NICKNAME)
    @Headers({JsonBody.CONTENT_TYPE, JsonBody.XHR})
    @FormUrlEncoded
    Observable<String> updateNickName(@Field("oldDisplayName") String oldName,
                                      @Field("newDisplayName") String newName);

    /**
     * 更新账号
     *
     * @return
     */
    @POST(ApiUrls.API_USER_ACCOUNT)
    @Headers({JsonBody.CONTENT_TYPE, JsonBody.XHR})
    @FormUrlEncoded
    Observable<String> updateAccount(@Field("oldLoginName") String oldName,
                                     @Field("newLoginName") String newName);

    /**
     * 更新密码
     */
    @POST(ApiUrls.API_USER_PASSWORD)
    @Headers({JsonBody.CONTENT_TYPE, JsonBody.XHR})
    @FormUrlEncoded
    Observable<String> changePassword(@Field("oldpwd") String oldPwd,
                                      @Field("newpwd") String newPwd);

    /**
     * 获取账号信息
     */
    @GET(ApiUrls.API_SETTING_ACCOUNT)
    @Parser(AccountParser.class)
    Observable<String> getUserAccount();

}
