package com.rae.cnblogs.discover.column.detail;

import com.antcode.sdk.model.AntIntroArticlesInfo;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ColumnDetailCatalogEntity extends AbstractExpandableItem<ColumnDetailCatalogEntity> implements MultiItemEntity {


    // 一级目录
    public final static int TYPE_LEVEL_START = 0;

    public final static int TYPE_LEVEL_END = 1;

    public final static int TYPE_LEVEL_0 = 3;
    // 二级目录
    public final static int TYPE_LEVEL_1 = 4;
    // 详情
    public final static int TYPE_DESC = 5;


    private AntIntroArticlesInfo mIntroArticlesInfo;

    private int level;
    private int itemType;
    private boolean isDesc;


    public ColumnDetailCatalogEntity(int level, int itemType, AntIntroArticlesInfo introArticlesInfo) {
        this.level = level;
        this.itemType = itemType;
        this.mIntroArticlesInfo = introArticlesInfo;
    }

    public AntIntroArticlesInfo getIntroArticlesInfo() {
        return mIntroArticlesInfo;
    }

    public boolean isDesc() {
        return isDesc;
    }

    public void setDesc(boolean desc) {
        isDesc = desc;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
