package com.rae.cnblogs;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.model.MomentMetaData;

import java.util.ArrayList;

/**
 * 路由
 * Created by ChenRui on 2016/12/6 23:49.
 */
public final class AppRoute {

    // WEB 登录
    public static final int REQ_CODE_WEB_LOGIN = 100;
    /**
     * 登录返回
     */
    public static final int REQ_LOGIN = 10086;
    /*朋友界面 - 来自粉丝*/
    public static final int ACTIVITY_FRIENDS_TYPE_FANS = 1;
    /*朋友界面 - 来自关注*/
    private static final int ACTIVITY_FRIENDS_TYPE_FOLLOW = 2;
    // 分类
    public static final int REQ_CODE_CATEGORY = 102;
    // 收藏
    public static final int REQ_CODE_FAVORITES = 103;
    // 博主
    public static final int REQ_CODE_BLOGGER = 104;
    // 发布闪存
//    public static final int REQ_POST_MOMENT = 105;
    // 图片选择
    public static final int REQ_IMAGE_SELECTION = 106;
    // 图片选择
    public static final int REQ_CODE_IMAGE_SELECTED = 107;

    /**
     * 博客内容
     */
    public static final String PATH_BLOG_CONTENT = "/app/blog/content";
    /**
     * 网页
     */
    public static final String PATH_WEB = "/web/index";
    /**
     * 意见反馈
     */
    public static final String PATH_FEEDBACK = "/app/feedback";
    /**
     * 首页
     */
    public static final String PATH_APP_HOME = "/app/home";
    /**
     * 登录
     */
    public static final String PATH_LOGIN = "/app/login";
    /**
     * 网页登录
     */
    public static final String PATH_WEB_LOGIN = "/web/login";
    /**
     * 粉丝以及关注
     */
    public static final String PATH_FRIENDS = "/app/friends";
    /**
     * 图片预览
     */
    public static final String PATH_IMAGE_PREVIEW = "/image/preview";
    /**
     * 博主主页
     */
    public static final String PATH_BLOGGER = "/blogger/index";

    /**
     * 栏目分类
     */
    public static final String PATH_CATEGORY = "/category/index";
    /**
     * 我的收藏
     */
    public static final String PATH_FAVORITE = "/favorite/index";
    /**
     * 设置
     */
    public static final String PATH_SETTING = "/app/setting";
    /**
     * 搜索
     */
    public static final String PATH_SEARCH = "/app/search";
    /**
     * 系统消息
     */
    public static final String PATH_SYSTEM_MESSAGE = "/system/message";
    /**
     * 字体设置
     */
    public static final String PATH_FONT_SETTING = "/font/setting";
    /**
     * 博客评论
     */
    public static final String PATH_BLOG_COMMENT = "/app/blog/comment";
    /**
     * 发布闪存
     */
    public static final String PATH_MOMENT_POST = "/moment/post";
    /**
     * 闪存详情
     */
    public static final String PATH_MOMENT_DETAIL = "/moment/detail";
    /**
     * 闪存消息
     */
    public static final String PATH_MOMENT_MESSAGE = "/moment/message";
    /**
     * 提到我的闪存消息
     */
    public static final String PATH_MOMENT_MENTION = "/moment/mention";
    /**
     * 图片选择
     */
    public static final String PATH_IMAGE_SELECTION = "/image/selection";
    /**
     * 关于我们
     */
    public static final String PATH_ABOUT_ME = "/app/about";
    /**
     * 浏览记录
     */
    public static final String PATH_BLOG_HISTORY = "/blog/history";

    /**
     * 初始化
     */
    static void init(@NonNull Application applicationContext, boolean debug) {
        if (debug) {
            debug();
        }
        ARouter.init(applicationContext);
    }

    public static void debug() {
        ARouter.openDebug();
        ARouter.openLog();
        ARouter.printStackTrace();
    }


    public static void route(Context context, String path) {
        ARouter.getInstance().build(path).navigation(context);
    }

    /**
     * 博客正文界面
     *
     * @param blogId 博客ID
     * @param type   博客类型
     */
    public static void routeToBlogContent(Context context, String blogId, BlogType type) {
        ARouter.getInstance().build(PATH_BLOG_CONTENT)
                .withString("blogId", blogId)
                .withString("type", type.getTypeName())
                .navigation(context);
    }

    /**
     * 博客正文界面
     *
     * @param blog 博客实体
     * @param type 博客类型
     */
    public static void routeToBlogContent(Context context, @NonNull BlogBean blog, @NonNull BlogType type) {
        // 不传递摘要和正文这些过大的数据。进去博文正文之后再从数据库拉取。已经在BlogBean里面处理大数据问题
        ARouter.getInstance().build(PATH_BLOG_CONTENT)
                .withParcelable("blog", blog)
                .withString("blogId", blog.getBlogId())
                .withString("type", type.getTypeName())
                .navigation(context);
    }

    /**
     * 网页
     *
     * @param url 路径
     */
    public static void routeToWeb(Context context, String url) {
        ARouter.getInstance().build(PATH_WEB).withString("url", url).navigation(context);
    }

    /**
     * 用户反馈
     */
    public static void routeToFeedback(Context context) {
        ARouter.getInstance().build(PATH_FEEDBACK)
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .navigation(context);
    }


    /**
     * 网页，新线程
     *
     * @param url 路径
     */
    public static void routeToWebNewTask(Context context, String url) {
        ARouter.getInstance().build(PATH_FEEDBACK)
                .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation(context);
    }

    /**
     * 主界面
     */
    public static void routeToMain(Context context) {
        ARouter.getInstance().build(PATH_APP_HOME)
                .withFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .navigation(context);
    }


    /**
     * 登录
     */
    public static void routeToLogin(Context context) {
        route(context, PATH_LOGIN);
    }

    /**
     * 登录带返回结果
     */
    public static void routeToLoginForResult(Activity context) {
        ARouter.getInstance().build(PATH_LOGIN).navigation(context, REQ_LOGIN);
    }

    /**
     * 跳转登录界面，有回调结果
     */
    public static void routeToWebLogin(Activity context) {
        ARouter.getInstance().build(PATH_WEB_LOGIN).navigation(context, REQ_CODE_WEB_LOGIN);
    }

    /**
     * 粉丝
     *
     * @param bloggerName 博主昵称
     * @param blogApp     博主ID
     */
    public static void routeToFans(Context context, String bloggerName, String blogApp) {
        routeToFriends(context, ACTIVITY_FRIENDS_TYPE_FANS, bloggerName, blogApp);
    }

    /**
     * 关注
     *
     * @param bloggerName 博主昵称
     * @param blogApp     博主ID
     */
    public static void routeToFollow(Context context, String bloggerName, String blogApp) {
        routeToFriends(context, ACTIVITY_FRIENDS_TYPE_FOLLOW, bloggerName, blogApp);
    }

    /**
     * 跳转到朋友界面
     *
     * @param type        来源类型，参考该类{@link #ACTIVITY_FRIENDS_TYPE_FANS}
     * @param bloggerName 博主昵称
     * @param blogApp     博主ID
     */
    private static void routeToFriends(Context context, int type, String bloggerName, String blogApp) {
        ARouter.getInstance().build(PATH_FRIENDS)
                .withString("blogApp", blogApp)
                .withString("bloggerName", bloggerName)
                .withInt("fromType", type)
                .navigation(context);
    }

    /**
     * 图片大图预览
     *
     * @param images   图片数组
     * @param position 跳转到低几张图片，默认传0
     */
    public static void routeToImagePreview(Activity context, @NonNull ArrayList<String> images, int position, ArrayList<String> selectedImages, int maxCount) {
        Postcard postcard = ARouter.getInstance().build(PATH_IMAGE_PREVIEW);
        postcard.withStringArrayList("images", images)
                .withInt("position", position)
                .withInt("maxCount", maxCount);
        if (selectedImages != null) {
            postcard.withStringArrayList("selectedImages", selectedImages);
        }
        postcard.navigation(context, REQ_CODE_IMAGE_SELECTED);
    }

    /**
     * 图片大图预览
     *
     * @param images   图片数组
     * @param position 跳转到低几张图片，默认传0
     */
    public static void routeToImagePreview(Activity context, @NonNull ArrayList<String> images, int position) {
        routeToImagePreview(context, images, position, null, 0);
    }

    /**
     * 图片大图预览
     *
     * @param images   图片数组
     * @param position 跳转到低几张图片，默认传0
     */
    public static void routeToImagePreview(Context context, @NonNull ArrayList<String> images, int position) {
        ARouter.getInstance().build(PATH_IMAGE_PREVIEW)
                .withStringArrayList("images", images)
                .withInt("position", position)
                .navigation(context);
    }


    /**
     * 图片查看
     *
     * @param imgUrl 图片路径
     */
    public static void routeToImagePreview(Activity context, @NonNull String imgUrl) {
        ArrayList<String> data = new ArrayList<>();
        data.add(imgUrl);
        routeToImagePreview(context, data, 0);
    }


    /**
     * 图片查看
     *
     * @param imgUrl 图片路径
     */
    public static void routeToImagePreview(Context context, @NonNull String imgUrl) {
        ArrayList<String> data = new ArrayList<>();
        data.add(imgUrl);
        routeToImagePreview(context, data, 0);
    }

    /**
     * 博主界面
     *
     * @param blogApp 博客APP
     */
    public static void routeToBlogger(Context context, @Nullable String blogApp) {
        if (TextUtils.isEmpty(blogApp)) {
            Log.w("AppRoute", "route to blogger error: blog is null!");
            return;
        }
        ARouter.getInstance().build(PATH_BLOGGER).withString("blogApp", blogApp).navigation(context);
    }

    /**
     * 博主界面
     *
     * @param blogApp 博客APP
     */
    public static void routeToBlogger(Activity context, String blogApp) {
        if (TextUtils.isEmpty(blogApp)) {
            Log.w("AppRoute", "route to blogger error: blog is null!");
            return;
        }
        ARouter.getInstance().build(PATH_BLOGGER).withString("blogApp", blogApp).navigation(context, REQ_CODE_BLOGGER);
    }


    /**
     * 分类管理
     */
    public static void routeToCategoryForResult(Activity context) {
        ARouter.getInstance().build(PATH_CATEGORY).navigation(context, REQ_CODE_CATEGORY);
    }

    /**
     * 我的收藏
     */
    public static void jumpToFavorites(Activity context) {
        ARouter.getInstance().build(PATH_FAVORITE).navigation(context, REQ_CODE_FAVORITES);
    }

    /**
     * 设置
     */
    public static void jumpToSetting(Context context) {
        route(context, PATH_SETTING);
    }

    /**
     * 搜索
     */
    public static void routeToSearch(Context context) {
        routeToSearch(context, 0);
    }

    /**
     * 搜索-新闻
     */
    public static void routeToSearchNews(Context context) {
        routeToSearch(context, 2);
    }

    /**
     * 搜索-知识库
     */
    public static void routeToSearchKb(Context context) {
        routeToSearch(context, 3);
    }

    /**
     * 搜索
     */
    private static void routeToSearch(Context context, int position) {
        ARouter.getInstance().build(PATH_SEARCH)
                .withInt("position", position)
                .navigation(context);
    }

    /**
     * 搜索-博主
     */
    public static void routeToSearchBlogger(Context context, String blogApp, String nickName) {
        ARouter.getInstance().build(PATH_SEARCH)
                .withString("blogApp", blogApp)
                .withString("nickName", nickName)
                .navigation(context);
    }

    /**
     * 系统消息
     */
    public static void routeToSystemMessage(Context context) {
        route(context, PATH_SYSTEM_MESSAGE);
    }

    /**
     * 字体设置
     */
    public static void routeToFontSetting(Context context) {
        route(context, PATH_FONT_SETTING);
    }

    /**
     * 博客评论
     */
    public static void routeToComment(Context context, BlogBean blog, BlogType type) {
        ARouter.getInstance().build(PATH_BLOG_COMMENT)
                .withString("type", type.getTypeName())
                .withParcelable("blog", blog)
                .navigation(context);
    }

    /**
     * 发布闪存
     */
    public static void routeToPostMoment(Activity context) {
        routeToPostMoment(context, null);
    }

    /**
     * 发布闪存
     */
    public static void routeToPostMoment(Activity context, @Nullable MomentMetaData data) {
        ARouter.getInstance().build(PATH_MOMENT_POST)
                .withParcelable(Intent.EXTRA_TEXT, data)
                .navigation(context);

    }

    /**
     * 闪存详情
     */
    public static void routeToMomentDetail(Context context, MomentBean data) {
        ARouter.getInstance().build(PATH_MOMENT_DETAIL)
                .withParcelable("data", data)
                .navigation(context);
    }

    /**
     * 闪存详情
     */
    public static void routeToMomentDetail(Context context, String userAlias, String ingId) {
        ARouter.getInstance().build(PATH_MOMENT_DETAIL)
                .withString("ingId", ingId)
                .withString("userId", userAlias)
                .navigation(context);
    }


    /**
     * 闪存消息
     */
    public static void routeToMomentMessage(Context context) {
        ARouter.getInstance().build(PATH_MOMENT_MESSAGE).navigation(context);
    }

    /**
     * 提到我的闪存
     */
    public static void jumpToMomentAtMe(Context context) {
        ARouter.getInstance().build(PATH_MOMENT_MENTION).navigation(context);
    }

    /**
     * 跳转到图片选择
     */
    public static void jumpToImageSelection(Activity context, ArrayList<String> selectedImages) {

        Postcard postcard = ARouter.getInstance().build(PATH_IMAGE_SELECTION);
        if (selectedImages != null) {
            postcard.withStringArrayList("selectedImages", selectedImages);
        }
        postcard.navigation(context, REQ_IMAGE_SELECTION);
    }

    /**
     * 关于我们
     */
    public static void jumpToAboutMe(Context context) {
        route(context, PATH_ABOUT_ME);
    }

    /**
     * 浏览记录
     */
    public static void routeToHistory(Context context) {
        route(context, PATH_BLOG_HISTORY);
    }
}
