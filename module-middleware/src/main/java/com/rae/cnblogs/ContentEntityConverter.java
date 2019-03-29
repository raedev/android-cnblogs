package com.rae.cnblogs;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.sdk.AppGson;
import com.rae.cnblogs.sdk.bean.BlogBean;

import java.util.ArrayList;

/**
 * Created by rae on 2018/6/4.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public final class ContentEntityConverter {

    private ContentEntityConverter() {
    }

    public static BlogBean convertToBlog(ContentEntity b) {
        BlogBean m = new BlogBean();
        m.setBlogType(b.getType());
        m.setBlogId(b.getId());
        m.setAuthor(b.getAuthor());
        m.setAvatar(b.getAvatar());
        m.setTitle(b.getTitle());
        m.setSummary(b.getSummary());
        m.setPostDate(b.getDate());
        m.setViews(b.getViewCount());
        m.setComment(b.getCommentCount());
        m.setLikes(b.getLikeCount());
        m.setBlogApp(b.getAuthorId());
        m.setUrl(b.getUrl());
        m.setThumbUrls(AppGson.toJson(b.getThumbs()));
        return m;
    }

    public static ContentEntity convert(BlogBean b) {
        ContentEntity m = new ContentEntity();
        m.setType(b.getBlogType());
        m.setId(b.getBlogId());
        m.setAuthor(b.getAuthor());
        m.setAvatar(b.getAvatar());
        m.setTitle(b.getTitle());
        m.setSummary(b.getSummary());
        m.setDate(b.getPostDate());
        m.setViewCount(b.getViews());
        m.setCommentCount(b.getComment());
        m.setLikeCount(b.getLikes());
        m.setAuthorId(b.getBlogApp());
        m.setUrl(b.getUrl());
        m.setThumbs((ArrayList<String>) b.getThumbs());
        return m;
    }
}
