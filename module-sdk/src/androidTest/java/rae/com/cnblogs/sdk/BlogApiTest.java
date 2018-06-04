package rae.com.cnblogs.sdk;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.ICategoryApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 博客测试
 * Created by ChenRui on 2016/11/30 00:15.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class BlogApiTest extends BaseTest {

    private IBlogApi mApi;

    @Override
    @Before
    public void setup() {
        super.setup();
        mApi = getApiProvider().getBlogApi();
    }

    /**
     * 获取分类
     */
    @Test
    public void testBlogInfo() {
        runTest("testBlogInfo", mApi.getBlog("https://www.cnblogs.com/zx3180/p/9121204.html"));
    }

    /**
     * 获取分类
     */
    @Test
    public void testCategory() {
        ICategoryApi api = getApiProvider().getCategoriesApi();
        runTest("testCategory", api.getCategories());
    }

    /**
     * 首页博客列表
     */
    @Test
    public void testHomeBlogs() {
        runTest("testHomeBlogs", mApi.getBlogList(1, null, null, null));
    }

    /**
     * 博文内容
     */
    @Test
    public void testContent() {
        runTest("testContent", mApi.getBlogContent("6246780"));
    }

    /**
     * 博客评论列表
     */
    @Test
    public void testComment() {
        runTest("testComment", mApi.getBlogComments(1, "574285", "pengze0902"));
    }

    /**
     * 知识库列表
     */
    @Test
    public void testKB() {
        runTest("testKB", mApi.getKbArticles(1));
    }

    /**
     * 知识库内容
     */
    @Test
    public void testKBContent() {
        runTest("testKBContent", mApi.getKbContent("569056"));
    }

    /**
     * 博文点赞
     */
    @Test
    public void testLikeBlog() {
        runTest("testLikeBlog", mApi.likeBlog("6323406", "silenttiger"));
    }

    /**
     * 取消博文点赞
     */
    @Test
    public void testUnLikeBlog() {
        runTest("testUnLikeBlog", mApi.unLikeBlog("6323406", "silenttiger"));
    }

    /**
     * 知识库点赞
     */
    @Test
    public void testLikeKb() {
        runTest("testLikeKb", mApi.likeKb("569992"));
    }

    /**
     * 发表博客评论
     */
    @Test
    public void testAddCommentBlog() {
        runTest("testAddCommentBlog", mApi.addBlogComment("6323406", "silenttiger", null, "test comment"));
    }

    /**
     * 删除博客评论
     */
    @Test
    public void testDelCommentBlog() {
        runTest("testDelCommentBlog", mApi.deleteBlogComment("6323406"));
    }
}
