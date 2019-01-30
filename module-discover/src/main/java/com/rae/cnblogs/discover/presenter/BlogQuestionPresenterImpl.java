package com.rae.cnblogs.discover.presenter;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.IPresenter;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IBlogQuestionApi;
import com.rae.cnblogs.sdk.bean.BlogQuestionBean;

import java.util.List;

import io.reactivex.Observable;

public class BlogQuestionPresenterImpl extends BasicPresenter<IBlogQuestionContract.View> implements IBlogQuestionContract.Presenter {


    private BlogQuestionPageObservable mPageObservable;

    public BlogQuestionPresenterImpl(IBlogQuestionContract.View view) {
        super(view);
        mPageObservable = new BlogQuestionPageObservable(view, this);
    }

    @Override
    protected void onStart() {
        mPageObservable.start();
    }

    @Override
    public void loadMore() {
        mPageObservable.loadMore();
    }


    private class BlogQuestionPageObservable extends PageObservable<BlogQuestionBean> {
        private IBlogQuestionApi mBlogQuestionApi;

        BlogQuestionPageObservable(IBlogQuestionContract.View view, IPresenter presenter) {
            super(view, presenter);
            mBlogQuestionApi = CnblogsApiFactory.getInstance(view.getContext()).getBlogQuestionApi();
        }

        @Override
        protected Observable<List<BlogQuestionBean>> onCreateObserver(int page) {
            int type = getView().getType();
            Observable<List<BlogQuestionBean>> observable;
            switch (type) {
                case IBlogQuestionContract.TYPE_HIGH_SCORE:
                    observable = mBlogQuestionApi.getHighScoreQuestions(page);
                    break;
                case IBlogQuestionContract.TYPE_MY:
                    observable = mBlogQuestionApi.getMyQuestions(page);
                    break;
                case IBlogQuestionContract.TYPE_UNSOLVED:
                default:
                    observable = mBlogQuestionApi.getUnsolvedQuestions(page);
                    break;

            }

            return observable;
        }
    }
}
