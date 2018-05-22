package com.rae.cnblogs.basic;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by rae on 2018/5/16.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public final class Rx {

    public static <T> boolean isEmpty(List<T> data) {
        return data == null || data.size() <= 0;
    }

    public static <T> int getCount(List<T> data) {
        return data == null ? 0 : data.size();
    }

    public static int parseInt(CharSequence text) {
        return parseInt(text, 0);
    }

    public static int parseInt(CharSequence text, int defaultValue) {
        if (TextUtils.isEmpty(text)) return defaultValue;
        try {
            return Integer.parseInt(text.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
