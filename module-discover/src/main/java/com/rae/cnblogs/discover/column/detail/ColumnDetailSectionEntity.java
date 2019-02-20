package com.rae.cnblogs.discover.column.detail;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ColumnDetailSectionEntity implements MultiItemEntity {

    // 区块
    final static int TYPE_SECTION = 12;


    private String title;

    private String content;

    public String getTitle() {
        return title;
    }


    public String getContent() {
        return content;
    }


    public ColumnDetailSectionEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public int getItemType() {
        return TYPE_SECTION;
    }
}
