package com.rae.cnblogs.blog.content;

import android.support.annotation.Nullable;
import android.util.Log;

import com.rae.cnblogs.ContentEntityConverter;
import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.basic.IPageView;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.blog.comm.ContentListContract;
import com.rae.cnblogs.blog.job.JobEvent;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 博客列表
 * Created by rae on 2018/5/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public abstract class BasicBlogPresenterImpl extends BasicPresenter<ContentListContract.View> implements ContentListContract.Presenter, IPageView<BlogBean> {

    private final BlogType mBlogType;
    // 维护数据源
    private final List<ContentEntity> mDataList = new ArrayList<>();
    private DbBlog mDbBlog;
    private PageObservable<BlogBean> mPageObservable;

    /**
     * @param blogType 博客类型
     */
    public BasicBlogPresenterImpl(ContentListContract.View view, BlogType blogType) {
        super(view);
        mDbBlog = DbFactory.getInstance().getBlog();
        mBlogType = blogType;
        // 创建分页对象
        mPageObservable = new PageObservable<BlogBean>(this, this) {
            /**
             * 需要清除数据源的时候触发清除关联的数据
             */
            @Override
            protected void onClearData() {
                super.onClearData();
                mDataList.clear();
            }

            @Override
            protected Observable<List<BlogBean>> onCreateObserver(int page) {
                CategoryBean category = getView().getCategory();
                // 获取博客列表
                return BasicBlogPresenterImpl.this.onCreateObserver(category, page);
            }

            @Override
            protected void onLoadDataComplete(List<BlogBean> dataList) {
                super.onLoadDataComplete(dataList);
                // 通知异步下载博文内容
                EventBus.getDefault().post(new JobEvent(JobEvent.ACTION_JOB_BLOG_CONTENT));
            }
        };

    }

    /**
     * 博客对象转换为内容实体
     *
     * @param data 博客列表
     */
    protected List<ContentEntity> convertEntity(List<BlogBean> data) {
        for (BlogBean b : data) {
            b.setBlogType(mBlogType.getTypeName());
            mDataList.add(ContentEntityConverter.convert(b));
        }
        return mDataList;
    }

    /**
     * 加载本地数据
     */
    protected void loadLocalData(int page) {
        // 加载离线数据
        AndroidObservable.create(Observable.just(page))
                .map(new Function<Integer, List<BlogBean>>() {
                    @Override
                    public List<BlogBean> apply(Integer page) throws IOException {
                        CategoryBean category = getView().getCategory();
                        if (category == null || category.getCategoryId() == null) {
                            throw new NullPointerException("暂无记录");
                        }
                        return mDbBlog.getList(category.getCategoryId(), page, mBlogType);
                    }
                })
                .subscribe(new DisposableObserver<List<BlogBean>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onEmptyData(e.getMessage());
                    }

                    @Override
                    public void onNext(List<BlogBean> data) {
                        getView().onLoadData(convertEntity(data));
                    }
                });
    }

    @Override
    public void loadMore() {
        // 加载更多数据
        mPageObservable.loadMore();
    }


    /**
     * 创建业务请求对象
     *
     * @param category 分类
     * @param page     分页
     */
    protected abstract Observable<List<BlogBean>> onCreateObserver(@Nullable CategoryBean category, int page);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataList.clear();
        if (mPageObservable != null) {
            mPageObservable.destroy();
            mPageObservable = null;
        }
    }

    @Override
    public void onEmptyData(String msg) {
        onNoMoreData();
    }

    @Override
    public void onLoadData(List<BlogBean> data) {
        // 转换对象
        getView().onLoadData(convertEntity(data));
        // 保存本地数据库
        saveLocalData(data);
    }

    /**
     * 保存本地数据库
     */
    private void saveLocalData(List<BlogBean> data) {
        CategoryBean category = getView().getCategory();
        String id;

        // 如果分类为空，默认等于博客类型
        if (category == null) {
            id = mBlogType.getTypeName();
        } else {
            id = category.getCategoryId();
        }

        for (BlogBean m : data) {
            m.setCategoryId(id);
        }

        // 异步线程入库
        Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new DefaultObserver<List<BlogBean>>() {
                    @Override
                    public void onNext(List<BlogBean> blogBeans) {
                        // 入库
                        DbFactory.getInstance().getBlog().addAll(blogBeans);
                        // 入库完成后，清除数据
                        blogBeans.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("rae", "save blog data failed!", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onLoginExpired() {
        // 不用实现
    }

    @Override
    public void onNoMoreData() {
        // 查询本地缓存数据
        loadLocalData(mPageObservable.getCurrentPage());
    }

    @Override
    protected void onStart() {
        mPageObservable.start();
    }

    public BlogType getBlogType() {
        return mBlogType;
    }
}
