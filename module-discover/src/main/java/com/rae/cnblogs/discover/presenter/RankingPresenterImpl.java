package com.rae.cnblogs.discover.presenter;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IRankingApi;
import com.rae.cnblogs.sdk.bean.HotSearchBean;

import java.util.List;

import io.reactivex.Observable;

public class RankingPresenterImpl extends BasicPresenter<IRankingContract.View> implements IRankingContract.Presenter {


    private PageObservable<HotSearchBean> mPageObservable;

    public RankingPresenterImpl(IRankingContract.View view) {
        super(view);
        mPageObservable = new RankingPageObservable(view, this);
    }

    @Override
    protected void onStart() {
        mPageObservable.start();
    }

    @Override
    public void loadMore() {
        // 热门搜索只有一页
        if (mPageObservable.getCurrentPage() > 1 && getView().getType() == IRankingContract.TYPE_HOT_SEARCH) {
            getView().onNoMoreData();
            return;
        }
        mPageObservable.loadMore();
    }


    private class RankingPageObservable extends PageObservable<HotSearchBean> {
        private IRankingApi mRankingApi;

        public RankingPageObservable(IRankingContract.View view, IPresenter presenter) {
            super(view, presenter);
            mRankingApi = CnblogsApiFactory.getInstance(view.getContext()).getRankingApi();
        }

        @Override
        protected Observable<List<HotSearchBean>> onCreateObserver(int page) {
            int type = getView().getType();
            Observable<List<HotSearchBean>> observable;
            switch (type) {
                case IRankingContract.TYPE_HOT_SEARCH:
                    observable = mRankingApi.hotSearch();
                    break;
                case IRankingContract.TYPE_TOP_READ:
                    observable = mRankingApi.topRead(page);
                    break;
                case IRankingContract.TYPE_TOP_FAVORITE:
                    observable = mRankingApi.topFavorite(page);
                    break;
                case IRankingContract.TYPE_TOP_AUTHOR:
                default:
                    observable = mRankingApi.topAuthor(page);
                    break;

            }

            return observable;
        }
    }
}
