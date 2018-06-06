package com.rae.cnblogs.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * 博客实体
 * 注意：序列化的时候不要传数据大的这两个字段（summary、content)
 * Created by ChenRui on 2016/11/28 23:45.
 */
@Entity(nameInDb = "blogs")
public class BlogBean implements Parcelable {

    @Id
    private String blogId;

    private String title;

    private String url;

    private String avatar;

    private String summary;

    private String author;

    private String authorUrl;

    private String comment;

    private String views;

    private String postDate;


    private String blogApp;

    private String tag; // 标签


    private String categoryId; // 所属分类


    private String thumbUrls; // 预览小图,JSON 格式，比如：["http://img.cnblogs.com/a.jpg","http://img.cnblogs.com/b.jpg"]

    private boolean isRead; // 是否已读

    private Long updateTime; // 更新阅读时间

    /**
     * 博客类型，参考取值{@link BlogType#getTypeName()}
     */
    private String blogType;

    @Transient
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
        return TextUtils.isEmpty(likes) ? "0" : likes;
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
        return TextUtils.isEmpty(comment) ? "0" : comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getViews() {
        return TextUtils.isEmpty(views) ? "0" : views;
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


    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.blogId);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.avatar);
        dest.writeString(this.summary);
        dest.writeString(this.author);
        dest.writeString(this.authorUrl);
        dest.writeString(this.comment);
        dest.writeString(this.views);
        dest.writeString(this.postDate);
        dest.writeString(this.blogApp);
        dest.writeString(this.tag);
        dest.writeString(this.categoryId);
        dest.writeString(this.thumbUrls);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeValue(this.updateTime);
        dest.writeString(this.blogType);
        dest.writeStringList(this.mThumbList);
        dest.writeString(this.content);
        dest.writeString(this.likes);
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    protected BlogBean(Parcel in) {
        this.blogId = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.avatar = in.readString();
        this.summary = in.readString();
        this.author = in.readString();
        this.authorUrl = in.readString();
        this.comment = in.readString();
        this.views = in.readString();
        this.postDate = in.readString();
        this.blogApp = in.readString();
        this.tag = in.readString();
        this.categoryId = in.readString();
        this.thumbUrls = in.readString();
        this.isRead = in.readByte() != 0;
        this.updateTime = (Long) in.readValue(Long.class.getClassLoader());
        this.blogType = in.readString();
        this.mThumbList = in.createStringArrayList();
        this.content = in.readString();
        this.likes = in.readString();
    }

    @Generated(hash = 1110720888)
    public BlogBean(String blogId, String title, String url, String avatar, String summary, String author,
                    String authorUrl, String comment, String views, String postDate, String blogApp, String tag,
                    String categoryId, String thumbUrls, boolean isRead, Long updateTime, String blogType, String content,
                    String likes) {
        this.blogId = blogId;
        this.title = title;
        this.url = url;
        this.avatar = avatar;
        this.summary = summary;
        this.author = author;
        this.authorUrl = authorUrl;
        this.comment = comment;
        this.views = views;
        this.postDate = postDate;
        this.blogApp = blogApp;
        this.tag = tag;
        this.categoryId = categoryId;
        this.thumbUrls = thumbUrls;
        this.isRead = isRead;
        this.updateTime = updateTime;
        this.blogType = blogType;
        this.content = content;
        this.likes = likes;
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
