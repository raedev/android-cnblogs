package com.rae.cnblogs.blog.detail;

import android.support.annotation.Nullable;

import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.AppGson;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 博客内容详情
 * Created by rae on 2018/5/28.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class BlogDetailPresenterImpl extends BasicPresenter<ContentDetailContract.View> implements ContentDetailContract.Presenter {

    // 博客数据库
    private DbBlog mDbBlog;
    private IBlogApi mBlogApi;
    @Nullable
    private IBookmarksApi mBookmarksApi; // 按需初始化
    private INewsApi mNewsApi;

    public BlogDetailPresenterImpl(ContentDetailContract.View view) {
        super(view);
        mDbBlog = DbFactory.getInstance().getBlog();
        mBlogApi = CnblogsApiFactory.getInstance(getContext()).getBlogApi();
    }


    @Override
    public void onLike(final boolean selected) {

        if (!UserProvider.getInstance().isLogin()) {
            getView().onNeedLogin();
            return;
        }

        Observable<Empty> observable;
        ContentEntity entity = getView().getContentEntity();
        if (selected) {
            observable = mBlogApi.unLikeBlog(entity.getId(), entity.getAuthorId());
        } else {
            observable = mBlogApi.likeBlog(entity.getId(), entity.getAuthorId());
        }
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
                        UserBlogInfo blogInfo = dbBlog.get(getView().getContentEntity().getId());
                        if (blogInfo == null) return;
                        blogInfo.setIsLiked(!blogInfo.getIsLiked());
                        dbBlog.saveUserBlogInfo(blogInfo);
                    }
                });
    }

    @Override
    public void onCollect(boolean selected) {
        if (!UserProvider.getInstance().isLogin()) {
            getView().onNeedLogin();
            return;
        }

        // 初始化接口
        if (mBookmarksApi == null)
            mBookmarksApi = CnblogsApiFactory.getInstance(getContext()).getBookmarksApi();

        if (selected) {
            // 不支持取消收藏
            getView().onNotSupportCollecte();
            return;
        }

        ContentEntity entity = getView().getContentEntity();
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
                        UserBlogInfo blogInfo = dbBlog.get(getView().getContentEntity().getId());
                        if (blogInfo == null) return;
                        blogInfo.setIsBookmarks(true);
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
        BlogType type = BlogType.typeOf(m.getType());
        Observable<Empty> observable;
        if (type == BlogType.NEWS) {
            if (mNewsApi == null)
                mNewsApi = CnblogsApiFactory.getInstance(getContext()).getNewsApi();
            observable = mNewsApi.addNewsComment(m.getId(), "0", content);
            AppMobclickAgent.onClickEvent(getContext(), "NEWS_COMMENT");
        } else {
            AppMobclickAgent.onClickEvent(getContext(), "BLOG_COMMENT");
            observable = mBlogApi.addBlogComment(m.getId(), m.getAuthorId(), "0", content);
        }
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


    @Override
    protected void onStart() {
        final ContentEntity contentEntity = getView().getContentEntity();
        // 请求内容
        AndroidObservable.create(fetchContentSource())
                .with(this)
                .map(new Function<String, BlogBean>() {
                    @Override
                    public BlogBean apply(String content) {
                        // 从数据库查询，转换为博客对象
                        BlogBean data = mDbBlog.getBlog(getView().getContentEntity().getId());
                        if (data == null) {
                            // 没有查询到博客，可能没保存成功，或者缓存已经删除
                            throw new NullPointerException("本地数据库没有找到博客信息");
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
                        getView().onLoadDataSuccess(content, AppGson.toJson(content));
                    }
                });

        // 用户博客信息：点赞、收藏状态
        Observable<UserBlogInfo> observable = Observable.create(new ObservableOnSubscribe<UserBlogInfo>() {
            @Override
            public void subscribe(ObservableEmitter<UserBlogInfo> e) {
                UserBlogInfo blogInfo = mDbBlog.get(contentEntity.getId());
                if (blogInfo == null) {
                    e.onError(new NullPointerException("blog is null"));
                    return;
                }
                e.onNext(blogInfo);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        AndroidObservable.create(observable)
                .with(this)
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
                        return mDbBlog.getBlogContent(type, id);
                    }
                })
                // 返回为空默认返回空的观察者
                .onErrorResumeNext(Observable.<String>empty());

        // 数据源2： 网络数据
        Observable<String> network = mBlogApi
                .getBlogContent(id)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String content) {
                        updateContent(content);
                        return content;
                    }

                    /**
                     * 异步更新博客内容
                     * @param content 内容
                     */
                    private void updateContent(String content) {

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
                });
        return Observable.concat(local, network);
    }
}
