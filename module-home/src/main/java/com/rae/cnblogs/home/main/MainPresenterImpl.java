package com.rae.cnblogs.home.main;

import com.rae.cnblogs.basic.AppMobclickAgent;
import com.rae.cnblogs.basic.ApplicationCompat;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.BuildConfig;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IRaeServerApi;
import com.rae.cnblogs.sdk.bean.VersionInfo;

public class MainPresenterImpl extends BasicPresenter<MainContract.View> implements MainContract.Presenter {

    private IRaeServerApi mRaeServerApi;

    public MainPresenterImpl(MainContract.View view) {
        super(view);
        mRaeServerApi = CnblogsApiFactory.getInstance(getContext()).getRaeServerApi();
        mobclickOpenEvent();
        // 检查更新
        checkUpdate();
    }


    @Override
    protected void onStart() {
    }

    /**
     * 统计打开时间
     */
    private void mobclickOpenEvent() {
        String blogApp = null;
        if (UserProvider.getInstance().isLogin()) {
            blogApp = UserProvider.getInstance().getLoginUserInfo().getBlogApp();
        }
        AppMobclickAgent.onAppOpenEvent(getContext(), blogApp);
    }

    private void checkUpdate() {
        final int versionCode = ApplicationCompat.getVersionCode(getContext());
        String versionName = ApplicationCompat.getVersionName(getContext());
        String channel = ApplicationCompat.getChannel(getContext());
        AndroidObservable
                .create(mRaeServerApi.versionInfo(versionCode, versionName, channel, BuildConfig.BUILD_TYPE))
                .with(this)
                .subscribe(new ApiDefaultObserver<VersionInfo>() {
                    @Override
                    protected void onError(String message) {
                        // 更新出错不提示
//                        VersionInfo versionInfo = new VersionInfo();
//                        versionInfo.setVersionName("v2.0.1");
//                        versionInfo.setAppDesc("测试更新");
//                        versionInfo.setDownloadUrl("https://www.baidu.com");
//                        versionInfo.setVersionCode("12");
//                        getView().onNewVersion(versionInfo);
                    }

                    @Override
                    protected void accept(VersionInfo versionInfo) {
                        getView().onNewVersion(versionInfo);
                    }
                });
    }
}
