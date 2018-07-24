package com.rae.cnblogs.blog.content;

import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.blog.comm.ContentListContract;

/**
 * Created by rae on 2018/7/24.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface BookmarkListContract extends ContentListContract {
    interface Presenter extends ContentListContract.Presenter {
        void delete(ContentEntity item);
    }

    interface View extends ContentListContract.View {

        void onDeleteBookmarksError(String message);

        void onDeleteBookmarksSuccess(ContentEntity item);
    }
}
