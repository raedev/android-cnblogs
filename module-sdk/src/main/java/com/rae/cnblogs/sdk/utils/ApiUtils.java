package com.rae.cnblogs.sdk.utils;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.sdk.bean.BlogCommentBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;

/**
 * 工具类
 * Created by ChenRui on 2016/12/13 23:20.
 */
public final class ApiUtils {

    public static String getUrl(String text) {
        if (text == null || text.isEmpty()) return text;
        if (text.startsWith("//")) {
            return text.replace("//", "http://");
        }
        return text;
    }

    public static String getDate(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }

        text = text.replace("T", " ").replace("Z", "");

        String regx = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";
        Matcher matcher = Pattern.compile(regx).matcher(text);
        if (!matcher.find()) {
            return text;
        }

        text = matcher.group();
        String time = text.split(" ")[1];

        // 时间间隔
        long span = (System.currentTimeMillis() - parseDate(text).getTime()) / 1000;

        // 今天过去的时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long today = (System.currentTimeMillis() - calendar.getTimeInMillis()) / 1000;
        if (span < 0) {
        } else if (span < 60) {
            text = "刚刚";
        } else if (span < 3600) {
            text = (span / 60) + "分钟前";
        } else if (span < today) {
            text = "今天 " + time;
        } else if (span < today + 86400) {
            text = "昨天 " + time;
        } else if (span < today + 2 * 86400) {
            text = "前天 " + time;
        }


        return text;
    }

    public static Date parseDate(String text) {
        return parseDate(text, "yyyy-MM-dd HH:mm");
    }

    public static Date parseDefaultDate(String text) {
        return parseDate(text, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate(String text, String pattern) {
        if (TextUtils.isEmpty(text)) {
            return new Date();
        }
        Date target;
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            target = format.parse(text);// RaeDateUtil.parseCommentInList(text, "yyyy-MM-dd HH:mm");
        } catch (Exception e) {
            Log.e("rae", "解析出错!", e);
            target = new Date();
        }
        return target;
    }


    public static String getNumber(String text) {
        if (TextUtils.isEmpty(text)) return text;
        Matcher matcher = Pattern.compile("\\d+").matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return text;
    }

    public static int parseInt(String text, int defaultValue) {
        if (TextUtils.isEmpty(text)) return defaultValue;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static String getCount(String text) {
        if (TextUtils.isEmpty(text)) return "0";
        return getNumber(text.trim());
    }

    public static String getBlogApp(String authorUrl) {
        if (authorUrl == null) return null;
        try {
            HttpUrl httpUrl = HttpUrl.parse(authorUrl);
            if (httpUrl != null) {
                List<String> pathSegments = httpUrl.pathSegments();
                String path = null;
                for (String pathSegment : pathSegments) {
                    if (!TextUtils.isEmpty(pathSegment)) {
                        path = pathSegment;
                    }
                }
                if (!TextUtils.isEmpty(path)) {
                    return path;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (authorUrl.contains("home.cnblogs.com")) {
            try {
                authorUrl = authorUrl.replace("//", "http://");
                Uri uri = Uri.parse(authorUrl);
                return uri.getLastPathSegment();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return authorUrl
                .replace("https", "")
                .replace("http", "")
                .replace("://www.cnblogs.com/", "")
                .replace("/u/", "")
                .replace("/", "");
    }

    /**
     * 引用评论内容
     *
     * @param comment 要引用的评论
     * @param content 你要发表的内容
     * @return 内容
     */
    public static String getCommentContent(BlogCommentBean comment, String content) {
        // {"blogApp":"silenttiger","postId":6323406,"body":"@TCG2008\n[quote]网页应用都差不多，什么QQ上的应用宝，空间的应用啊，百度轻应用...主要都是为了引流，你一个小公司当然要从微信百度上引导别人使用你的产品啦。[/quote]\naa","parentCommentId":"3608347"}
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(comment.getBlogApp());
        sb.append("\n");
        sb.append("[quote]");
        sb.append(comment.getBody());
        sb.append("[/quote]");
        sb.append("\n");
        sb.append(content);
        return sb.toString();
    }

    /**
     * 获取回复评论内容
     *
     * @param comment 要回复的评论
     * @param content 你要回复的内容
     */
    public static String getAtCommentContent(BlogCommentBean comment, String content) {
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(comment.getBlogApp());
        sb.append("\n");
        sb.append(content);
        return sb.toString();
    }

    public static String getUserAlias(String text) {
        if (TextUtils.isEmpty(text)) return text;
        // showCommentBox(1243228,607820);return false;
        Matcher matcher = Pattern.compile("\\d+").matcher(text);
        while (matcher.find()) {
            text = matcher.group();
        }
        return text;
    }


    public static String getNumber(String text, int index) {
        if (TextUtils.isEmpty(text)) return text;
        Matcher matcher = Pattern.compile("\\d+").matcher(text);
        int i = 0;
        while (matcher.find()) {
            text = matcher.group();
            if (i == index)
                return text;
        }
        return text;
    }
}
