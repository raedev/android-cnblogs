package com.rae.cnblogs.sdk.parser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 百度搜索建议解析
 * Created by ChenRui on 2017/2/8 0008 9:30.
 */
public class BaiduSuggestionParser implements IHtmlParser<List<String>> {


    @Override
    public List<String> parse(Document document, String json) {
        try {
            json = json.replace("cnblogs(", "").replace(");", "");
            JSONObject object = new JSONObject(json);
            if (object.has("s")) {
                JSONArray array = object.getJSONArray("s");

                List<String> result = new ArrayList<>();
                int size = array.length();
                for (int i = 0; i < size; i++) {
                    result.add(array.get(i).toString());
                }

                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
