package com.rae.cnblogs.sdk.api.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rae.cnblogs.sdk.AppGson;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.ICategoryApi;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.db.DbCategory;
import com.rae.cnblogs.sdk.db.DbFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 博客分类
 * Created by ChenRui on 2016/11/30 0030 17:36.
 */
public class CategoryApiImpl implements ICategoryApi {

    private final Context mContext;
    private final Gson mGson;

    public CategoryApiImpl(Context context) {
        this.mContext = context;
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        mGson = AppGson.get();
    }

    private List<CategoryBean> getFromAssets() {
        try {
            String json = readString().replace("\r\n", "");
            return mGson.fromJson(json, new TypeToken<List<CategoryBean>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readString() {
        try {
            if (mContext == null) return "";
            InputStream stream = mContext.getAssets().open("category.json");
            BufferedInputStream bis = new BufferedInputStream(stream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[128];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            String result = outputStream.toString();
            outputStream.close();
            bis.close();
            stream.close();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public Observable<List<CategoryBean>> getCategories() {
        // 从数据库中获取
        DbCategory db = DbFactory.getInstance().getCategory();
        List<CategoryBean> list = db.list();

        // 没有数据,开始初始化数据
        if (list == null || list.isEmpty()) {
            list = getFromAssets();
            db.reset(list);
        }

        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }

        return Observable.just(list);
    }

    @Override
    public Observable<List<CategoryBean>> getHomeCategories() {
        return getCategories().map(new Function<List<CategoryBean>, List<CategoryBean>>() {
            @Override
            public List<CategoryBean> apply(List<CategoryBean> categoryBeans) {
                List<CategoryBean> result = new ArrayList<>();
                for (CategoryBean item : categoryBeans) {
                    if (!item.isHide()) {
                        result.add(item);
                    }
                }

                categoryBeans.clear();
                return result;
            }
        });
    }

    @Override
    public Observable<Empty> updateCategories(List<CategoryBean> categoryList) {
        DbCategory db = DbFactory.getInstance().getCategory();
        db.reset(categoryList); //重置数据
        return Observable.just(Empty.value());
    }
}
