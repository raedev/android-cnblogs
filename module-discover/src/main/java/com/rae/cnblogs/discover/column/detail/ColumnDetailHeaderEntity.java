package com.rae.cnblogs.discover.column.detail;

import com.antcode.sdk.model.AntColumnInfo;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ColumnDetailHeaderEntity implements MultiItemEntity {

    // 头部
    final static int TYPE_HEADER = 11;

    private AntColumnInfo mColumnInfo;

    @Override
    public int getItemType() {
        return TYPE_HEADER;
    }

    public ColumnDetailHeaderEntity(AntColumnInfo columnInfo) {
        mColumnInfo = columnInfo;
    }

    public AntColumnInfo getColumnInfo() {
        return mColumnInfo;
    }
}
