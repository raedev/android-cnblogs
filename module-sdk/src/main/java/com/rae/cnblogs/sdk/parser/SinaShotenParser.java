package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.CnblogsApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 新浪短连接
 * Created by ChenRui on 2017/10/31 0031 17:11.
 */
public class SinaShotenParser implements IJsonParser<String> {

    @Override
    public String parse(String json) throws IOException {
        json = json.trim();
        if (!json.startsWith("[")) throw new CnblogsApiException("新浪接口返回错误");
        try {
            JSONArray array = new JSONArray(json);
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject o = array.getJSONObject(i);
                return o.getString("url_short");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        throw new CnblogsApiException("新浪接口返回错误");
    }
}
