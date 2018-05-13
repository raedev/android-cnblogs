package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 博客实体
 * 注意：序列化的时候不要传数据大的这两个字段（summary、content)
 * Created by ChenRui on 2016/11/28 23:45.
 */
//@Table(name = "blogs")
public class BlogBean implements Parcelable {

    private String title;

    private String url;

    private String avatar;

    private String summary;

    private String author;

    private String authorUrl;

    private String comment;

    private String views;

    private String postDate;

    private String blogId;

    private String blogApp;

    private String tag; // 标签


    private String categoryId; // 所属分类


    private String thumbUrls; // 预览小图,JSON 格式，比如：["http://img.cnblogs.com/a.jpg","http://img.cnblogs.com/b.jpg"]

    protected boolean isReaded;

    /**
     * 博客类型，参考取值{@link BlogType#getTypeName()}
     */

    private String blogType;
    private List<String> mThumbList;

    public String getBlogType() {
        return blogType;
    }

    public void setBlogType(String blogType) {
        this.blogType = blogType;
    }

    public String getBlogApp() {
        return blogApp;
    }

    public String getThumbUrls() {
        return thumbUrls;
    }

    public void setThumbUrls(String thumbUrls) {
        this.thumbUrls = thumbUrls;
    }

    public void setBlogApp(String blogApp) {
        this.blogApp = blogApp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    private String likes; // 点赞

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public BlogBean() {
        super();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getBlogId() {
        return blogId;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlogBean && !TextUtils.isEmpty(blogId)) {
            return TextUtils.equals(blogId, ((BlogBean) obj).getBlogId());
        }
        return super.equals(obj);
    }


    /**
     * 获取小图
     */
    @Nullable
    public List<String> getThumbs() {
        if (TextUtils.isEmpty(thumbUrls)) return null;
        try {
            if (mThumbList == null) {
                mThumbList = new Gson().fromJson(thumbUrls, new TypeToken<List<String>>() {
                }.getType());
//                mThumbList = JSON.parseArray(thumbUrls, String.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mThumbList;
    }

    @Override
    public String toString() {
        return super.toString() + "@" + title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.avatar);
//        dest.writeString(this.summary);  //  不传递大数据
        dest.writeString(this.author);
        dest.writeString(this.authorUrl);
        dest.writeString(this.comment);
        dest.writeString(this.views);
        dest.writeString(this.postDate);
        dest.writeString(this.blogId);
        dest.writeString(this.blogApp);
        dest.writeString(this.tag);
        dest.writeString(this.categoryId);
        dest.writeString(this.thumbUrls);
        dest.writeByte(this.isReaded ? (byte) 1 : (byte) 0);
        dest.writeString(this.blogType);
        dest.writeStringList(this.mThumbList);
//        dest.writeString(this.content); //  不传递大数据
        dest.writeString(this.likes);
    }

    protected BlogBean(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.avatar = in.readString();
//        this.summary = in.readString(); //  不传递大数据
        this.author = in.readString();
        this.authorUrl = in.readString();
        this.comment = in.readString();
        this.views = in.readString();
        this.postDate = in.readString();
        this.blogId = in.readString();
        this.blogApp = in.readString();
        this.tag = in.readString();
        this.categoryId = in.readString();
        this.thumbUrls = in.readString();
        this.isReaded = in.readByte() != 0;
        this.blogType = in.readString();
        this.mThumbList = in.createStringArrayList();
//        this.content = in.readString(); //  不传递大数据
        this.likes = in.readString();
    }

    public static final Creator<BlogBean> CREATOR = new Creator<BlogBean>() {
        @Override
        public BlogBean createFromParcel(Parcel source) {
            return new BlogBean(source);
        }

        @Override
        public BlogBean[] newArray(int size) {
            return new BlogBean[size];
        }
    };
}
