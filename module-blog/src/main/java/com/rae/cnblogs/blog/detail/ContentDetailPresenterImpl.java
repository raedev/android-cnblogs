package com.rae.cnblogs.blog.detail;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.ContentEntityConverter;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.basic.rx.DefaultEmptyObserver;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.AppGson;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rae on 2018/12/21.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public abstract class ContentDetailPresenterImpl extends BasicPresenter<ContentDetailContract.View> implements ContentDetailContract.Presenter {

    private final CnblogAppConfig mAppConfig;
    private final ConnectivityManager mConnectivityManager;
    @Nullable
    private IBookmarksApi mBookmarksApi; // 按需初始化


    public ContentDetailPresenterImpl(ContentDetailContract.View view) {
        super(view);
        mConnectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        mAppConfig = CnblogAppConfig.getInstance(getContext());
    }

    @Override
    protected void onStart() {
        this.loadBlogLocalStatus();
        final ContentEntity contentEntity = getView().getContentEntity(); // 内容实体
        final BlogBean blog = ContentEntityConverter.convertToBlog(contentEntity); // 博客实体
        Observable<String> observable; // 内容

        if (isWIFI()) {
            // 有网情况下从网络加载
            observable = createNetworkContentObservable();
        } else {
            // 无网情况情况加载本地
            observable = createLocalContentObservable();
        }

        // 无图模式处理
        observable = withNotImageMode(observable);

        // 内容转换成实体
        Observable<BlogBean> blogObservable = observable.map(new Function<String, BlogBean>() {
            @Override
            public BlogBean apply(String content) {
                blog.setContent(content);
                return blog;
            }
        });

        // 请求内容
        AndroidObservable.create(blogObservable).with(this).subscribe(new ApiDefaultObserver<BlogBean>() {
            @Override
            protected void onError(String message) {
                getView().onLoadDataFailed(message);
            }

            @Override
            protected void accept(BlogBean content) {
                updateLocalBlogStatus(contentEntity);
                getView().onLoadDataSuccess(content, AppGson.toJson(content));
            }
        });
    }


    /**
     * 自动判断无图模式
     */
    private Observable<String> withNotImageMode(Observable<String> observable) {
        return observable.map(new Function<String, String>() {
            @Override
            public String apply(String content) throws Exception {
                // 网络不可用、WIFI情况、 关闭智能无图模式
                if (!networkIsOk() || isWIFI() || !mAppConfig.disableBlogImage())
                    return content;

                Document document = Jsoup.parse(content);
                Elements elements = document.select("img");
                for (Element element : elements) {
                    element.attr("src", "file:///android_asset/images/placeholder.png");
                }

                return document.html();
            }
        });
    }


    /**
     * 读取本地的博客内容
     *
     * @return
     */
    private Observable<String> createLocalContentObservable() {
        return Observable
                .just(getView().getContentEntity())
                .subscribeOn(Schedulers.newThread())
                .map(new Function<ContentEntity, String>() {
                    @Override
                    public String apply(ContentEntity contentEntity) throws Exception {
                        Log.i("rae", "读取本地内容！");
                        // 根据ID和类型获取博文内容
                        String content = DbFactory.getInstance().getBlog().getBlogContent(contentEntity.getType(), contentEntity.getId());
                        if (TextUtils.isEmpty(content)) return null;
                        return content;
                    }
                });
    }

    /**
     * 读取网络的博客内容
     *
     * @return
     */
    private Observable<String> createNetworkContentObservable() {

        /*
         * 内容加载顺序
         * 1. 接口获取
         * 2. 网页获取
         * 3. 本地获取
         */
        ContentEntity contentEntity = getView().getContentEntity();

        // 接口获取
        Observable<String> apiObservable = onCreateContentObservable(contentEntity.getId());
        // 网页获取
        Observable<String> webObservable = onCreateWebContentObservable(contentEntity.getId());
        // 本地获取
        Observable<String> localObservable = createLocalContentObservable().onErrorResumeNext(Observable.<String>empty());

        // 保存到本地数据库
        apiObservable = withSaveLocalContentObservable(apiObservable).onErrorResumeNext(Observable.<String>empty());
        webObservable = withSaveLocalContentObservable(webObservable).onErrorResumeNext(Observable.<String>empty());

        // 关联顺序
        return Observable.concat(apiObservable, webObservable, localObservable).take(1);
    }


    /**
     * 内容保存到本地
     */
    private Observable<String> withSaveLocalContentObservable(Observable<String> observable) {
        return observable.map(new Function<String, String>() {
            @Override
            public String apply(String content) {
//                Log.i("rae", "保存内容: " + content.length());
                // 博文内容接口也没有返回内容
                if (TextUtils.isEmpty(content))
                    throw new NullPointerException("blog content is null!");
                updateContent(content); // 内容保存到本地数据库
                return content;
            }
        });
    }

    /**
     * 更新博客为已读状态
     *
     * @param contentEntity
     */
    private void updateLocalBlogStatus(ContentEntity contentEntity) {
        Observable.just(contentEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultEmptyObserver<ContentEntity>() {
                    @Override
                    public void onNext(ContentEntity contentEntity) {
                        DbBlog dbBlog = DbFactory.getInstance().getBlog();
                        UserBlogInfo blogInfo = getUserBlogInfo();
                        blogInfo.setRead(true);
                        blogInfo.setIsRead(true);
                        dbBlog.updateUserBlog(blogInfo);

                        BlogBean blog = dbBlog.getBlog(contentEntity.getId());
                        if (blog != null) {
                            blog.setIsRead(true);
                            blog.setUpdateTime(System.currentTimeMillis());
                            dbBlog.updateBlog(blog);
                        }
                    }
                });
    }

    @Override
    public void loadBlogLocalStatus() {
        // 用户博客信息：点赞、收藏状态
        AndroidObservable.create(Observable
                .create(new ObservableOnSubscribe<UserBlogInfo>() {
                    @Override
                    public void subscribe(ObservableEmitter<UserBlogInfo> e) {
                        UserBlogInfo blogInfo = DbFactory.getInstance().getBlog().get(getView().getContentEntity().getId());
                        if (blogInfo == null) {
                            e.onError(new NullPointerException("blog is null"));
                            return;
                        }
                        e.onNext(blogInfo);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())).with(this)
                .subscribe(new ApiDefaultObserver<UserBlogInfo>() {
                    @Override
                    protected void onError(String message) {
                    }

                    @Override
                    protected void accept(UserBlogInfo userBlogInfo) {
                        getView().onLoadUserBlogInfo(userBlogInfo);
                    }
                });
    }

    private boolean isWIFI() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private boolean networkIsOk() {
        return mConnectivityManager.getActiveNetworkInfo() != null;
    }


    @Override
    public void onLike(final boolean selected) {

        if (!UserProvider.getInstance().isLogin()) {
            getView().onNeedLogin();
            return;
        }

        ContentEntity entity = getView().getContentEntity();
        Observable<Empty> observable = onCreateLikeObservable(entity, selected);
        if (observable == null) return;
//        if (selected) {
//            observable = mBlogApi.unLikeBlog(entity.getId(), entity.getAuthorId());
//        } else {
//            observable = mBlogApi.likeBlog(entity.getId(), entity.getAuthorId());
//        }
        AndroidObservable.create(observable)
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onLoginExpired() {
                        super.onLoginExpired();
                        getView().onNeedLogin();
                    }

                    @Override
                    protected void onError(String msg) {
                        // 新闻推荐过了
                        if (msg.contains("您已经推荐过")) {
                            getView().onLikeSuccess();
                            return;
                        }
                        getView().onLikeError(msg);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        getView().onLikeSuccess();
                        // 更新数据库
                        DbBlog dbBlog = DbFactory.getInstance().getBlog();
                        UserBlogInfo blogInfo = getUserBlogInfo();
                        blogInfo.setLiked(!blogInfo.isLiked());
                        dbBlog.saveUserBlogInfo(blogInfo);
                    }
                });
    }

    @NonNull
    private UserBlogInfo getUserBlogInfo() {
        ContentEntity entity = getView().getContentEntity();
        String id = entity.getId();
        DbBlog db = DbFactory.getInstance().getBlog();
        UserBlogInfo blogInfo = db.get(id);
        if (blogInfo != null) return blogInfo;
        blogInfo = new UserBlogInfo();
        blogInfo.setBlogId(id);
        blogInfo.setBlogType(entity.getType());
        // 保存
        db.saveUserBlogInfo(blogInfo);
        UserBlogInfo dbInfo = db.get(id);
        if (dbInfo != null) return dbInfo;
        return blogInfo;
    }


    @Override
    public void onCollect(boolean selected) {
        if (!UserProvider.getInstance().isLogin()) {
            getView().onNeedLogin();
            return;
        }


        if (selected) {
            // 不支持取消收藏
            getView().onNotSupportCollecte();
            return;
        }

        // 初始化接口
        if (mBookmarksApi == null) {
            mBookmarksApi = CnblogsApiFactory.getInstance(getContext()).getBookmarksApi();
        }

        ContentEntity entity = getView().getContentEntity();
        assert mBookmarksApi != null;
        AndroidObservable.create(mBookmarksApi
                .addBookmarks(
                        entity.getTitle(),
                        entity.getSummary(),
                        entity.getUrl()))
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onLoginExpired() {
                        super.onLoginExpired();
                        getView().onNeedLogin();
                    }

                    @Override
                    protected void onError(String message) {
                        if (message.contains("网摘")) {
                            // 这种情况是正常
                            accept(Empty.value());
                            return;
                        }
                        getView().onCollectFailed(message);
                    }


                    @Override
                    protected void accept(Empty empty) {
                        getView().onCollectSuccess();
                        // 更新本地博客已经收藏
                        DbBlog dbBlog = DbFactory.getInstance().getBlog();
                        UserBlogInfo blogInfo = getUserBlogInfo();
                        blogInfo.setBookmarks(true);
                        dbBlog.saveUserBlogInfo(blogInfo);
                    }
                });
    }

    @Override
    public void onComment(String content) {

        if (!UserProvider.getInstance().isLogin()) {
            getView().onNeedLogin();
            return;
        }
        // 根据博客、新闻、知识库的类型
        ContentEntity m = getView().getContentEntity();
        Observable<Empty> observable = onCreateCommentObservable(m, content);
        if (observable == null) return;
        AndroidObservable
                .create(observable)
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onCommentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        getView().onCommentSuccess();
                    }
                });

    }

    /**
     * 异步更新博客内容
     *
     * @param content 内容
     */
    protected void updateContent(String content) {

        if (TextUtils.isEmpty(content)) return;

        // 缓存内容
        final String[] data = new String[]{
                getView().getContentEntity().getId(),
                getView().getContentEntity().getType(),
                content
        };

        Observable
                .just(data)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new DefaultObserver<String[]>() {
                    @Override
                    public void onNext(String[] value) {
                        DbFactory.getInstance()
                                .getBlog()
                                .updateBlogContent(data[0], data[1], data[2]);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 创建获取内容的网络请求
     *
     * @param id 内容id
     */
    protected abstract Observable<String> onCreateContentObservable(String id);

    /**
     * 从原文地址获取内容
     *
     * @param id 内容id
     */
    protected Observable<String> onCreateWebContentObservable(String id) {
        return Observable.empty();
    }

    /**
     * 创建点赞的网络请求
     *
     * @param entity 实体
     * @param liked  是否点赞
     * @return 返回空不请求
     */
    @Nullable
    protected abstract Observable<Empty> onCreateLikeObservable(ContentEntity entity, boolean liked);


    /**
     * 创建评论网络请求
     *
     * @param m       实体
     * @param content 内容
     */
    protected abstract Observable<Empty> onCreateCommentObservable(ContentEntity m, String content);
}
