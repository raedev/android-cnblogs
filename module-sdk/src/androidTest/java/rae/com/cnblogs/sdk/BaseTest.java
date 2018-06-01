package rae.com.cnblogs.sdk;

import android.app.Application;
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
import com.rae.cnblogs.sdk.db.DbCnblogs;

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
        UserProvider.init((Application) mContext.getApplicationContext());
        DbCnblogs.init(mContext);
        // 模拟已经登录, 需要.CNBlogsCookie和.Cnblogs.AspNetCore.Cookies
        autoLogin();
    }

    protected void autoLogin() {
        String url = "wz.cnblogs.com";
        String cookie = "257609FA8BD9AB43F07A4B4110DEA5561685E8827D2536EB50CB07E6BD72A852B65E7121C7282323F9689202135E707D72184476D358B429FD5F32F00103BE9C02414571D796E42637836235120D40B4D03C3CC6";
        String netCoreCookie = "CfDJ8FHXRRtkJWRFtU30nh_M9mAcOtlDEoxNOvReESDtP-LGb9f1uAbknAYX_5g3d2Y-mOtPlu_vqplSTz3mRrRqcUrNE9QYYCP7cqzVzbnLztUF38wiIP6XaW10kl0QvUi_xEdaOv62KWeqYeZMAwtkOqw4H4ark-KBNhDzGAPDG3L0_5ymM3XA8f8RBjbNG5ZDE7bAwIQq3GtI4oX_4rl5uoS2Xw8n36ESUB4tQJ0kdOJf8GeKYdOWoZznhRwmrRiUl_VtGOVryQYV3-8hys11UEodQTYM1MLAL8QhJLp6ZKOAI3UMLQfcHMfKNL7_bZhd5w";
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, ".CNBlogsCookie=" + cookie + "; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.setCookie(url, ".Cnblogs.AspNetCore.Cookies=" + netCoreCookie + "; domain=.cnblogs.com; path=/; HttpOnly");
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
