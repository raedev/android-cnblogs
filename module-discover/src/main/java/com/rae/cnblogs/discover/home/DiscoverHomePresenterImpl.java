package com.rae.cnblogs.discover.home;

import com.antcode.sdk.AntCodeSDK;
import com.antcode.sdk.IAntAdApi;
import com.antcode.sdk.IAntColumnApi;
import com.antcode.sdk.IAntUserApi;
import com.antcode.sdk.model.AntAdInfo;
import com.antcode.sdk.model.AntAppConfigInfo;
import com.antcode.sdk.model.AntColumnInfo;
import com.rae.cnblogs.discover.AntCodeBasicPresenter;
import com.rae.cnblogs.discover.AntSdkDefaultObserver;

import java.util.List;

public class DiscoverHomePresenterImpl extends AntCodeBasicPresenter<IDiscoverHomeContract.View> implements IDiscoverHomeContract.Presenter {

    private final IAntAdApi mAdApi;
    private final IAntColumnApi mColumnApi;
    private final IAntUserApi mUserApi;

    public DiscoverHomePresenterImpl(IDiscoverHomeContract.View view) {
        super(view);
        AntCodeSDK antCodeSDK = AntCodeSDK.getInstance();
        mAdApi = antCodeSDK.getAdApi();
        mColumnApi = antCodeSDK.getColumnApi();
        mUserApi = antCodeSDK.getUserApi();
    }

    @Override
    protected void onStart() {
        // load category
        mUserApi.getAppConfig().with(this)
                .subscribe(new AntSdkDefaultObserver<AntAppConfigInfo>() {
                    @Override
                    protected void onError(String message) {
                    }

                    @Override
                    protected void accept(AntAppConfigInfo antAppConfigInfo) {
                        getView().onLoadCategories(antAppConfigInfo.getHomeTabs());
                    }
                });


        // load home ads
        mAdApi.getHomePageAds()
                .with(this)
                .subscribe(new AntSdkDefaultObserver<List<AntAdInfo>>() {
                    @Override
                    protected void onError(String message) {
                    }

                    @Override
                    protected void accept(List<AntAdInfo> ads) {
                        getView().onLoadAds(ads);
                    }
                });

        // load home columns

        mColumnApi.getHomeColumns().with(this).subscribe(new AntSdkDefaultObserver<List<AntColumnInfo>>() {
            @Override
            protected void onError(String message) {
            }

            @Override
            protected void accept(List<AntColumnInfo> columns) {
                getView().onLoadColumns(columns);
            }
        });
    }
}
