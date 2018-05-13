package rae.com.cnblogs.sdk;

import android.webkit.CookieManager;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 用户接口测试
 * Created by ChenRui on 2017/1/14 01:09.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class UserApiTest extends BaseTest {
    IUserApi mApi;

    @Override
    @Before
    public void setup() {
        super.setup();
        mApi = getApiProvider().getUserApi();
    }

    @Override
    protected void autoLogin() {
        CookieManager.getInstance().removeAllCookie();
        CookieManager.getInstance().flush();
    }

    /**
     * 登录
     */
    @Test
    public void testLogin() {
//        String userName = ApiEncrypt.encrypt("chenrui7");
//        String pwd = ApiEncrypt.encrypt("chenrui123456789");
//        runTest("testLogin", mApi.login(null, userName, pwd, true));
    }

    /**
     * 获取用户信息
     */
    @Test
    public void testUserInfo() {
        // 先获取到blogApp信息，然后再根据blogApp获取用户信息
        runTest("testUserInfo", mApi.getUserBlogAppInfo().flatMap(new Function<UserInfoBean, ObservableSource<UserInfoBean>>() {
            @Override
            public ObservableSource<UserInfoBean> apply(UserInfoBean u) {
                return mApi.getUserInfo(u.getBlogApp());
            }
        }));
    }
}
