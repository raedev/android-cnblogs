package com.rae.cnblogs.sdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;

/**
 * JSON 类型的请求体
 * Created by ChenRui on 2017/6/3 0003 1:14.
 */
public class JsonBody extends RequestBody {

    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String CONTENT_TYPE = "Content-Type: " + MEDIA_TYPE;
    /**
     * 异步请求
     */
    public static final String XHR = "X-Requested-With:XMLHttpRequest";


    private final List<String> encodedNames;
    private final List<String> encodedValues;

    JsonBody(List<String> encodedNames, List<String> encodedValues) {
        this.encodedNames = Util.immutableList(encodedNames);
        this.encodedValues = Util.immutableList(encodedValues);
    }


    @Override
    public MediaType contentType() {
        return MediaType.parse(MEDIA_TYPE);
    }

    @Override
    public long contentLength() {
        return writeOrCountBytes(null, true);
    }

    @Override
    public void writeTo(BufferedSink sink) {
        writeOrCountBytes(sink, false);
    }

    /**
     * Either writes this request to {@code sink} or measures its content length. We have one method
     * do double-duty to make sure the counting and content are consistent, particularly when it comes
     * to awkward operations like measuring the encoded length of header strings, or the
     * length-in-digits of an encoded integer.
     */
    private long writeOrCountBytes(BufferedSink sink, boolean countBytes) {
        long byteCount = 0L;

        Buffer buffer;
        if (countBytes) {
            buffer = new Buffer();
        } else {
            buffer = sink.buffer();
        }
        JSONObject object = new JSONObject();

        for (int i = 0, size = encodedNames.size(); i < size; i++) {
            try {
                object.put(encodedNames.get(i), encodedValues.get(i));
            } catch (JSONException ignored) {
            }
        }

        buffer.writeString(object.toString(), Charset.forName("UTF-8"));

        if (countBytes) {
            byteCount = buffer.size();
            buffer.clear();
        }

        return byteCount;
    }


    public static final class Builder {
        private final List<String> names = new ArrayList<>();
        private final List<String> values = new ArrayList<>();

        public JsonBody.Builder add(String name, String value) {
            names.add(name);
            values.add(value);
//            names.add(OKHttpUrl.canonicalize(name, OKHttpUrl.FORM_ENCODE_SET, false, false, true, true));
//            values.add(OKHttpUrl.canonicalize(value, OKHttpUrl.FORM_ENCODE_SET, false, false, true, true));
            return this;
        }

//        public JsonBody.Builder addEncoded(String name, String value) {
//            names.add(OKHttpUrl.canonicalize(name, OKHttpUrl.FORM_ENCODE_SET, true, false, true, true));
//            values.add(OKHttpUrl.canonicalize(value, OKHttpUrl.FORM_ENCODE_SET, true, false, true, true));
//            return this;
//        }

        public JsonBody build() {
            return new JsonBody(names, values);
        }
    }
}
