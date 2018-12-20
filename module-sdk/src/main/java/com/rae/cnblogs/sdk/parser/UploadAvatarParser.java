package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.CnblogsApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 头像图片上传返回解析
 * Created by ChenRui on 2017/10/27 0027 17:31.
 */
public class UploadAvatarParser implements IJsonParser<String> {

    @Override
    public String parse(String json) throws IOException {
        try {
            JSONObject object = new JSONObject(json);

            // 更新头像返回的数据
            if (object.has("IsSuccess")) {
                if (object.getBoolean("IsSuccess")) {
                    return object.getString("AvatarSrc");
                }
                throw new CnblogsApiException(object.getString("Message"));
            }

            //  上传头像返回的数据
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
