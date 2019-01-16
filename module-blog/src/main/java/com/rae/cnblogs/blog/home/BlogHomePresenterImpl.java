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
import java.util.Collections;
import java.util.Comparator;
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
                        reorganizeData(data);
                        getView().onLoadCategory(data);
                    }


                    @Override
                    protected void accept(List<CategoryBean> data) {
                        reorganizeData(data);
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

    /**
     * 重新整理数据，添加默认的分类
     */
    @Override
    public void reorganizeData(List<CategoryBean> data) {
        boolean hasRecommented = false, hasHome = false, hasNews = false, hasKb = false;
        for (CategoryBean item : data) {
            String name = item.getName();
            if ("首页".equals(name)) {
                hasHome = true;
                item.setOrderNo(-4);
            }
            if ("推荐".equals(name)) {
                hasRecommented = true;
                item.setOrderNo(-3);
            }
        }

        if (!hasRecommented) {
            CategoryBean recommend = new CategoryBean();
            recommend.setCategoryId("-2");
            recommend.setParentId("0");
            recommend.setName("推荐");
            recommend.setType("Picked");
            recommend.setOrderNo(-3);
            data.add(recommend);
        }
        if (!hasHome) {
            CategoryBean home = new CategoryBean();
            home.setCategoryId("808");
            home.setParentId("0");
            home.setName("首页");
            home.setType("SiteHome");
            home.setOrderNo(-4);
            data.add(home);
        }

        // 重新排序
        Collections.sort(data, new Comparator<CategoryBean>() {
            @Override
            public int compare(CategoryBean a, CategoryBean b) {
                if (a.getOrderNo() == b.getOrderNo()) return 0;
                return a.getOrderNo() > b.getOrderNo() ? 1 : -1;
            }
        });
    }
}
