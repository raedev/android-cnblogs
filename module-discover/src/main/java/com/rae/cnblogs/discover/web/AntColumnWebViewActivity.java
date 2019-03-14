package com.rae.cnblogs.discover.web;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.activity.WebActivity;
import com.rae.cnblogs.discover.R;

@Route(path = AppRoute.PATH_DISCOVER_COLUMN_WEB)
public class AntColumnWebViewActivity extends WebActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ant_web;
    }

    @Override
    protected boolean canDragBack() {
        return true;
    }

    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(this, 100);
    }

    @Override
    protected int getHomeAsUpIndicator() {
        return 0;
    }
}
