package rae.com.cnblogs.sdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.webkit.CookieManager;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.github.raee.runit.RUnitTestLogUtils;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.UserProvider;

import org.junit.Before;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 接口测试基类
 * Created by ChenRui on 2016/11/30 00:16.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class BaseTest {

    private static final String TAG = "CNBLOGS-API-TEST";

    protected Context mContext;

    @Before
    public void setup() {
        mContext = InstrumentationRegistry.getTargetContext();
        UserProvider.init(mContext);
//        DbCnblogs.init(mContext);
        // 模拟已经登录, 需要.CNBlogsCookie和.Cnblogs.AspNetCore.Cookies
        autoLogin();
    }

    protected void autoLogin() {
        String url = "www.cnblogs.com";
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, ".CNBlogsCookie=23C783112C89888B509652C2E962F88CF742E008BBD65B72381446683D73615B621C71846010E7A134A78DA4F421DDAC42AA504B70709D67283D0AEBAA274E4CD6D029D6DA9B089048AD24FE5D369F4B7261790536E75A2AC51883F533878D1EE20AA30B; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.setCookie(url, ".Cnblogs.AspNetCore.Cookies=CfDJ8BMYgQprmCpNu7uffp6PrYZAlx7wnnFIHwWOK8_lsJJwLMVz-wlYX9HKRB146BmGnDk4Vt_azop7Ce64ouMrxCne5EjwlIZVp1azwx4ZgnLqOGRTfGEzFDMMpVRmEZ32MZaEBXSAJGl_PsOjdlPEJeAevz_WkwvF3BaD5wkAPQnpGhjnkL4HkN0YE_jztsJkZfaUAYBr6BEjALTy75OOH_7NLiLZmOg0tjodbckftsi4HtjRtHwX2T4imGGEOeC84VdYndayUwNNZVvEbX0nOd2jeHRjWYpr7YzxcgajVhUh; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.flush();
    }

    protected CnblogsApiProvider getApiProvider() {
        return CnblogsApiFactory.getInstance(mContext);
    }

    protected <T> void runTest(final String testName, Observable<T> observable) {
        observable.subscribeOn(Schedulers.io())
                .doFinally(new Action() {
                    @Override
                    public void run() {
                        AndroidRUnit4ClassRunner.finish(testName);
                    }
                })
                .subscribe(new DefaultObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        if (t == null) {
                            Log.w(TAG, "返回对象为空！");
                            return;
                        }
                        if (t instanceof Empty) {
                            Log.i(TAG, "返回对象为Empty");
                            return;
                        }
                        if (t instanceof String) {
                            Log.i(TAG, t.toString());
                            return;
                        }
                        RUnitTestLogUtils.print(TAG, t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "[" + testName + "] 测试失败", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "[" + testName + "] 测试完成");
                    }
                });
    }
}
