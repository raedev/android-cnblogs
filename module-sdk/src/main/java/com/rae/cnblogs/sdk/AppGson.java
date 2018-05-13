package com.rae.cnblogs.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

/**
 * Created by ChenRui on 2017/6/12 0012 18:00.
 */
public final class AppGson {
    private static Gson sGson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        sGson = builder.create();
    }

    public static Gson get() {
        return sGson;
    }

}
