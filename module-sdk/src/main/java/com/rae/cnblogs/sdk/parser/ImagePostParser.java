package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.CnblogsApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 图片上传返回解析
 * Created by ChenRui on 2017/10/27 0027 17:31.
 */
public class ImagePostParser implements IJsonParser<String> {

    @Override
    public String parse(String json) throws IOException {
        try {
            JSONObject object = new JSONObject(json);
            boolean isSuccess = object.getBoolean("success");
            String url = object.getString("message");
            if (isSuccess) {
                return url;
            }
            throw new CnblogsApiException(url);
        } catch (JSONException e) {
            throw new CnblogsApiException(e);
        }
    }
}
