package com.rae.cnblogs.sdk.db;

import android.text.TextUtils;

import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

import java.util.List;

import io.reactivex.annotations.Nullable;

/**
 * 博客数据库
 * Created by ChenRui on 2017/1/25 0025 16:56.
 */
public class DbBlog extends DbCnblogs {

    DbBlog() {
    }

    public UserBlogInfo get(String blogId) {
        if (TextUtils.isEmpty(blogId)) return null;
//        return new Select().from(UserBlogInfo.class).where("blogId=?", blogId).executeSingle();
        return null;
    }

    public List<BlogBean> getList(String category, int page, BlogType type) {
        return null;
//        return new Select().from(BlogBean.class).where("blogType=? and categoryId=?", type.getTypeName(), category).orderBy("blogId desc").offset(page * 20).limit(20).execute();
    }

    @Nullable
    public BlogBean getBlog(String blogId) {
//        try {
//            return new Select().from(BlogBean.class).where("blogId=?", blogId).executeSingle();
//        } catch (OutOfMemoryError e) {
//            // fix bug #616 内存空间满了
//            DbFactory.getInstance().clearData();
//        }
        return null;
    }

    public void saveBlogInfo(UserBlogInfo m) {
//        m.save();
    }


    public boolean exists(String blogId) {
        return false;
//        return new Select().from(BlogBean.class).where("blogId=?", blogId).exists();
    }

    public void addAll(final List<BlogBean> blogs, final String categoryId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 执行插入
                executeTransaction(new Runnable() {
                    @Override
                    public void run() {
                        for (BlogBean blog : blogs) {
                            blog.setCategoryId(categoryId);
                            // 查找是否已经有了，有了就跳过
                            if (exists(blog.getBlogId())) {
//                                Log.w("rae-db", "跳过：" + blog.getBlogId() + " = " + blog.getTitle());
                                continue;
                            }

//                            Log.i("rae-db", "插入数据库：" + blog.getBlogId() + " = " + blog.getTitle());
//                            blog.save();
                        }
                    }
                });

            }
        }).start();
    }

    public List<BlogBean> findAll() {
        return null;
//        return new Select().from(BlogBean.class).execute();
    }

    /**
     * 获取没有内容的列表
     *
     * @return
     */
    public List<BlogBean> findAllWithoutBlogContent() {
        return null;
//        return new Select().from(BlogBean.class).as("blog").leftJoin(UserBlogInfo.class).as("info").on("blog.blogId=info.blogId").where("info.content is NULL").execute();
    }

    /**
     * 删除收藏
     *
     * @param url 路径
     */
    public void removeBookmarks(final String url) {
        // 找到已经收藏的
        new Thread(new Runnable() {
            @Override
            public void run() {
//
//                UserBlogInfo model = new Select()
//                        .from(UserBlogInfo.class).as("a")
//                        .leftJoin(BlogBean.class).as("b")
//                        .on("a.blogId=b.blogId")
//                        .where("a.isBookmarks=?", 1)
//                        .and("b.url like ?", String.format("%%%s%%", url))
//                        .executeSingle();
//
//
//                if (model == null || TextUtils.isEmpty(model.getBlogId())) return;
//                model.setBookmarks(false);
//                model.save();
            }
        }).start();
    }


    public void updateBlog(BlogBean m) {
//        m.save();
    }

    /**
     * 清除缓存
     * 1、博客数据
     * 2、用户的博客数据
     */
    void clearCache() {
        // 1、清除blogs表的缓存

//        super.executeTransaction(new Runnable() {
//            @Override
//            public void run() {
//                new Delete().from(BlogBean.class).execute();
//                // 2、清除blog_info表的博文内容以及已读状态
//                ActiveAndroid.execSQL("update blog_info set content=null,isRead=0");
//            }
//        });


    }

    public void updateBlogContent(final String blogId, final String blogType, final String content) {
//        executeTransaction(new Runnable() {
//            @Override
//            public void run() {
//                UserBlogInfo blogInfo = new Select().from(UserBlogInfo.class).where("blogId=?", blogId).executeSingle();
//                if (blogInfo == null) {
//                    blogInfo = new UserBlogInfo();
//                }
//
//                blogInfo.setBlogId(blogId);
//                blogInfo.setBlogType(blogType);
//                blogInfo.setContent(content);
//                long id = blogInfo.save();
//
////                Log.e("rae-db", "插入数据库：" + blogType + " -> " + blogId + "; id =" + id + "; 是否为空：" + TextUtils.isEmpty(content));
//            }
//        });

    }

    /**
     * 清除数据
     */
    public void clearData() {
//        executeTransaction(new Runnable() {
//            @Override
//            public void run() {
//                new Delete().from(UserBlogInfo.class).execute();
//                new Delete().from(BlogBean.class).execute();
//            }
//        });
    }
}
