package rae.com.cnblogs.sdk;

import android.util.Log;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 闪存接口测试
 * Created by ChenRui on 2016/11/30 00:15.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class MomentApiTest extends BaseTest {

    private IMomentApi mApi;

    @Override
    public void setup() {
        super.setup();
        mApi = CnblogsApiFactory.getInstance(mContext).getMomentApi();
    }

    @Test
    public void testPublish() {
        runTest("testPublish", mApi.publish("继续努力，加油！", 0));
    }

    @Test
    public void testMoments() {
        runTest("testMoments", mApi.getMoments(IMomentApi.MOMENT_TYPE_FOLLOWING, 1, System.currentTimeMillis()));
    }


    @Test
    public void testReg() {
        String text = "showCommentBox(1243228,607820);return false;";
        Log.e("rae", "测试结果：" + ApiUtils.getUserAlias(text));
    }

}
