package com.rae.cnblogs.sdk;

import com.google.gson.reflect.TypeToken;
import com.rae.cnblogs.sdk.parser.IJsonParser;

import java.io.IOException;
import java.lang.reflect.Type;

public class DefaultJsonParser<T> implements IJsonParser<T> {

    private final Type mType;

    public DefaultJsonParser() {
        mType = new TypeToken<T>() {
        }.getType();
    }

    public DefaultJsonParser(Type type) {
        mType = type;
    }


    @Override
    public T parse(String json) throws IOException {
        return AppGson.get().fromJson(json, mType);
    }
}
