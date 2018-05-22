package com.rae.cnblogs.moment.message;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * moment
 * Created by ChenRui on 2017/10/27 0027 10:57.
 */
public class MomentMessagePresenterImpl extends BasicPresenter<IMomentMessageContract.View> implements IMomentMessageContract.Presenter {

    private IMomentApi mMomentApi;

    private PageObservable<MomentCommentBean> mPageObservable;


    public MomentMessagePresenterImpl(IMomentMessageContract.View view) {
        super(view);
        mMomentApi = CnblogsApiFactory.getInstance(getContext()).getMomentApi();
        mPageObservable = new PageObservable<MomentCommentBean>(view, this) {
            @Override
            protected Observable<List<MomentCommentBean>> onCreateObserver(int page) {
                return mMomentApi.getReplyMeMoments(IMomentApi.MOMENT_TYPE_REPLY_ME, page, System.currentTimeMillis());
            }

            @Override
            protected void onLoadDataComplete(List<MomentCommentBean> dataList) {
                super.onLoadDataComplete(dataList);
                if (!Rx.isEmpty(dataList)) {
                    // 更新消息数量
                    updateReplyMeToRead(dataList.get(0).getId());
                }
            }

            private void updateReplyMeToRead(String id) {
                mMomentApi.updateRelyMeToRead(id, System.currentTimeMillis())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new ApiDefaultObserver<Empty>() {
                            @Override
                            protected void onError(String message) {
                            }

                            @Override
                            protected void accept(Empty empty) {
                            }
                        });
            }
        };
    }

    @Override
    public void postComment(String ingId, String userId, String commentId, String content) {
        AndroidObservable.create(mMomentApi.postComment(ingId, userId, commentId, content))
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onPostCommentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        getView().onPostCommentSuccess();
                    }
                });
    }

    @Override
    protected void onStart() {
        mPageObservable.start();
    }

    @Override
    public void destroy() {
        super.destroy();
        mPageObservable.destroy();
    }

    @Override
    public void loadMore() {
        mPageObservable.loadMore();
    }

}
