package com.rae.cnblogs.discover.column.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antcode.sdk.model.AntColumnInfo;
import com.antcode.sdk.model.AntIntroArticlesInfo;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.rae.cnblogs.basic.AppImageLoader;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.sdk.utils.ApiUtils;
import com.rae.cnblogs.widget.PlaceholderView;

public class AntColumnDetailAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private final PlaceholderView mPlaceholderView;
    @Nullable
    private AntColumnInfo mColumnInfo;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public AntColumnDetailAdapter(Context context) {
        super(null);
        mPlaceholderView = new PlaceholderView(context);
        setEmptyView(mPlaceholderView);
        addItemType(ColumnDetailHeaderEntity.TYPE_HEADER, R.layout.item_column_detail_header);
        addItemType(ColumnDetailSectionEntity.TYPE_SECTION, R.layout.item_column_section);
        addItemType(ColumnDetailCatalogEntity.TYPE_LEVEL_START, R.layout.item_column_category_start);
        addItemType(ColumnDetailCatalogEntity.TYPE_LEVEL_END, R.layout.item_column_category_end);
        addItemType(ColumnDetailCatalogEntity.TYPE_LEVEL_0, R.layout.item_column_category_level0);
        addItemType(ColumnDetailCatalogEntity.TYPE_LEVEL_1, R.layout.item_column_category_level1);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case ColumnDetailHeaderEntity.TYPE_HEADER:
                bindHeaderView(helper, (ColumnDetailHeaderEntity) item);
                break;
            case ColumnDetailSectionEntity.TYPE_SECTION:
                bindSectionView(helper, (ColumnDetailSectionEntity) item);
                break;
            case ColumnDetailCatalogEntity.TYPE_LEVEL_0:
                bindRootLevelView(helper, (ColumnDetailCatalogEntity) item);
                break;
            case ColumnDetailCatalogEntity.TYPE_LEVEL_1:
                bindRootLevel1View(helper, (ColumnDetailCatalogEntity) item);
                break;
            case ColumnDetailCatalogEntity.TYPE_LEVEL_END:
                bindCatalogView(helper, (ColumnDetailCatalogEntity) item);
                break;
        }
    }

    /**
     * 绑定目录视图
     */
    private void bindCatalogView(BaseViewHolder helper, ColumnDetailCatalogEntity item) {
        helper.addOnClickListener(R.id.btn_catalog);
    }

    /**
     * 绑定头部视图
     */
    private void bindHeaderView(BaseViewHolder helper, ColumnDetailHeaderEntity item) {
        AntColumnInfo columnInfo = item.getColumnInfo();
        helper.setText(R.id.tv_title, columnInfo.getTitle());
        helper.setText(R.id.tv_sub_title, columnInfo.getRecommendation());
        helper.setText(R.id.tv_article_num, ApiUtils.formatNumber(columnInfo.getArticleCount()));
        helper.setText(R.id.tv_sub_num, ApiUtils.formatNumber(columnInfo.getSubnum()));
        AppImageLoader.display(columnInfo.getLogo(), (ImageView) helper.getView(R.id.img_logo));
    }

    /**
     * 绑定块视图
     */
    private void bindSectionView(BaseViewHolder helper, ColumnDetailSectionEntity item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_desc, item.getContent());
        helper.setVisible(R.id.view_divider_top, item.isEnableTopDivider());
        helper.setVisible(R.id.view_divider_bottom, item.isEnableBottomDivider());
    }

    /**
     * 绑定一级分类
     */
    private void bindRootLevelView(final BaseViewHolder helper, final ColumnDetailCatalogEntity item) {
        helper.setText(R.id.tv_title, item.getIntroArticlesInfo().getTitle());
        final ImageView arrowView = helper.getView(R.id.img_arrow);
        arrowView.setRotation(item.isExpanded() ? 0 : 180);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = helper.getAdapterPosition();
                if (item.isExpanded()) {
                    collapse(pos);
                } else {
                    expand(pos);
                }
            }
        });
    }

    /**
     * 绑定二级分类
     */
    private void bindRootLevel1View(BaseViewHolder helper, ColumnDetailCatalogEntity item) {
        AntIntroArticlesInfo info = item.getIntroArticlesInfo();
        TextView textView = helper.getView(R.id.tv_title);
        Context context = helper.itemView.getContext();

        // 说明
        if (item.isDesc()) {
            String desc = info.getDescription();
            if (TextUtils.isEmpty(desc)) desc = info.getTitle();
            textView.setText(desc);
            textView.setTextColor(ContextCompat.getColor(context, R.color.ant_color_desc));
        } else {
            textView.setText(info.getTitle());
            textView.setTextColor(ContextCompat.getColor(context, R.color.ant_h1));
        }
    }

    public void setColumnInfo(AntColumnInfo columnInfo) {
        mColumnInfo = columnInfo;
    }

    @Nullable
    public AntColumnInfo getColumnInfo() {
        return mColumnInfo;
    }

    public void showEmpty(String message) {
        mPlaceholderView.empty(message);
    }
}
