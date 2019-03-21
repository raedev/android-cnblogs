package com.rae.cnblogs.discover.ui;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.antcode.sdk.model.AntArticleInfo;
import com.antcode.sdk.model.AntColumnInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.basic.GlideApp;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.R2;
import com.rae.cnblogs.discover.column.detail.AntUserColumnDetailAdapter;
import com.rae.cnblogs.discover.presenter.AntUserColumnDetailPresenterImpl;
import com.rae.cnblogs.discover.presenter.IAntUserColumnDetailContract;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

@Route(path = AppRoute.PATH_DISCOVER_USER_COLUMN_DETAIL)
public class AntUserColumnDetailActivity extends SwipeBackBasicActivity implements IAntUserColumnDetailContract.View, View.OnClickListener {

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R2.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R2.id.tv_title)
    TextView mTitleView;

    AntUserColumnDetailAdapter mAdapter;

    // 头部视图
    private View mHeaderView;

    private IAntUserColumnDetailContract.Presenter mPresenter;
    @Nullable
    private AntColumnInfo mColumnInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ant_user_column_detail);
        setTitle(" ");
        mPresenter = new AntUserColumnDetailPresenterImpl(this);
        mAdapter = new AntUserColumnDetailAdapter(this,  null);
        mAdapter.setEnableLoadMore(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore();
            }
        });

        // 滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private ColorDrawable mColorDrawable;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm == null || mColumnInfo == null) return;
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

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AntArticleInfo item = mAdapter.getItem(position % adapter.getItemCount());
                if (item == null) return;
                String type = item.getType();

                // 根据类型进行跳转
                if ("URL".equalsIgnoreCase(type)) {
                    // 跳转Web
                    AppRoute.routeToAntColumnWeb(getContext(), item.getSourceUrl());
                } else if ("BLOG".equalsIgnoreCase(type)) {
                    // 跳转博客
                    AppRoute.routeToContentDetail(getContext(), item.getSourceUrl());
                } else {
                    UICompat.toastInCenter(getContext(), "不支持的跳转类型：" + type);
                }
            }
        });


        mHeaderView = View.inflate(this, R.layout.item_user_column_detail_header, null);
        mAdapter.addHeaderView(mHeaderView);
        mAdapter.setHeaderAndEmpty(true);

        mPresenter.start(); // 获取专栏详情
        mPresenter.loadData(); // 获取文章数据
    }

    // 状态栏收起状态
    public void onNavigateCollapse() {
        mToolbar.setNavigationIcon(R.drawable.icon_back_black);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(2f);
        }
    }

    // 状态展开状态
    public void onNavigateExpand() {
        mToolbar.setNavigationIcon(R.drawable.icon_back_white);
    }

    @Override
    public String getColumnId() {
        return getIntent().getStringExtra("id");
    }

    @Override
    public void onLoadColumnDetail(AntColumnInfo columnInfo) {
        onNavigateExpand();
        mColumnInfo = columnInfo;
        // 初始化头部视图
        ImageView coverImageView = mHeaderView.findViewById(R.id.img_cover);
        ImageView blurImageView = mHeaderView.findViewById(R.id.img_logo);
        TextView titleView = mHeaderView.findViewById(R.id.tv_title);
        TextView subtitleView = mHeaderView.findViewById(R.id.tv_sub_title);
        TextView articleNumView = mHeaderView.findViewById(R.id.tv_article_num);
        TextView subNumView = mHeaderView.findViewById(R.id.tv_sub_num);
        TextView articleCountView = mHeaderView.findViewById(R.id.tv_article_count);
        mHeaderView.findViewById(R.id.fl_header).setOnClickListener(this);
        mHeaderView.findViewById(R.id.btn_unsubscribe).setOnClickListener(this);
        mHeaderView.findViewById(R.id.btn_mode).setOnClickListener(this);

        AppImageLoader.display(columnInfo.getAvatar(), coverImageView);
        GlideApp.with(this)
                .load(columnInfo.getLogo())
                .apply(bitmapTransform(new BlurTransformation(25, 3)))
                .into(blurImageView);
//        String articleNumText = getString(R.string.format_article_updated, columnInfo.getArticleNum());
        String articleNumText = String.valueOf(columnInfo.getArticleNum());
        mTitleView.setText(columnInfo.getTitle());
        titleView.setText(columnInfo.getTitle());
        subtitleView.setText(columnInfo.getRecommendation());
        articleNumView.setText(articleNumText);
        articleCountView.setText(ApiUtils.formatNumber(columnInfo.getArticleCount()));
        subNumView.setText(ApiUtils.formatNumber(columnInfo.getSubnum()));

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadDataError(String message) {
        mAdapter.showEmpty(message);
    }

    @Override
    public void onNoMoreData() {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void onEmptyData(String msg) {
        mAdapter.loadMoreEnd();
        mAdapter.showEmpty(msg);
    }

    @Override
    public void onLoadData(List<AntArticleInfo> data) {
        mAdapter.replaceData(data);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void onLoginExpired() {
        AppRoute.routeToAntUserAuth(this);
        finish();
    }

    @Override
    public void onUnsubscribeSuccess() {
        UICompat.dismiss();
        UICompat.success(this, R.string.unsubscribe_success);
        finish();
    }

    @Override
    public void onUnsubscribeError(String message) {
        UICompat.dismiss();
        UICompat.failed(getContext(), message);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fl_header && mColumnInfo != null)
            AppRoute.routeToAntColumnDetail(this, mColumnInfo.getId());

        if (v.getId() == R.id.btn_mode) {
            onModeClick();
        }

        // 取消订阅
        if (v.getId() == R.id.btn_unsubscribe) {
            onUnsubscribeClick();
        }
    }

    /**
     * 模式切换
     */
    private void onModeClick() {
//        int viewType = mAdapter.getViewType();
//        viewType = viewType == 1 ? 0 : 1;
//        mAdapter.setViewType(viewType);
//        mAdapter.notifyDataSetChanged();
    }

    /**
     * 取消订阅
     */
    private void onUnsubscribeClick() {
        new DefaultDialogFragment.Builder()
                .message("取消订阅后，所有阅读数据将清空，是否真的要取消订阅？")
                .cancelText("再考虑一下")
                .cancelable(true)
                .confirmText("取消订阅")
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UICompat.loading(getContext());
                        mPresenter.unsubscribe();
                    }
                }).show(getSupportFragmentManager());
    }
}
