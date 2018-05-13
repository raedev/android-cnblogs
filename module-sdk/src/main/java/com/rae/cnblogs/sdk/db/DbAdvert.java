package com.rae.cnblogs.sdk.db;


import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.cnblogs.sdk.bean.AdvertBeanDao;

import io.reactivex.Observable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 广告表
 * Created by ChenRui on 2016/12/22 23:12.
 */
public class DbAdvert {

    private final AdvertBeanDao mAdvertBeanDao;

    public DbAdvert() {
        mAdvertBeanDao = DbCnblogs.getSession().getAdvertBeanDao();
    }

    public AdvertBean getLauncherAd() {
        return mAdvertBeanDao.queryBuilder()
                .where(AdvertBeanDao.Properties.MAdType.eq("CNBLOG_LAUNCHER"))
                .orderDesc(AdvertBeanDao.Properties.MAdId)
                .limit(1)
                .build()
                .unique();
    }

    public void save(final AdvertBean data) {
        Observable.just(data)
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<AdvertBean>() {
                    @Override
                    public void onNext(AdvertBean advertBean) {
                        //  保存
                        mAdvertBeanDao.deleteAll();
                        mAdvertBeanDao.insertOrReplace(data);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 清除缓存
     */
    void clearCache() {
        Observable.just(mAdvertBeanDao)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<AdvertBeanDao>() {
                    @Override
                    public void onNext(AdvertBeanDao advertBeanDao) {
                        advertBeanDao.deleteAll();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
