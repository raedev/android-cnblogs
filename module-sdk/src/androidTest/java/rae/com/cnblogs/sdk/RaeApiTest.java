package rae.com.cnblogs.sdk;

import android.content.pm.PackageManager;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.rae.cnblogs.sdk.api.IRaeServerApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 我的接口测试
 * Created by ChenRui on 2016/11/30 00:15.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class RaeApiTest extends BaseTest {

    private IRaeServerApi mApi;

    @Override
    @Before
    public void setup() {
        super.setup();
        mApi = getApiProvider().getRaeServerApi();
    }

    @Test
    public void testLauncherAd() {
        runTest("testLauncherAd", mApi.getLauncherAd());
    }

    @Test
    public void testMessages() {
        runTest("testMessages", mApi.getMessages());
    }

    @Test
    public void testMessageCount() {
        runTest("testMessageCount", mApi.getMessageCount());
    }

    @Test
    public void testVersionUpdate() {
        int versionCode = 0;

        try {
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_META_DATA).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        runTest("testVersionUpdate", mApi.versionInfo(1, "1.0.0", "dev", "debug"));
    }
}
