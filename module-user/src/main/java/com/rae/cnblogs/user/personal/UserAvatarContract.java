package com.rae.cnblogs.user.personal;

import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.basic.IPresenterView;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface UserAvatarContract {
    interface Presenter extends IPresenter {
        void upload();
    }

    interface View extends IPresenterView {
        String getUploadPath();

        void onUploadSuccess();

        void onUploadFailed(String message);
    }
}
