package rae.com.cnblogs.sdk;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.rae.cnblogs.sdk.api.IBookmarksApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 收藏接口测试
 * Created by ChenRui on 2017/1/14 01:09.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class BookmarksApiTest extends BaseTest {
    IBookmarksApi mApi;

    @Override
    @Before
    public void setup() {
        super.setup();
        mApi = getApiProvider().getBookmarksApi();
    }

    @Test
    public void testAddBookmarks() {
        runTest("testAddBookmarks", mApi.addBookmarks("百度收藏测试from app", "我是描述信息-rae", "http://www.baidu.com"));
    }

    @Test
    public void testGetBookmarks() {
        runTest("testGetBookmarks", mApi.getBookmarks(1));
    }

    @Test
    public void testDeleteBookmarks() {
        runTest("testDeleteBookmarks", mApi.delBookmarks("3987443"));
    }
}
