package com.rae.cnblogs.moment.post;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rae.cnblogs.basic.BasicPresenter;
import com.rae.cnblogs.basic.Rx;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.moment.service.MomentIntentService;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.rae.cnblogs.sdk.model.ImageMetaData;
import com.rae.cnblogs.sdk.model.MomentMetaData;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布闪存
 * Created by ChenRui on 2017/10/27 0027 14:37.
 */
public class PostMomentPresenterImpl extends BasicPresenter<IPostMomentContract.View>
        implements IPostMomentContract.Presenter {

    private IMomentApi mMomentApi;
    private IBlogApi mBlogApi;
    private boolean mIsBlogOpened = true;
    private CnblogAppConfig mConfig;

    public PostMomentPresenterImpl(IPostMomentContract.View view) {
        super(view);
        CnblogsApiProvider apiProvider = CnblogsApiFactory.getInstance(getContext());
        mMomentApi = apiProvider.getMomentApi();
        mBlogApi = apiProvider.getBlogApi();
        mConfig = CnblogAppConfig.getInstance(getContext());
    }

    @Override
    protected void onStart() {
        boolean isLogin = UserProvider.getInstance().isLogin();
        if (isLogin) return;
        AndroidObservable
                .create(mBlogApi.checkBlogIsOpen())
                .with(this)
                .subscribe(new ApiDefaultObserver<Boolean>() {
                    @Override
                    protected void onError(String message) {
                        getView().onLoadBlogOpenStatus(false);
                    }

                    @Override
                    protected void accept(Boolean value) {
                        mIsBlogOpened = value;
                        getView().onLoadBlogOpenStatus(value);
                    }
                });
    }

    @Override
    public boolean post() {
        String content = getView().getContent().trim();
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        String imageContent = withImageContent(getView().getImageUrls());
        int imageLength = 0;
        String noUrlIng;
        if (!TextUtils.isEmpty(imageContent)) {
            noUrlIng = content + imageContent;
            imageLength = replaceText(imageContent).length();
        } else {
            noUrlIng = content;
        }

        noUrlIng = replaceText(noUrlIng);
        if (noUrlIng.length() > 240) {
            getView().onPostMomentFailed("请精简一下内容，文字加图片不要超过240字。\n当前图片占用字符数：" + imageLength + "个\n当前字符数: " + noUrlIng.length() + "个");
            return false;
        }

        int size = Rx.getCount(getView().getImageUrls());

        if (size > 0) {
            // 发表图文，组合参数，发送到后台上传
            Intent intent = new Intent(getContext(), MomentIntentService.class);
            MomentMetaData metaData = new MomentMetaData();
            metaData.content = content;
            metaData.images = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ImageMetaData m = new ImageMetaData();
                m.localPath = getView().getImageUrls().get(i);
                metaData.images.add(m);
            }
            intent.putExtra(Intent.EXTRA_TEXT, metaData);
            getContext().startService(intent);

            // 后台发布闪存提示
            boolean showTips = mConfig.getPostMomentInProgressTips();
            getView().onPostMomentInProgress(showTips);
            return false;
        }
        // 加上客户端标志
        content = "[来自Android客户端] " + content;
        AndroidObservable.create(mMomentApi.publish(content, 1))
                .with(this)
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        getView().onPostMomentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        getView().onPostMomentSuccess();
                    }
                });
        return true;
    }

    @Override
    public boolean isBlogOpened() {
        return mIsBlogOpened;
    }

    @Nullable
    private String withImageContent(List<String> images) {
        if (Rx.isEmpty(images)) return null;
        // 格式：内容 #img图片地址#end
        StringBuilder sb = new StringBuilder();
        sb.append("#img");
        JSONArray array = new JSONArray();
        int size = images.size();
        for (int i = 0; i < size; i++) {
            // 占位，用于计算字符大小
            array.put("http://t.cn/1234567 ");
        }
        sb.append(array.toString());
        sb.append("#end");
        return sb.toString();
    }

    /**
     * 发布闪存需要替换的特殊字符
     */
    private String replaceText(String text) {
        if (TextUtils.isEmpty(text)) return text;
        return text
                .replaceAll("(http|ftp|https):\\/\\/([^\\/:,，]+)(:\\d+)?(\\/[^\\u0391-\\uFFE5\\s,]*)?", "")
                .replaceAll("(\\s)+", "")
                .replaceAll("[^\\x00-\\xff]", "aa");
    }
}
