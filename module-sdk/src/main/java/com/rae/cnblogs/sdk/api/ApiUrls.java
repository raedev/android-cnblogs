package com.rae.cnblogs.sdk.api;

/**
 * 接口地址
 * Created by ChenRui on 2016/11/30 0030 16:46.
 */
final class ApiUrls {


    //region 博客园官方接口

    // 修改密码
    static final String API_USER_PASSWORD = "https://home.cnblogs.com/user/ChangPwd";

    // 更新头像
    static final String API_USER_AVATAR = "https://upload.cnblogs.com/avatar/crop";


    // 修改昵称
    static final String API_USER_NICKNAME = "https://home.cnblogs.com/user/ChangeDisplayName";

    // 修改账号
    static final String API_USER_ACCOUNT = "https://home.cnblogs.com/user/ChangeLoginName";

    // 账号设置
    static final String API_SETTING_ACCOUNT = "https://home.cnblogs.com/set/account";

    // 博客-列表
    static final String API_BLOG_LIST = "https://www.cnblogs.com/mvc/AggSite/PostList.aspx?ItemListActionName=PostList";

    // 博客-内容
    static final String API_BLOG_CONTENT = "http://wcf.open.cnblogs.com/blog/post/body/{id}";

    // 博客-点赞
    static final String API_BLOG_LIKE = "http://www.cnblogs.com/mvc/vote/VoteBlogPost.aspx?voteType=Digg&isAbandoned=false";

    /**
     * 博客-取消点赞
     */
    static final String API_BLOG_UNLIKE = "http://www.cnblogs.com/mvc/vote/VoteBlogPost.aspx?voteType=Digg&isAbandoned=true";

    // 博客-评论-列表
    static final String API_BLOG_COMMENT_LIST = "http://www.cnblogs.com/mvc/blog/GetComments.aspx";

    // 博客-评论-发表
    static final String API_BLOG_COMMENT_ADD = "http://www.cnblogs.com/mvc/PostComment/Add.aspx";

    // 博客-评论-发表
    static final String API_BLOG_COMMENT_DELETE = "http://www.cnblogs.com/mvc/comment/DeleteComment.aspx?pageIndex=0";

    // 用户-当前用户信息
    static final String API_USER_INFO = "https://home.cnblogs.com/ajax/user/CurrentIngUserInfo";

    // 用户-信息
    static final String API_USER_CENTER = "https://home.cnblogs.com/u/{blogApp}";

    // 用户-动态
    static final String API_USER_FEED = "https://home.cnblogs.com/u/{blogApp}/feed/{page}.html";

    // 用户-登录
    static final String API_SIGN_IN = "https://passport.cnblogs.com/user/signin";

    // 新闻-列表
    static final String API_NEWS_LIST = "http://wcf.open.cnblogs.com/news/recent/paged/{page}/20";

    // 新闻-内容
    static final String API_NEWS_CONTENT = "http://wcf.open.cnblogs.com/news/item/{id}";

    // 新闻-评论-列表
    static final String API_NEWS_COMMENT = "https://news.cnblogs.com/CommentAjax/GetComments";

    // 知识库-列表
    static final String API_KB_LIST = "https://home.cnblogs.com/kb/page/{page}/";

    // 知识库-点赞
    static final String API_KB_LIKE = "https://kb.cnblogs.com/mvcajax/vote/VoteArticle?voteType=Digg";

    // 收藏-添加
    static final String API_BOOK_MARKS_ADD = "https://wz.cnblogs.com/api/wz";

    // 收藏-标签
    static final String API_BOOK_MARKS_TAGS = "https://wz.cnblogs.com/mytag/";

    // 收藏-删除
    static final String API_BOOK_MARKS_DELETE = "https://wz.cnblogs.com/api/wz/{id}";

    // 收藏-列表
    static final String API_BOOK_MARKS_LIST = "https://wz.cnblogs.com/my/{page}.html";

    // 收藏-标签列表
    static final String API_BOOK_MARKS_TAG_LIST = "https://wz.cnblogs.com/my/{tag}/{page}.html";

    // 知识库-内容
    static final String API_KB_CONTENT = "https://kb.cnblogs.com/page/{id}/";

    // 新闻-评论-列表
    static final String API_NEWS_COMMENT_ADD = "https://news.cnblogs.com/Comment/InsertComment";

    // 新闻-评论-删除
    static final String API_NEWS_COMMENT_DELETE = "https://news.cnblogs.com/Comment/DelComment";

    // 新闻-点赞
    static final String API_NEWS_LIKE = "https://news.cnblogs.com/News/VoteNews?action=agree";

    // 朋友-关注-列表
    static final String API_FRIENDS_FOLLOW_LIST = "https://home.cnblogs.com/u/{blogApp}/relation/following";

    // 朋友-搜索
    static final String API_FRIENDS_SEARCH = "https://home.cnblogs.com/user/Search.aspx";

    // 朋友-粉丝-列表
    static final String API_FRIENDS_FANS_LIST = "https://home.cnblogs.com/u/{blogApp}/relation/followers";

    // 朋友-关注-发起
    static final String API_FRIENDS_FOLLOW = "https://home.cnblogs.com/ajax/follow/followUser?remark=";

    // 朋友-关注-取消
    static final String API_FRIENDS_UN_FOLLOW = "https://home.cnblogs.com/ajax/follow/RemoveFollow?isRemoveGroup=false";

    // 朋友-博客列表
    static final String API_FRIENDS_BLOG_LIST = "http://wcf.open.cnblogs.com/blog/u/{blogApp}/posts/{page}/20";

    // 搜索-百度建议
    static final String API_BAIDU_SUGGESTION = "http://suggestion.baidu.com/su?cb=cnblogs";

    // 搜索-博客-作者
    static final String API_SEARCH_BLOGGER = "http://wcf.open.cnblogs.com/blog/bloggers/search";

    // 搜索-博客-内容
    static final String API_SEARCH_BLOG_LIST = "https://zzk.cnblogs.com/s/blogpost";

    // 搜索-个人博客
    static final String API_SEARCH_BLOG_APP = "https://zzk.cnblogs.com/s/blogpost";

    // 搜索-知识库-列表
    static final String API_SEARCH_KB_LIST = "https://zzk.cnblogs.com/s/kb";

    // 搜索-新闻-列表
    static final String API_SEARCH_NEWS_LIST = "https://zzk.cnblogs.com/s/news";

    // 闪存-全站
    static final String API_MOMENT_LIST = "https://ing.cnblogs.com/ajax/ing/GetIngList?PageSize=30";

    // 闪存- 评论
    static final String API_MOMENT_SINGLE_COMMENTS = "https://ing.cnblogs.com/ajax/ing/SingleIngComments?PageSize=30";
    // 闪存- 详情
    static final String API_MOMENT_DETAIL = "https://ing.cnblogs.com/u/{user}/status/{ingId}/";

    // 闪存-发布
    static final String API_MOMENT_PUBLISH = "https://ing.cnblogs.com/ajax/Ing/MobileIngSubmit";

    // 闪存-删除
    static final String API_MOMENT_DEL = "https://ing.cnblogs.com/ajax/ing/del";

    // 闪存-评论删除
    static final String API_MOMENT_COMMENT_DEL = "https://ing.cnblogs.com/ajax/ing/DeleteComment";

    // 闪存-更新回复我的消息为已阅读
    static final String API_MOMENT_COMMENT_UPDATE_READ = "https://ing.cnblogs.com/ajax/ing/UpdateReplyToMeViewStatus";

    // 闪存-更新提到我的消息为已阅读
    static final String API_MOMENT_AT_UPDATE_READ = "https://ing.cnblogs.com/ajax/ing/UpdateMentionViewStatus";

    // 闪存-发表评论
    static final String API_MOMENT_POST_COMMENT = "https://ing.cnblogs.com/ajax/ing/PostComment";

    // 闪存-回复我的数量
    static final String API_MOMENT_REPLY_COUNT = "https://ing.cnblogs.com/ajax/ing/UnviewedReplyToMeCount";

    // 闪存-提到我的数量
    static final String API_MOMENT_AT_COUNT = "https://ing.cnblogs.com/ajax/ing/UnviewedMentionCount";

    // 上传图片
    static final String API_POST_IMAGE = "https://upload.cnblogs.com/imageuploader/processupload?host=www.cnblogs.com";

    // 上传头像图片
    static final String API_UPLOAD_AVATAR_IMAGE = "https://upload.cnblogs.com/ImageUploader/TemporaryAvatarUpload";

    // 博客园开通状态
    static final String API_BLOG_CHECK_OPEN = "https://passport.cnblogs.com/BlogApply.aspx";

    //endregion

    //region RAE 博客园服务端接口

    /*
     * 该接口下面是个人服务端接口，暂不开放
     * */

    // 服务端接口地址
    private static final String RAE_API_BASIC = "https://raedev.io/cnblogs/";
    // 启动页
    static final String RAE_API_AD_LAUNCHER = RAE_API_BASIC + "ad/launcher";
    // 检查更新
    static final String RAE_API_CHECK_VERSION = RAE_API_BASIC + "version/{versionCode}";
    // 热门搜索
    static final String RAE_API_SEARCH = RAE_API_BASIC + "search";
    // 系统消息
    static final String RAE_API_MESSAGE = RAE_API_BASIC + "messages/system";
    // 消息个数
    static final String RAE_API_MESSAGE_COUNT = RAE_API_BASIC + "messages/system/count";
    // 搜索-热搜
    static final String API_SEARCH_HOT = RAE_API_BASIC + "search/keyword/hot";

    //endregion


    //region RAE 博客园代理接口

    /*
     * 该接口下完全开放，为了优化客户端的用户体验，补充部分博客园接口的缺陷
     * 服务器资源有限，并且没有做任何限制，请各位按需调用。
     * 服务端抓取数据的原理同客户端，都是通过HTML解析来实现。
     * 开源地址：https://github.com/raedev/cnblogs-gateway-sdk
     * @author RAE
     * @date 2019-01-29
     * */

    // 基础路径
    private static final String RAE_CNBLOGS_BASE_URL = "http://raedev.io:8071";

    // 热门搜索榜单
    static final String RAE_CNBLOGS_RANKING_SEARCH = RAE_CNBLOGS_BASE_URL + "/ranking/search";
    // 阅读榜单
    static final String RAE_CNBLOGS_RANKING_READ = RAE_CNBLOGS_BASE_URL + "/ranking/read";
    // 收藏榜单
    static final String RAE_CNBLOGS_RANKING_FAVORITE = RAE_CNBLOGS_BASE_URL + "/ranking/favorite";
    // 大神榜单
    static final String RAE_CNBLOGS_RANKING_AUTHOR = RAE_CNBLOGS_BASE_URL + "/ranking/author";
    // 博客详情
    static final String RAE_CNBLOGS_BLOG_DETAIL = RAE_CNBLOGS_BASE_URL + "/blog/detail";

    // 博问 - 未解决
    static final String RAE_CNBLOGS_QUESTION_UNSOLVED = RAE_CNBLOGS_BASE_URL + "/question/unsolved";
    static final String RAE_CNBLOGS_QUESTION_HIGH_SCORE = RAE_CNBLOGS_BASE_URL + "/question/highscore";
    static final String RAE_CNBLOGS_QUESTION_MY = RAE_CNBLOGS_BASE_URL + "/question/my";

    //endregion

    //region 辅助接口

    /**
     * 新浪短链接接口
     * https://www.douban.com/note/249723561/
     */
    static final String API_SINA_SHORTEN = "http://api.t.sina.com.cn/short_url/shorten.json";

    //endregion

}
