package rae.com.cnblogs.sdk;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.INewsApi;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 新闻接口测试
 * Created by ChenRui on 2016/11/30 00:15.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class NewsApiTest extends BaseTest {

    private INewsApi mApi;

    @Override
    public void setup() {
        super.setup();
        mApi = CnblogsApiFactory.getInstance(mContext).getNewsApi();
    }

    @Test
    public void testNews() {
        runTest("testNews", mApi.getNews(1));
    }

    @Test
    public void testNewsContent() {
        runTest("testNewsContent", mApi.getNewsContent("561600"));
    }

    @Test
    public void testCommentList() {
        runTest("testCommentList", mApi.getNewsComment("574285", 1));
    }

    @Test
    public void testAddNewsComment() {
        // 发布一条新的新闻评论
        runTest("testAddNewsComment", mApi.addNewsComment("561600", "0", "test news comment!"));
    }

    @Test
    public void testDelNewsComment() {
        // 删除一条自己发布的新闻评论
        runTest("testDelNewsComment", mApi.deleteNewsComment("561600", "378551"));
    }

    /**
     * 新闻点赞
     */
    @Test
    public void testLikeNews() {
        runTest("testLikeNews", mApi.like("561600"));
    }
}
