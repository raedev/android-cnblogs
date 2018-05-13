package com.rae.cnblogs.sdk;

/**
 * 空数据
 * Created by ChenRui on 2017/5/24 0024 23:26.
 */
public final class Empty {

    public static Empty value() {
        return new Empty();
    }

    private Empty() {
    }
}
