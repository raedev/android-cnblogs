package com.rae.cnblogs.discover.ui;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.antcode.sdk.AntSessionManager;
import com.antcode.sdk.model.AntColumnInfo;
import com.antcode.sdk.model.AntIntroArticlesInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.dialog.ShareDialogFragment;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.discover.column.detail.AntColumnDetailAdapter;
import com.rae.cnblogs.discover.column.detail.ColumnDetailCatalogEntity;
import com.rae.cnblogs.discover.column.detail.ColumnDetailHeaderEntity;
import com.rae.cnblogs.discover.column.detail.ColumnDetailSectionEntity;
import com.rae.cnblogs.discover.presenter.AntColumnDetailPresenterImpl;
import com.rae.cnblogs.discover.presenter.IAntColumnDetailContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 专栏详情
 */
@Route(path = AppRoute.PATH_DISCOVER_COLUMN_DETAIL)
public class AntColumnDetailActivity extends SwipeBackBasicActivity implements IAntColumnDetailContract.View {

    @BindView(R2.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R2.id.img_share)
    ImageView mShareView;
    @BindView(R2.id.tv_title)
    TextView mTitleView;
    @BindView(R2.id.btn_study)
    Button mStudyButton;
    @BindView(R2.id.btn_sub)
    TextView mSubscribeButton;

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R2.id.ll_bottom)
    ViewGroup mBottomLayout;

    private AntColumnDetailAdapter mAdapter;
    private IAntColumnDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_column_detail);
        setTitle(" ");
        mPresenter = new AntColumnDetailPresenterImpl(this);
        mAdapter = new AntColumnDetailAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private View mHeaderView;
            private ColorDrawable mColorDrawable;


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm == null || mAdapter.getColumnInfo() == null) return;
                int firstCompletelyVisibleItemPosition = lm.findFirstCompletelyVisibleItemPosition();

                if (firstCompletelyVisibleItemPosition == 0) {
                    mHeaderView = lm.getChildAt(0);
                }
                // 计算距离顶部的滑动位置
                int topOffset;
                float topPercent = 0;
                if (mHeaderView != null) {
                    topOffset = Math.abs(mHeaderView.getTop());
                    topPercent = (topOffset * 0.1f) / (mHeaderView.getHeight() * 0.1f);
                    topPercent = topPercent > 0.9 ? 1 : topPercent;
                }
                if (mColorDrawable == null) {
                    mColorDrawable = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.white));
                }

                // 处理颜色变动
                mTitleView.setAlpha(Math.min(1, topPercent * 2));
                mColorDrawable.setAlpha(Math.min(255, (int) (topPercent * 2 * 255)));
                mToolbar.setBackground(mColorDrawable);
                if (mTitleView.getAlpha() >= 0.5) {
                    // 状态栏收起状态
                    onNavigateCollapse();
                }
                if (topPercent < 0.5) {
                    // 状态展开状态
                    onNavigateExpand();
                }
            }
        });

        // 点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AntColumnInfo columnInfo = mAdapter.getColumnInfo();
                if (columnInfo == null) return;

                // 查看目录大纲图片
                if (view.getId() == R.id.btn_catalog && columnInfo != null) {
                    AppRoute.routeToImagePreview(getContext(), columnInfo.getOutlineImgOriginal());
                }

                // 点击目录标题
                if (view.getId() == R.id.tv_title) {
                    if (columnInfo.isSubscribe()) {
                        // 已订阅的话跳掉详情
                        AppRoute.routeToAntUserColumnDetail(getContext(), columnInfo.getId());
                    } else {
                        UICompat.toastInCenter(getContext(), "请先订阅专栏");
                    }
                }

            }
        });

        mPresenter.start();
    }

    // 状态栏收起状态
    public void onNavigateCollapse() {
        mToolbar.setNavigationIcon(R.drawable.icon_back_black);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(2f);
            mShareView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
        }
    }

    // 状态展开状态
    public void onNavigateExpand() {
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mShareView.setImageTintList(null);
        }
    }

    @Override
    public String getColumnId() {
        return getIntent().getStringExtra("id");
    }

    @Override
    public void onLoadColumnDetail(AntColumnInfo columnInfo) {
        onNavigateExpand();
        mBottomLayout.setVisibility(View.VISIBLE);
        // 拆分以及组装数据
        mTitleView.setText(columnInfo.getTitle());
        mAdapter.setColumnInfo(columnInfo);
        List<MultiItemEntity> data = new ArrayList<>();

        // 头部数据
        data.add(new ColumnDetailHeaderEntity(columnInfo));

        // 课程简介
        data.add(new ColumnDetailSectionEntity("课程简介", columnInfo.getIntro()));

        // 作者介绍
        if (columnInfo.getAntAuthor() != null && !TextUtils.isEmpty(columnInfo.getAntAuthor().getIntro())) {
            data.add(new ColumnDetailSectionEntity("作者简介", columnInfo.getAntAuthor().getIntro()));
        }

        // 适合人群
        if (!TextUtils.isEmpty(columnInfo.getConsumer())) {
            data.add(new ColumnDetailSectionEntity("适合人群", columnInfo.getConsumer()));
        }


        // -----------------   目录解析 开始 --------------------- //

        // 添加目录头部
        data.add(new ColumnDetailCatalogEntity(0, ColumnDetailCatalogEntity.TYPE_LEVEL_START, null));
        List<AntIntroArticlesInfo> articles = columnInfo.getArticlesInfoList();
        HashMap<String, ColumnDetailCatalogEntity> mRootCatalogMap = new HashMap<>();
        // 一级目录
        for (AntIntroArticlesInfo article : articles) {
            String parentId = article.getParentId();
            if (TextUtils.isEmpty(parentId)) {
                ColumnDetailCatalogEntity levelEntity = new ColumnDetailCatalogEntity(0, ColumnDetailCatalogEntity.TYPE_LEVEL_0, article);

                // 加多一条目录说明
                ColumnDetailCatalogEntity descItem = new ColumnDetailCatalogEntity(1, ColumnDetailCatalogEntity.TYPE_LEVEL_1, article);
                descItem.setDesc(true);
                levelEntity.addSubItem(descItem);

                mRootCatalogMap.put(String.valueOf(article.getId()), levelEntity);
                data.add(levelEntity);
            }
        }
        // 二级目录
        for (AntIntroArticlesInfo article : articles) {
            String parentId = article.getParentId();
            if (TextUtils.isEmpty(parentId)) continue;
//             找到一级目录，然后添加到子列表中
            ColumnDetailCatalogEntity entity = mRootCatalogMap.get(parentId);
            if (entity == null) continue;
            ColumnDetailCatalogEntity levelEntity = new ColumnDetailCatalogEntity(1, ColumnDetailCatalogEntity.TYPE_LEVEL_1, article);
            entity.addSubItem(levelEntity);
        }
//        if (!TextUtils.isEmpty(columnInfo.getOutlineImgOriginal())) {
        data.add(new ColumnDetailCatalogEntity(0, ColumnDetailCatalogEntity.TYPE_LEVEL_END, null));
//        }

        // -----------------   目录解析 结束 --------------------- //


        // 订阅须知
        ColumnDetailSectionEntity subEntity = new ColumnDetailSectionEntity("订阅须知", columnInfo.getNotice());
        subEntity.setEnableTopDivider(true);
        data.add(subEntity);
        mAdapter.setNewData(data);

        // 默认展开
        int expandCount = 0;
        for (int i = mAdapter.getHeaderLayoutCount(); i < data.size() - 1 + mAdapter.getHeaderLayoutCount(); i++) {
            int itemViewType = mAdapter.getItemViewType(i);
            if (itemViewType == ColumnDetailCatalogEntity.TYPE_LEVEL_0) {
                if (expandCount > 1) break;
                mAdapter.expandAll(i, false, false);
                expandCount++;
            }
        }
    }

    @Override
    public void onLoadDataError(String message) {
        onNavigateCollapse();
        mAdapter.showEmpty(message);
        mShareView.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSubscribeError(String message) {
        mSubscribeButton.setText(R.string.subscribe_now);
        mSubscribeButton.setEnabled(true);
        UICompat.failed(this, message);
    }

    @Override
    public void onSubscribeSuccess() {
        AntColumnInfo columnInfo = mAdapter.getColumnInfo();
        if (columnInfo != null) {
            columnInfo.setSubscribe(true);
        }
        UICompat.toast(this, getString(R.string.subscribe_success));
        UICompat.scaleIn(mStudyButton);
    }

    @Override
    public void onColumnSubscribe(boolean subscribe) {
        UICompat.setVisibility(mSubscribeButton, !subscribe);
        UICompat.setVisibility(mStudyButton, subscribe);
    }

    @Override
    public void onLoginExpired() {
        AppRoute.routeToAntUserAuth(this);
    }

    @OnClick(R2.id.btn_sub)
    public void onSubscribeClick() {
        if (!AntSessionManager.getDefault().isLogin()) {
            AppRoute.routeToAntUserAuth(this);
            return;
        }

        mSubscribeButton.setText("订阅中...");
        mSubscribeButton.setEnabled(false);

        mPresenter.subscribe();

    }

    @OnClick(R2.id.btn_study)
    public void onStudyClick() {
        if (mAdapter.getColumnInfo() == null) {
            UICompat.failed(this, "数据尚未准备好");
            return;
        }
        AppRoute.routeToAntUserColumnDetail(this, mAdapter.getColumnInfo().getId());
        finish();
    }

    @OnClick(R2.id.img_share)
    public void onShareClick() {
        if (mAdapter == null || mAdapter.getColumnInfo() == null) return;
        AntColumnInfo columnInfo = mAdapter.getColumnInfo();
        ShareDialogFragment.newInstance("http://www.baidu.com", columnInfo.getTitle(), columnInfo.getRecommendation(), columnInfo.getAvatar(), false).show(getSupportFragmentManager(), "share");
    }
}
