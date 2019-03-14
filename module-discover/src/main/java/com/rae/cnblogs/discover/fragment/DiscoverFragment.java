package com.rae.cnblogs.discover.fragment;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.antcode.sdk.model.AntAdInfo;
import com.antcode.sdk.model.AntColumnInfo;
import com.antcode.sdk.model.AntTabInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.discover.BannerImageLoader;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.discover.holder.DiscoverItem;
import com.rae.cnblogs.discover.home.DiscoverHomeAdapter;
import com.rae.cnblogs.discover.home.DiscoverHomePresenterImpl;
import com.rae.cnblogs.discover.home.IDiscoverHomeContract;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 发现
 * Created by ChenRui on 2018/6/13 10:22.
 */
@Route(path = AppRoute.PATH_FRAGMENT_DISCOVER)
public class DiscoverFragment extends BasicFragment implements IDiscoverHomeContract.View {

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R2.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    IDiscoverHomeContract.Presenter mPresenter;
    private DiscoverHomeAdapter mAdapter;
    private Banner mBanner;
    private ViewGroup mTabLayout;
    private List<AntAdInfo> mAdInfoList;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_discover;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DiscoverHomePresenterImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // init views
        mAdapter = new DiscoverHomeAdapter();
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

            }
        }, mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.start();
            }
        });
        initViews();
        mPresenter.start();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DiscoverItem item = mAdapter.getItem(position);
                if (item == null) return;
                int viewType = item.getItemType();
                // 查看更多
                if (viewType == DiscoverItem.TYPE_SESSION) {
                    AppRoute.routeToAntColumn(view.getContext(), 1);
                }
                Object data = item.getData();
                if (data instanceof AntColumnInfo) {
                    AppRoute.routeToAntColumnDetail(view.getContext(), ((AntColumnInfo) data).getId());
                }
            }
        });
    }

    private void initViews() {
        // 初始化分类Tab
        mTabLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.view_discover_category, (ViewGroup) getView(), false);
        mAdapter.addHeaderView(mTabLayout);

        // 初始化头部Banner
        mBanner = (Banner) getLayoutInflater().inflate(R.layout.view_discover_banner, (ViewGroup) getView(), false);
        mBanner.setImageLoader(new BannerImageLoader());
        mBanner.setIndicatorGravity(Gravity.CENTER);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                AntAdInfo adInfo = mAdInfoList.get(position % mAdInfoList.size());
                AppRoute.autoRoute(getContext(), adInfo.getType(), adInfo.getUrl(), adInfo.getData());
            }
        });

        ViewPager viewPager = mBanner.findViewById(R.id.bannerViewPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -35, getResources().getDisplayMetrics()));
        mAdapter.addHeaderView(mBanner, 0);


    }

    @Override
    public void onLoadAds(List<AntAdInfo> ads) {
        mAdInfoList = ads;
        mRefreshLayout.setRefreshing(false);
        mBanner.setImages(ads).start();
    }

    @Override
    public void onLoadColumns(List<AntColumnInfo> columns) {
        // load columns
        mRefreshLayout.setRefreshing(false);

        List<DiscoverItem> data = new ArrayList<>();
        data.add(new DiscoverItem(true, "精选专栏"));

        for (AntColumnInfo column : columns) {
            DiscoverItem item = new DiscoverItem(false, null);
            item.setData(column);
            item.setItemType(DiscoverItem.TYPE_CONTENT_VERTICAL);
            data.add(item);
        }

        mAdapter.replaceData(data);
        mAdapter.loadMoreEnd();
    }

    @Override
    public void onLoadCategories(List<AntTabInfo> homeTabs) {
        mTabLayout.removeAllViews();
        for (AntTabInfo homeTab : homeTabs) {
            View view = getLayoutInflater().inflate(R.layout.item_discover_home_tab, mTabLayout, false);
            TextView textView = view.findViewById(R.id.tv_title);
            ImageView logo = view.findViewById(R.id.img_logo);
            textView.setText(homeTab.getName());
            String iconUrl = homeTab.getIcon();
            if (iconUrl.startsWith("res")) {
                try {
                    iconUrl = iconUrl.replace("res/", "");
                    logo.setImageResource(getResources().getIdentifier(iconUrl, "drawable", getContext().getPackageName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AppImageLoader.display(iconUrl, logo);
            }
            view.setOnClickListener(new TabClickListener(homeTab));
            mTabLayout.addView(view);
        }
    }

    @Override
    public void onLoadColumnError(String message) {
        mRefreshLayout.setRefreshing(false);
        UICompat.failed(getContext(), message);
    }

    @Override
    public void onLoadAdError(String message) {
        // 为空
        Resources resources = getResources();
        int resId = R.drawable.default_placeholder_normal;
        String imageUrl = TextUtils.concat(ContentResolver.SCHEME_ANDROID_RESOURCE, "://",
                resources.getResourcePackageName(resId), "/",
                resources.getResourceTypeName(resId), "/",
                resources.getResourceName(resId))
                .toString();

        mAdInfoList = new ArrayList<>();
        AntAdInfo item = new AntAdInfo();
        item.setImageUrl(imageUrl);
        mAdInfoList.add(item);
        mBanner.setImages(mAdInfoList).start();
    }

    class TabClickListener implements View.OnClickListener {

        private final AntTabInfo mHomeTab;

        TabClickListener(AntTabInfo homeTab) {
            mHomeTab = homeTab;
        }

        @Override
        public void onClick(View v) {
            AppRoute.autoRoute(v.getContext(), null, mHomeTab.getPath(), null);
        }
    }
}
