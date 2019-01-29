package com.rae.cnblogs.discover.holder;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionEntity;

public class DiscoverItem extends SectionEntity implements MultiItemEntity {

    // 标题
    public final static int TYPE_SESSION = 0;

    // 横向排列内容
    public final static int TYPE_CONTENT_HORIZONTAL = 1;

    // 垂直排列内容
    public final static int TYPE_CONTENT_VERTICAL = 2;

    private int itemType;

    private  Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public DiscoverItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
