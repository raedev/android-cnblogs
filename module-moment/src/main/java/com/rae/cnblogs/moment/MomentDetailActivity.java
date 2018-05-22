package com.rae.cnblogs.moment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.SwipeBackBasicActivity;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.dialog.ShareDialogFragment;
import com.rae.cnblogs.moment.fragment.MomentDetailFragment;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.widget.PlaceholderView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 详情
 * Created by ChenRui on 2017/11/2 0002 15:01.
 */
@Route(path = AppRoute.PATH_MOMENT_DETAIL)
public class MomentDetailActivity extends SwipeBackBasicActivity {

    private MomentBean mMomentBean;

    @BindView(R2.id.placeholder)
    PlaceholderView mPlaceholderView;
    private String mIngId;
    private String mUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail);
        mMomentBean = getIntent().getParcelableExtra("data");
        mIngId = getIntent().getStringExtra("ingId");
        mUserId = getIntent().getStringExtra("userId");
        if (mMomentBean != null && !TextUtils.isEmpty(mMomentBean.getId())) {
            mPlaceholderView.dismiss();
            attachFragment();
        } else if (!TextUtils.isEmpty(mIngId) && !TextUtils.isEmpty(mUserId)) {
            // 根据闪存ID获取详情
            loadMomentDetail();
        } else {
            mPlaceholderView.empty("参数缺失");
        }
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserProvider.getInstance().isLogin()) {
                    loadMomentDetail();
                } else {
                    AppRoute.routeToLogin(v.getContext());
                }
            }
        });
    }

    @SuppressLint("InvalidR2Usage")
    private void attachFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_content, MomentDetailFragment.newInstance(mMomentBean))
                .commitNow();
    }

    private void loadMomentDetail() {
        if (!UserProvider.getInstance().isLogin()) {
            mPlaceholderView.retry("登录后更精彩");
            return;
        }

        IMomentApi momentApi = CnblogsApiFactory.getInstance(this).getMomentApi();

        AndroidObservable
                .create(momentApi.getMomentDetail(mUserId, mIngId, System.currentTimeMillis()))
                .with(this)
                .subscribe(new ApiDefaultObserver<MomentBean>() {
                    @Override
                    protected void onError(String message) {
                        mPlaceholderView.retry(message);
                    }

                    @Override
                    protected void accept(MomentBean momentBean) {
                        mMomentBean = momentBean;
                        attachFragment();
                        mPlaceholderView.dismiss();
                    }
                });

    }

    @OnClick(R2.id.back)
    public void onBackClick() {

    }

    /**
     * 分享
     */
    @OnClick(R2.id.btn_share)
    public void onShareClick() {
        String title = mMomentBean.getAuthorName() + "：" + mMomentBean.getContent();
        String url = mMomentBean.getSourceUrl();
        ShareDialogFragment.newInstance(
                url,
                title,
                mMomentBean.getContent(),
                mMomentBean.getAvatar(), 0, false, false)
                .show(getSupportFragmentManager(), "share");
    }
}
