package com.rae.cnblogs.web.client;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.webkit.JavascriptInterface;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.ContentEntityConverter;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.ContentEntity;
import com.rae.cnblogs.dialog.DefaultDialogFragment;
import com.rae.cnblogs.middleware.R;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.rae.cnblogs.sdk.parser.BlogInfoParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by rae on 2018/6/4.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class AppJavaScript extends RaeJavaScriptBridge {

    private final String mFrom;
    private final FragmentManager mFragmentManager;


    public AppJavaScript(Context context, FragmentManager fragmentManager, String from) {
        super(context);
        mFragmentManager = fragmentManager;
        mFrom = from;
    }

    @Override
    @JavascriptInterface
    public void setHtml(@NonNull String html) {
        boolean isLoad = getHtml() != null;
        super.setHtml(html);
        // 有来源的，不启用这个规则
        // 如果有来源的，并且操作了网页，也可以启动规则
        if (mFrom != null && !isLoad) {
            return;
        }
        // 当网页加载完毕的时候，自动检查是否为博文，跳转到对应的博文界面
        Document document = Jsoup.parse(html);
        final String title = document.title();

        Elements elements = document.select(".postBody");
        if (elements.size() <= 0) {
            return;
        }
        BlogInfoParser blogInfoParser = new BlogInfoParser();
        BlogBean blogBean = blogInfoParser.parse(document, html);
        final ContentEntity entity = ContentEntityConverter.convert(blogBean);
        // 是博文页面
        if (!CnblogAppConfig.getInstance(getContext()).canReaderTips()) {
            UICompat.toast(getContext(), "已为您优化博客阅读体验");
            AppRoute.routeToContentDetail(getContext(), entity);
            return;
        }

        new DefaultDialogFragment.Builder()
                .canceledOnTouchOutside(false)
                .cancelable(true)
                .message(getString(R.string.tips_web_blog_route, title))
                .confirmText(getString(R.string.view_now))
                .confirm(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AppRoute.routeToContentDetail(getContext(), entity);
                    }
                })
                .show(mFragmentManager);
    }

}
