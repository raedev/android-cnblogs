package com.rae.cnblogs.moment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.moment.fragment.MomentFragment;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IMomentApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 提到我的闪存
 * Created by ChenRui on 2017/11/8 0008 10:19.
 */
@Route(path = AppRoute.PATH_MOMENT_MENTION)
public class MomentAtMeActivity extends SwipeBackBasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        MomentFragment fragment = MomentFragment.newInstance(IMomentApi.MOMENT_TYPE_AT_ME);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, fragment).commitNow();

        // 更新阅读状态
        IMomentApi momentApi = CnblogsApiFactory.getInstance(this).getMomentApi();
        momentApi.updateAtMeToRead(System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {

                    }

                    @Override
                    protected void accept(Empty empty) {

                    }
                });
    }


}
