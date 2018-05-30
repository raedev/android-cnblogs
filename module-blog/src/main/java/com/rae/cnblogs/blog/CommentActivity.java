package com.rae.cnblogs.blog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.blog.fragment.CommentFragment;

/**
 * 评论
 * Created by ChenRui on 2017/10/21 0021 2:54.
 */
@Route(path = AppRoute.PATH_BLOG_COMMENT)
public class CommentActivity extends SwipeBackBasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        ContentEntity entity = getIntent().getParcelableExtra("entity");
        CommentFragment fragment = CommentFragment.newInstance(entity);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }
}
