package com.rae.cnblogs.home.launcher;

import com.rae.cnblogs.basic.BasicPresenter;

/**
 * 启动页
 */
public class LauncherPresenterImpl extends BasicPresenter<LauncherContract.View> implements LauncherContract.Presenter {

    public LauncherPresenterImpl(LauncherContract.View view) {
        super(view);
    }

    @Override
    protected void onStart() {

    }
}
