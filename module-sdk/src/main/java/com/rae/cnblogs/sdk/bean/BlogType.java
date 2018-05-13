package com.rae.cnblogs.sdk.bean;

import android.text.TextUtils;

/**
 * 博客类型
 * Created by ChenRui on 2017/2/1 17:57.
 */
public enum BlogType {
    /**
     * 博客
     */
    BLOG("BLOG"),

    /**
     * 新闻
     */
    NEWS("NEWS"),


    /**
     * 博主
     */
    BLOGGER("BLOGGER"),

    /**
     * 知识库
     */
    KB("KB"),
    UNKNOWN("UNKNOWN");

    private String typeName;

    BlogType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }


    public static BlogType typeOf(String type) {
        if (TextUtils.isEmpty(type)) return BlogType.UNKNOWN;
        BlogType[] values = BlogType.values();
        for (BlogType value : values) {
            if (TextUtils.equals(type, value.getTypeName())) {
                return value;
            }
        }
        return BlogType.UNKNOWN;
    }
}
