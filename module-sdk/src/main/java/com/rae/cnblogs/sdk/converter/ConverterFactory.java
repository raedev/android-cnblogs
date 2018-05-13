package com.rae.cnblogs.sdk.converter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 博客园返回的数据的转换成实体类工厂
 * Created by ChenRui on 2017/5/22 0022 0:03.
 */
public class ConverterFactory extends Converter.Factory {
    private final Gson gson;

    @NonNull
    public static ConverterFactory create() {
        return new ConverterFactory();
    }

    private ConverterFactory() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        gson = builder.create();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new TextResponseBodyConverter<>(type, annotations, gson, adapter);
    }
}
