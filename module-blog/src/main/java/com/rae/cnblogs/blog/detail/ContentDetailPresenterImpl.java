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
        final ContentEntity contentEntity = getView().getContentEntity();
        // 请求内容
        AndroidObservable.create(fetchContentSource())
                .with(this)
                // 离线模式下处理图片
                .map(new Function<String, String>() {
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
                })
                .map(new Function<String, BlogBean>() {
                    @Override
                    public BlogBean apply(String content) {
                        // 从数据库查询，转换为博客对象
                        BlogBean data = DbFactory.getInstance().getBlog().getBlog(contentEntity.getId());
                        if (data == null) {
                            // 没有查询到博客，可能没保存成功，或者缓存已经删除
                            data = ContentEntityConverter.convertToBlog(contentEntity);
                        }
                        data.setContent(content); // 设置博客内容
                        return data;
                    }
                })
                // 没有找到博客的时候返回
                .subscribe(new ApiDefaultObserver<BlogBean>() {
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

        this.loadBlogLocalStatus();
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
     * 请求数据源
     */
    private Observable<String> fetchContentSource() {

        String id = getView().getContentEntity().getId();

        // 数据源1： 本地数据库
        Observable<String> local = Observable
                .just(id)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String id) {
                        // 根据ID和类型获取博文内容
                        String type = getView().getContentEntity().getType();
                        String content = DbFactory.getInstance().getBlog().getBlogContent(type, id);
                        if (TextUtils.isEmpty(content)) return null;
                        Log.i("rae","读取本地内容！");
                        return content;
                    }
                })
                // 返回为空默认返回空的观察者，执行下个观察者
                .onErrorResumeNext(Observable.<String>empty());

        // 数据源2： 网络数据
        // 根据不同的博客类型：博客、新闻、知识库来确定网络数据提供者
        Observable<String> networkObservable = onCreateContentObservable(id);

        Observable<String> network = networkObservable.map(new Function<String, String>() {
            @Override
            public String apply(String content) {
                // 博文内容接口也没有返回内容
                if (TextUtils.isEmpty(content))
                    return null;
                updateContent(content); // 博客内容写入本地数据库
                Log.i("rae","读取网络内容！");
                return content;
            }

        }).onErrorResumeNext(Observable.<String>empty());


        return onFetchContentSource(local, network);
    }


    protected Observable<String> onFetchContentSource(Observable<String> local, Observable<String> network) {
        return Observable.concat(local, network).take(1);
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
