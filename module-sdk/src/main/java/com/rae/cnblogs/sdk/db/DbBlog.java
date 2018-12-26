package com.rae.cnblogs.sdk.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogBeanDao;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.DaoSession;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;
import com.rae.cnblogs.sdk.db.model.UserBlogInfoDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;


/**
 * 博客数据库
 * <p>说明：</p>
 * <p>1.1.4之前使用了数据库缓存博文内容，导致数据库文件过大。</p>
 * <p>1.1.5版本改成文件的形式保存，格式为：blogId + blogType 取MD5值保存。</p>
 * <p>Created by ChenRui on 2017/1/25 0025 16:56.</p>
 */
public class DbBlog {

    private final DaoSession mSession;

    DbBlog() {
        mSession = DbCnblogs.getSession();
    }

    public void add(BlogBean data) {
        mSession.getBlogBeanDao().insertOrReplace(data);
    }

    /**
     * 批量添加博客，默认把分类Id都设置成为传入的分类参数
     *
     * @param categoryId 分类Id
     */
    public void addAll(List<BlogBean> blogs, String categoryId) {
        for (BlogBean blog : blogs) {
            blog.setCategoryId(categoryId);
        }
        addAll(blogs);
    }

    /**
     * 批量添加博客
     */
    public void addAll(List<BlogBean> blogs) {
        mSession.getBlogBeanDao().insertOrReplaceInTx(blogs);
    }

    /**
     * 清除缓存
     * 1、博客数据
     * 2、用户的博客数据
     */
    void clearCache() {
        // 1、清除blogs表的缓存
        mSession.getBlogBeanDao().deleteAll();
        mSession.getUserBlogInfoDao().deleteAll();
    }

    /**
     * 博客是否存在
     */
    public boolean exists(String blogId) {
        return mSession.getBlogBeanDao().queryBuilder().where(BlogBeanDao.Properties.BlogId.eq(blogId)).count() > 0;
    }

    /**
     * 获取浏览记录
     */
    public List<BlogBean> getRecentHistory(int page) {

        return mSession.getBlogBeanDao()
                .queryBuilder()
                .where(BlogBeanDao.Properties.IsRead.eq(true))
                .limit(20)
                .offset(Math.max(0, page - 1) * 20)
                .orderDesc(BlogBeanDao.Properties.UpdateTime)
                .list();
    }


    /**
     * 清除浏览记录
     */
    public void clearRecentHistory() {
        Database database = mSession.getDatabase();
        String tableName = BlogBeanDao.TABLENAME;
        String columnName = BlogBeanDao.Properties.IsRead.columnName;
        database.beginTransaction();
        try {
            database.execSQL(String.format("update %s set %s=0 where %s=1", tableName, columnName, columnName));
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    /**
     * 查询所有博客数据
     */
    public List<BlogBean> findAll() {
        return mSession.getBlogBeanDao().loadAll();
    }

    /**
     * 获取没有内容的列表
     */
    public List<BlogBean> findAllWithoutBlogContent() {
        return mSession.getBlogBeanDao().queryBuilder().where(BlogBeanDao.Properties.Content.isNull()).list();
    }

    /**
     * 获取用户博客信息
     */
    @Nullable
    public UserBlogInfo get(String blogId) {
        if (TextUtils.isEmpty(blogId)) return null;
        UserBlogInfo info = mSession.getUserBlogInfoDao()
                .queryBuilder()
                .where(UserBlogInfoDao.Properties.BlogId.eq(blogId))
                .unique();

        return info;
    }

    /**
     * 获取博客信息
     */
    @Nullable
    public BlogBean getBlog(String blogId) {
        return mSession.getBlogBeanDao().queryBuilder().where(BlogBeanDao.Properties.BlogId.eq(blogId)).unique();
    }

    /**
     * 获取博客内容
     *
     * @param blogType 博客类型
     * @param blogId   博客Id
     */
    @Nullable
    public String getBlogContent(String blogType, String blogId) {
        // 找到博客信息
        BlogBean blogInfo = mSession.getBlogBeanDao()
                .queryBuilder()
                .where(BlogBeanDao.Properties.BlogId.eq(blogId), BlogBeanDao.Properties.BlogType.eq(blogType))
                .build()
                .unique();

        if (blogInfo == null) return null;

        try {
            String path = blogInfo.getContent();
            if (TextUtils.isEmpty(path) || !new File(path).exists()) return null;
            FileInputStream inputStream = new FileInputStream(path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[128];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            String content = outputStream.toString();
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取指定分类的博客列表
     *
     * @param category 分类名称
     * @param page     分页
     * @param type     类型
     */
    @Nullable
    public List<BlogBean> getList(@Nullable String category, int page, BlogType type) {
        if (category == null) return null;
        QueryBuilder<BlogBean> builder = mSession.getBlogBeanDao().queryBuilder();
        return builder.where(BlogBeanDao.Properties.BlogType.eq(type.getTypeName()))
                .where(BlogBeanDao.Properties.CategoryId.eq(category))
                .orderDesc(BlogBeanDao.Properties.BlogId)
                .offset(page * 20)
                .limit(20)
                .list();
    }

    /**
     * 删除收藏
     *
     * @param url 路径
     */
    public void removeBookmarks(final String url) {
        QueryBuilder<UserBlogInfo> builder = mSession.getUserBlogInfoDao().queryBuilder();
        builder.join(UserBlogInfoDao.Properties.BlogId, BlogBean.class, BlogBeanDao.Properties.BlogId)
                .where(UserBlogInfoDao.Properties.IsBookmarks.eq(true),
                        BlogBeanDao.Properties.Url.like(String.format("%%%s%%", url)));

        List<UserBlogInfo> userBlogs = builder.list();

        if (userBlogs == null || userBlogs.size() <= 0 || TextUtils.isEmpty(userBlogs.get(0).getBlogId()))
            return;

        UserBlogInfo m = userBlogs.get(0);
        m.setBookmarks(false);
        mSession.getUserBlogInfoDao().update(m);
    }

    /**
     * 保存用户的博客信息
     */
    public void saveUserBlogInfo(UserBlogInfo m) {
        mSession.getUserBlogInfoDao().save(m);
    }

    /**
     * 更新博客信息
     */
    public void updateBlog(BlogBean m) {
        mSession.getBlogBeanDao().update(m);
    }

    /**
     * 更新博客信息
     */
    public void updateUserBlog(UserBlogInfo m) {
        mSession.getUserBlogInfoDao().update(m);
    }

    /**
     * 根据标题删除本地收藏
     */
    public void deleteBlogBookmark(String title) {
        List<BlogBean> list = mSession.getBlogBeanDao().queryBuilder().where(BlogBeanDao.Properties.Title.eq(title)).list();
        if (list.size() <= 0) return;
        BlogBean blogBean = list.get(0);
        UserBlogInfo userBlogInfo = get(blogBean.getBlogId());
        if (userBlogInfo == null) return;
        userBlogInfo.setIsBookmarks(false);
        userBlogInfo.setBookmarks(false);
        updateUserBlog(userBlogInfo);
    }

    /**
     * 更新博客内容
     *
     * @param blogId   博客Id
     * @param blogType 博客类型
     * @param content  博客内容
     */
    public void updateBlogContent(@NonNull String blogId, @NonNull String blogType, @NonNull String content) {
        try {
            // 找到博客信息
            BlogBean blogInfo = mSession.getBlogBeanDao()
                    .queryBuilder()
                    .where(BlogBeanDao.Properties.BlogId.eq(blogId), BlogBeanDao.Properties.BlogType.eq(blogType))
                    .build()
                    .unique();

            if (blogInfo == null) {
                Log.e("rae", "Update blog content error,because the blog entity not found.");
                return;
            }

            // 写入文件
            String fileName = Base64.encodeToString((blogId + blogType).getBytes(), Base64.NO_WRAP);
            File dir = DbCnblogs.getInstance().getCacheDir();
            File file = new File(dir, fileName);
            if (file.exists() && file.delete()) {
                Log.w("cnblogs", "delete content file success, path is " + file.getPath());
            }

            // 写文本内容
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
            // 保存路径
            blogInfo.setContent(file.getPath());
            mSession.getBlogBeanDao().save(blogInfo);
        } catch (Exception e) {
            Log.e("cnblogs", "update blog content failed! id is " + blogId, e);
        }

    }

}
