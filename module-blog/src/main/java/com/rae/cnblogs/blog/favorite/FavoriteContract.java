package com.rae.cnblogs.blog.favorite;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;
import com.rae.cnblogs.sdk.bean.TagBean;

import java.util.List;

/**
 * 收藏
 * Created by rae on 2018/6/1.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface FavoriteContract {
    interface Presenter extends IPresenter {

    }

    interface View extends IPresenterView {

        void onLoadTags(List<TagBean> data);

    }
}
