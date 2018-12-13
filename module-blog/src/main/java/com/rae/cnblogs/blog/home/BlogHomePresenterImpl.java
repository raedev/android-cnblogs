package com.rae.cnblogs.blog.home;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;
import com.rae.cnblogs.sdk.api.ICategoryApi;
import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页处理
 * Created by ChenRui on 2016/12/2 00:25.
 */
public class BlogHomePresenterImpl extends BasicPresenter<BlogHomeContract.View> implements BlogHomeContract.Presenter {

    private ICategoryApi mCategoryApi;
    private ISearchApi mSearchApi;


    public BlogHomePresenterImpl(BlogHomeContract.View view) {
        super(view);
        CnblogsApiProvider provider = CnblogsApiFactory.getInstance(getContext());
        mCategoryApi = provider.getCategoriesApi();
        mSearchApi = provider.getSearchApi();
    }

    @Override
    protected void onStart() {
        // 加载分类
        AndroidObservable.create(mCategoryApi.getHomeCategories())
                .with(this)
                .subscribe(new ApiDefaultObserver<List<CategoryBean>>() {
                    @Override
                    protected void onError(String message) {
                        // 发生错误至少加载首页这个分类
                        List<CategoryBean> data = new ArrayList<>();

                        CategoryBean home = new CategoryBean();
                        home.setCategoryId("808");
                        home.setParentId("0");
                        home.setName("首页");
                        home.setType("SiteHome");

                        CategoryBean recommend = new CategoryBean();
                        recommend.setCategoryId("-2");
                        recommend.setParentId("0");
                        recommend.setName("推荐");
                        recommend.setType("Picked");

                        data.add(home);
                        data.add(recommend);

                        getView().onLoadCategory(data);
                    }

                    @Override
                    protected void accept(List<CategoryBean> data) {

                        CategoryBean news = new CategoryBean();
                        news.setCategoryId("0");
                        news.setParentId("0");
                        news.setName("新闻");
                        news.setType("news");

                        CategoryBean kb = new CategoryBean();
                        kb.setCategoryId("0");
                        kb.setParentId("0");
                        kb.setName("知识库");
                        kb.setType("kb");

                        data.add(2, news);
                        data.add(3, kb);

                        getView().onLoadCategory(data);
                    }
                });

        // 加载热门搜索
        AndroidObservable.create(mSearchApi.hotSearch())
                .with(this)
                .subscribe(new ApiDefaultObserver<List<String>>() {
                    @Override
                    protected void onError(String message) {

                    }

                    @Override
                    protected void accept(List<String> data) {
                        if (!Rx.isEmpty(data)) {
                            getView().onLoadHotSearchData(data.get(0));
                        }
                    }
                });
    }
}
