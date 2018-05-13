package com.rae.cnblogs.sdk.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.rae.cnblogs.sdk.ApiErrorCode;
import com.rae.cnblogs.sdk.ApiOptions;
import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonParser;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.parser.IHtmlParser;
import com.rae.cnblogs.sdk.parser.IJsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 文本类型的响应解析器
 * Created by ChenRui on 2017/5/25 0025 23:46.
 */
public class TextResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Type type;
    private final TypeAdapter<T> mAdapter;
    private final Gson mGson;
    private IJsonParser<T> mJsonParser;
    private IHtmlParser<T> mHtmlParser;
    private ApiOptions mApiOptions;

    @SuppressWarnings("unchecked")
    public TextResponseBodyConverter(Type type, Annotation[] annotations, Gson gson, TypeAdapter<T> adapter) {
        this.type = type;
        mAdapter = adapter;
        mGson = gson;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Parser) {
                Class<?> cls = ((Parser) annotation).value();
                try {
                    mHtmlParser = (IHtmlParser<T>) cls.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            if (annotation instanceof JsonParser) {
                Class<?> cls = ((JsonParser) annotation).value();
                try {
                    mJsonParser = (IJsonParser<T>) cls.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (annotation instanceof ApiOptions) {
                mApiOptions = (ApiOptions) annotation;
            }
        }
    }

    /**
     * 接口是否忽略登录
     */
    private boolean ignoreLogin() {
        return mApiOptions != null && mApiOptions.ignoreLogin();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T convert(ResponseBody value) throws IOException {
        // 解析
        String text = value.string();
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        text = text.trim();
        if (text.startsWith("{") || text.startsWith("[")) {
            // 解析JSON
            return json2Entity(text);
        }
        // 如果没有解析器，直接返回响应文本。
        else if (type == String.class && mHtmlParser == null && mJsonParser == null) {
            return (T) text;
        }
        return html2Entity(text);
    }

    /**
     * HTML convert to entity
     */
    @SuppressWarnings("unchecked")
    private T html2Entity(String text) throws IOException {
        if (TextUtils.equals(text, "true") && type == Empty.class) {
            return (T) Empty.value();
        }
        if (TextUtils.equals(text, "false") && type == Empty.class) {
            return (T) Empty.value();
        }

        Document document = Jsoup.parse(text);

        if (document.title().contains("用户登录") && !ignoreLogin()) {
            throw new CnblogsApiException(ApiErrorCode.LOGIN_EXPIRED, "登录失效，请重新登录");
        }

        if (mHtmlParser == null) {
            throw new CnblogsApiException("Html数据解析器为空");
        }

        return mHtmlParser.parse(document, text);
    }

    /**
     * JSON String convert to entity
     */
    @SuppressWarnings("unchecked")
    private T json2Entity(String text) throws IOException {

        // 交给自定义的JSON解析
        if (mJsonParser != null) {
            return mJsonParser.parse(text);
        }


        // 删除评论的时候会返回true or false
        if (TextUtils.equals(text, "true") && type == Empty.class) {
            return (T) Empty.value();
        }

        if (TextUtils.equals(text, "false") && type == Empty.class) {
            throw new CnblogsApiException("删除评论失败");
        }

        if (text.contains("用户登录") && !ignoreLogin()) {
            // 上报登录失效信息
            CnblogsApiException ex = new CnblogsApiException(ApiErrorCode.LOGIN_EXPIRED, "登录失效，请重新登录");
            ex.setResponse(text);
            throw ex;
        }

        try {
            JSONObject obj = new JSONObject(text);
            boolean isSuccess = parseSuccess(obj);
            String message = parseMessage(obj);
            Object data = parseData(obj);

            if (isSuccess && !obj.isNull("data") && data != null) {
                text = data.toString();
                JsonReader jsonReader = mGson.newJsonReader(new StringReader(text));
                return mAdapter.read(jsonReader);
            } else if (isSuccess && type == Void.class) {
                return null;
            } else if (isSuccess && type == Empty.class) {
                return (T) Empty.value();
            } else if (isSuccess && obj.isNull("data")) {
                throw new CnblogsApiException("数据为空");
            } else {
                message = TextUtils.isEmpty(message) ? "未知错误" : Jsoup.parse(message).text();
                throw new CnblogsApiException(message);
            }
        } catch (JSONException e) {
            throw new CnblogsApiException(e);
        }
    }

    /**
     * 解析是否成功标志字段
     */
    private boolean parseSuccess(JSONObject obj) throws JSONException {
        if (obj.has("IsSuccess")) {
            return obj.getBoolean("IsSuccess");
        } else if (obj.has("IsSucceed")) {
            return obj.getBoolean("IsSucceed");
        } else if (obj.has("success")) {
            return obj.getBoolean("success");
        } else if (obj.has("isSuccess")) {
            return obj.getBoolean("isSuccess"); // 发布闪存返回的字段
        }
        return false;
    }

    /**
     * 解析错误消息字段
     */
    private String parseMessage(JSONObject obj) throws JSONException {
        if (obj.has("Message")) {
            return obj.getString("Message");
        } else if (obj.has("message")) {
            return obj.getString("message");
        } else if (obj.has("responseText")) {
            return obj.getString("responseText"); // 发布闪存返回的字段
        }
        return null;
    }

    /**
     * 解析DATA字段
     */
    private Object parseData(JSONObject obj) throws JSONException {
        if (obj.has("Data")) {
            return obj.get("Data");
        } else if (obj.has("data")) {
            return obj.get("data");
        }
        return null;
    }
}
